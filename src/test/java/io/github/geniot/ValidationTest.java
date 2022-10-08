package io.github.geniot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.geniot.inspire.DataRecord;
import io.github.geniot.inspire.Main;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class ValidationTest {
    @Test
    public void testValidation() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            DataRecord[] dataRecords = objectMapper.readValue(
                    "[{\"reference\":\"1234\",\"startBalance\":\"0\",\"mutation\":\"0\",\"endBalance\":\"0\"}]"
                    , DataRecord[].class);
            Assertions.assertEquals("", Main.validate(dataRecords));

            dataRecords = objectMapper.readValue(
                    "[{\"reference\":\"1234\",\"startBalance\":\"0\",\"mutation\":\"0\",\"endBalance\":\"0\"}," +
                            "{\"reference\":\"1235\",\"startBalance\":\"0\",\"mutation\":\"0\",\"endBalance\":\"0\"}]"
                    , DataRecord[].class);
            Assertions.assertEquals("", Main.validate(dataRecords));

            dataRecords = objectMapper.readValue(
                    "[{\"reference\":\"1234\",\"startBalance\":\"0\",\"mutation\":\"0\",\"endBalance\":\"0\"}," +
                            "{\"reference\":\"1234\",\"startBalance\":\"0\",\"mutation\":\"0\",\"endBalance\":\"0\",\"description\":\"desc\"}]"
                    , DataRecord[].class);
            Assertions.assertEquals("1234\tdesc\t- duplicate" + System.lineSeparator(), Main.validate(dataRecords));

            dataRecords = objectMapper.readValue(
                    "[{\"reference\":\"1234\",\"startBalance\":\"0\",\"mutation\":\"1\",\"endBalance\":\"0\",\"description\":\"desc\"}]"
                    , DataRecord[].class);
            Assertions.assertEquals("1234\tdesc\t- end balance incorrect" + System.lineSeparator(), Main.validate(dataRecords));

            dataRecords = objectMapper.readValue(
                    "[{\"reference\":\"1234\",\"startBalance\":\"0\",\"mutation\":\"1\",\"endBalance\":\"0\",\"description\":\"desc1\"}," +
                            "{\"reference\":\"1234\",\"startBalance\":\"0\",\"mutation\":\"0\",\"endBalance\":\"0\",\"description\":\"desc2\"}]"
                    , DataRecord[].class);
            Assertions.assertEquals(
                    "1234\tdesc1\t- end balance incorrect" + System.lineSeparator() +
                            "1234\tdesc2\t- duplicate" + System.lineSeparator()
                    , Main.validate(dataRecords));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
