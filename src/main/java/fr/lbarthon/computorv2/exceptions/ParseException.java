package fr.lbarthon.computorv2.exceptions;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class ParseException extends Exception {
    private static final int wantedChars = 10;

    private final String parsedString;
    private final int offset;

    public ParseException(String parsedString, int offset) {
        this.parsedString = parsedString;
        this.offset = offset;
    }

    @Override
    public String getMessage() {
        int start = this.offset - wantedChars / 2;
        if (start < 0) {
            start = 0;
        }
        int end = start + wantedChars;
        if (end > this.parsedString.length()) {
            end = this.parsedString.length();
        }

        return new StringBuilder()
                .append("Error parsing the input\n")
                .append(this.parsedString, start, end)
                .append('\n')
                .append(getSpaces(this.offset - start))
                .append('^')
                .toString();
    }

    private String getSpaces(int nbr) {
        if (nbr < 0) return "";
        char[] spaces = new char[nbr];
        Arrays.fill(spaces, ' ');
        return new String(spaces);
    }
}
