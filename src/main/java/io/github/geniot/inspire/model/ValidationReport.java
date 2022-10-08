package io.github.geniot.inspire.model;

import lombok.Data;

import java.util.*;

@Data
public class ValidationReport {
    private List<ValidationError> validationErrors = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ValidationError validationError : validationErrors) {
            stringBuilder.append(validationError.toString());
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
}
