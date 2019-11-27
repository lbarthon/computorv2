package fr.lbarthon.computorv2.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnknownFunctionException extends Exception {
    private final String functionName;

    @Override
    public String getMessage() {
        return "Cannot find the function '" + this.functionName + "'";
    }
}
