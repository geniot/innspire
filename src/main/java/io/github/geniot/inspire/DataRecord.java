package io.github.geniot.inspire;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataRecord {
    @JacksonXmlProperty(isAttribute = true)
    private String reference;
    private String startBalance;
    private String mutation;
    private String endBalance;
    private String accountNumber;
    private String description;
}
