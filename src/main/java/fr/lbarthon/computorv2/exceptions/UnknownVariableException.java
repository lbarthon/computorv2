package fr.lbarthon.computorv2.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnknownVariableException extends Exception {
    private String variableName;

    @Override
    public String getMessage() {
        return "Cannot find a reference to '" + variableName + "'";
    }
}
