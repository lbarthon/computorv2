package fr.lbarthon.computorv2.variables;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VariableType {
    NUMBER(Complex.class),
    MATRICE(null),
    FUNCTION(null),
    NONE(null);

    Class clazz;

    public static VariableType getTypeOfString(String str) {
        if (str.contains("[") || str.contains("]")) {
            return MATRICE;
        }

        if (str.matches("(.).*=")) {
            return FUNCTION;
        }

        return NUMBER;
    }
}
