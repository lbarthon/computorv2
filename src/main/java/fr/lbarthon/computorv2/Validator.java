package fr.lbarthon.computorv2;

import fr.lbarthon.computorv2.exceptions.MatrixFormatException;
import fr.lbarthon.computorv2.exceptions.ParseException;
import fr.lbarthon.computorv2.utils.StringUtils;
import fr.lbarthon.computorv2.variables.Matrix;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Validator {

    private final String str;
    private char[] charArray;

    public Validator(String str) {
        this.str = str.trim();
    }

    private char[] getAsCharArray() {
        if (this.charArray == null) {
            this.charArray = this.str.toCharArray();
        }
        return this.charArray;
    }

    public Validator brackets() throws ParseException {
        return this.brackets(StringUtils.DEPTH_START, StringUtils.DEPTH_END);
    }

    public Validator brackets(char start, char end) throws ParseException {
        int depth = 0;
        for (char c : this.getAsCharArray()) {
            if (c == start) depth++;
            if (c == end) depth--;
            if (depth < 0) break;
        }

        int finalDepth = depth;
        if (finalDepth != 0) {
            List<Integer> indexes = StringUtils.indexesOf(this.str, finalDepth > 0 ? start : end).stream()
                    .filter(index -> finalDepth < 0 || !this.isClosed(start, end, index))
                    .sorted(Comparator.comparingInt(n -> n))
                    .collect(Collectors.toList());
            if (finalDepth < 0) {
                indexes.sort(Comparator.reverseOrder());
            }
            this.parseError(indexes.get(0));
        }
        return this;
    }

    private boolean isClosed(char start, char end, int index) {
        if (index == str.length()) return false;

        int depth = 1;
        for (char c : this.str.substring(index + 1).toCharArray()) {
            if (c == start) depth++;
            if (c == end) depth --;
            if (depth == 0) return true;
        }
        return false;
    }

    public Matrix matrix() throws ParseException, MatrixFormatException {
        return Matrix.valueOf(this.str);
    }

    private void parseError(int index) throws ParseException {
        throw new ParseException(this.str, index);
    }
}
