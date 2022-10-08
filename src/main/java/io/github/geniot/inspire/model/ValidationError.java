package io.github.geniot.inspire.model;

import lombok.Data;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class ValidationError {
    private DataRecord dataRecord;
    private LinkedHashSet<InvalidType> invalidTypes = new LinkedHashSet<>();

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dataRecord.getReference());
        stringBuilder.append('\t');
        stringBuilder.append(dataRecord.getDescription());
        for (InvalidType invalidType : invalidTypes) {
            stringBuilder.append('\t');
            stringBuilder.append(invalidType);
        }
        return stringBuilder.toString();
    }
}
