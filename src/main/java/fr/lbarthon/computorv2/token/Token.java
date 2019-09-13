package fr.lbarthon.computorv2.token;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {
    private String str;
    private Type type;

    public enum Type {
        UNKNOWN,
        NUMBER,
        COMPLEX,
        OPERATION,
        PARENTHESIS,
        VARIABLE,
        FUNCTION_START,
        FUNCTION_END,
        ASSIGNATION
    }

    public String toString() {
        return "Token type: " + this.type.toString() + "\nContent: " + this.str + " (length " + this.str.length() + ")";
    }
}
