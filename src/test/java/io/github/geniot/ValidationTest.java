package io.github.geniot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.geniot.inspire.model.DataRecord;
import io.github.geniot.inspire.Main;
import io.github.geniot.inspire.model.InvalidType;
import io.github.geniot.inspire.model.ValidationError;
import io.github.geniot.inspire.model.ValidationReport;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class ValidationTest {

    @Test
    public void testValidationSingle() {
        try {

            DataRecord dataRecord = buildDataRecord("1234", "desc1", "0", "0", "0");
            Assertions.assertEquals(new ValidationReport().toString(), Main.validate(new DataRecord[]{dataRecord}).toString());

            dataRecord = buildDataRecord("1234", "desc1", "0", "1", "0");
            ValidationReport expectedReport = new ValidationReport();
            expectedReport.getValidationErrors().add(buildValidationError(dataRecord, new LinkedHashSet<>(List.of(InvalidType.END_BALANCE_INCORRECT))));
            Assertions.assertEquals(expectedReport.toString(), Main.validate(new DataRecord[]{dataRecord}).toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testValidationMultiple() {
        try {
            DataRecord dataRecord1 = buildDataRecord("1234", "desc1", "0", "0", "0");
            DataRecord dataRecord2 = buildDataRecord("1235", "desc2", "0", "0", "0");
            Assertions.assertEquals(new ValidationReport().toString(), Main.validate(new DataRecord[]{dataRecord1, dataRecord2}).toString());

            dataRecord1 = buildDataRecord("1234", "desc1", "0", "0", "0");
            dataRecord2 = buildDataRecord("1234", "desc2", "0", "0", "0");
            ValidationReport expectedReport = new ValidationReport();
            expectedReport.getValidationErrors().add(buildValidationError(dataRecord2, new LinkedHashSet<>(List.of(InvalidType.DUPLICATE))));
            Assertions.assertEquals(expectedReport.toString(), Main.validate(new DataRecord[]{dataRecord1, dataRecord2}).toString());

            dataRecord1 = buildDataRecord("1234", "desc1", "0", "1", "0");
            dataRecord2 = buildDataRecord("1235", "desc2", "0", "0", "0");
            expectedReport = new ValidationReport();
            expectedReport.getValidationErrors().add(buildValidationError(dataRecord1, new LinkedHashSet<>(List.of(InvalidType.END_BALANCE_INCORRECT))));
            Assertions.assertEquals(expectedReport.toString(), Main.validate(new DataRecord[]{dataRecord1, dataRecord2}).toString());


            dataRecord1 = buildDataRecord("1234", "desc1", "0", "1", "0");
            dataRecord2 = buildDataRecord("1234", "desc2", "0", "0", "0");
            expectedReport = new ValidationReport();
            expectedReport.getValidationErrors().add(buildValidationError(dataRecord1, new LinkedHashSet<>(List.of(InvalidType.END_BALANCE_INCORRECT))));
            expectedReport.getValidationErrors().add(buildValidationError(dataRecord2, new LinkedHashSet<>(List.of(InvalidType.DUPLICATE))));
            Assertions.assertEquals(expectedReport.toString(), Main.validate(new DataRecord[]{dataRecord1, dataRecord2}).toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DataRecord buildDataRecord(String reference, String description, String startBalance, String mutation, String endBalance) {
        DataRecord dataRecord = new DataRecord();
        dataRecord.setReference(Long.parseLong(reference));
        dataRecord.setDescription(description);
        dataRecord.setStartBalance(new BigDecimal(startBalance));
        dataRecord.setMutation(new BigDecimal(mutation));
        dataRecord.setEndBalance(new BigDecimal(endBalance));
        return dataRecord;
    }

    private ValidationError buildValidationError(DataRecord dataRecord, LinkedHashSet<InvalidType> invalidTypes) {
        ValidationError validationError = new ValidationError();
        validationError.setDataRecord(dataRecord);
        validationError.setInvalidTypes(invalidTypes);
        return validationError;
    }
}
