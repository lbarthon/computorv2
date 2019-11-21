package fr.lbarthon.computorv2.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MatrixFormatException extends NumberFormatException {
    private int line;
    private int numbers;
    private int wanted;

    @Override
    public String getMessage() {
        return "The line " + line + " of the matrix should have " + wanted + " numbers instead of " + numbers;
    }
}
