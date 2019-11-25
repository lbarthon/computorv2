package fr.lbarthon.computorv2.exceptions;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class UnknownVariableException extends Exception {
    private Set<String> variableNames;

    public UnknownVariableException(String str) {
        this.variableNames = Collections.singleton(str);
    }

    public UnknownVariableException(Set<String> set) {
        this.variableNames = set;
    }

    @Override
    public String getMessage() {
        return "Cannot find a reference to " + this.variableNames.stream().map(s -> "'" + s + "'").collect(Collectors.joining(", "));
    }
}
