package io.github.geniot.inspire;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.geniot.inspire.model.DataRecord;
import io.github.geniot.inspire.model.InvalidType;
import io.github.geniot.inspire.model.ValidationError;
import io.github.geniot.inspire.model.ValidationReport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("*****XML*******");
        System.out.println(validate(processXML()));

        System.out.println("***CSV***");
        System.out.println(validate(processCSV()));

    }

    public static ValidationReport validate(DataRecord[] dataRecords) {
        ValidationReport validationReport = new ValidationReport();
        Set<String> transactionReferences = new HashSet<>();
        for (DataRecord dataRecord : dataRecords) {

            BigDecimal balance = new BigDecimal(dataRecord.getStartBalance());
            BigDecimal mutation = new BigDecimal(dataRecord.getMutation());
            BigDecimal endBalance = new BigDecimal(dataRecord.getEndBalance());

            boolean isDuplicate = transactionReferences.contains(dataRecord.getReference());
            boolean isEndBalanceIncorrect = !endBalance.equals(balance.add(mutation));

            transactionReferences.add(dataRecord.getReference());

            if (isDuplicate || isEndBalanceIncorrect) {
                ValidationError validationError = new ValidationError();
                validationError.setDataRecord(dataRecord);
                if (isDuplicate) {
                    validationError.getInvalidTypes().add(InvalidType.DUPLICATE);
                }
                if (isEndBalanceIncorrect) {
                    validationError.getInvalidTypes().add(InvalidType.END_BALANCE_INCORRECT);
                }
                validationReport.getValidationErrors().add(validationError);
            }
        }
        return validationReport;
    }

    public static DataRecord[] processXML() {
        try {
            File file = new File("data/records.xml");
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(file, DataRecord[].class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new DataRecord[]{};
        }
    }

    public static DataRecord[] processCSV() {
        try {
            List<DataRecord> recordsList = new ArrayList<>();
            String csvFileName = "data/records.csv";
            Reader in = new FileReader(csvFileName, Charset.forName("Windows-1252"));
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
            Iterator<CSVRecord> iterator = records.iterator();
            iterator.next();//skipping the header
            while (iterator.hasNext()) {
                CSVRecord record = iterator.next();
                DataRecord rec = new DataRecord();
                rec.setReference(record.get(0));
                rec.setAccountNumber(record.get(1));
                rec.setDescription(record.get(2));
                rec.setStartBalance(record.get(3));
                rec.setMutation(record.get(4));
                rec.setEndBalance(record.get(5));
                recordsList.add(rec);
            }
            return recordsList.toArray(new DataRecord[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new DataRecord[]{};
        }
    }
}
