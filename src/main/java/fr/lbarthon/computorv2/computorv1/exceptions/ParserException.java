package fr.lbarthon.computorv2.computorv1.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public class ParserException extends Exception {
    private static final int wantedChars = 10;

    final String parsedString;
    final int offset;

    public void displayProblematicPart() {
        int start = this.offset - 5;
        if (start < 0) {
            start = 0;
        }
        int end = start + wantedChars;
        if (end > this.parsedString.length()) {
            end = this.parsedString.length();
        }
        System.out.println("Error parsing the input !");
        System.out.println(this.parsedString.substring(start, end));
        System.out.println(getSpaces(this.offset - start) + '^');
    }

    private String getSpaces(int nbr) {
        char[] spaces = new char[nbr];
        Arrays.fill(spaces, ' ');
        return new String(spaces);
    }
}
