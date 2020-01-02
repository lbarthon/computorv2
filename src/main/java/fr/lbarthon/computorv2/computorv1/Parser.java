package fr.lbarthon.computorv2.computorv1;

import fr.lbarthon.computorv2.computorv1.exceptions.InvalidPowerException;
import fr.lbarthon.computorv2.computorv1.exceptions.ParserException;

import java.util.Arrays;

public class Parser {
    private Parser() {}

    public static Equation parse(String str) throws
            ParserException,
            InvalidPowerException,
            ArrayIndexOutOfBoundsException
    {
        Equation equation = new Equation();

        String[] parts = str.split("=");

        if (parts.length != 2) {
            throw new ParserException(str, str.length());
        }

        System.out.println(str);
        parsePart(equation.getLeftPart(), parts[0]);
        parsePart(equation.getRightPart(), parts[1]);

        return equation;
    }

    private static void parsePart(Equation.Part part, String str) throws
            ParserException,
            InvalidPowerException,
            ArrayIndexOutOfBoundsException
    {
        // TODO
    }

    /**
     * Equivalent of c atoi function.
     * This function does not handle the signs (+ & -)
     * @param chars
     * @return
     */
    private static double atoi(char[] chars) {
        double ret = 0;
        int i = 0;

        while (Character.isSpaceChar(chars[i])) {
            i++;
        }

        for (; i < chars.length; i++) {
            if (!Character.isDigit(chars[i])) break;
            ret *= 10;
            ret += Character.getNumericValue(chars[i]);
        }

        if (i < chars.length && (chars[i] == '.' || chars[i] == ',')) {
            i++;

            int nbrsAfterDot = 0;
            for (; i < chars.length; i++) {
                if (!Character.isDigit(chars[i])) break;
                nbrsAfterDot++;
                ret += Character.getNumericValue(chars[i]) / power(10, nbrsAfterDot);
            }
        }

        return ret;
    }

    private static double power(double nbr, int power) {
        if (power <= 0) return 1;
        return nbr * power(nbr, power - 1);
    }
}
