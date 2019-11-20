package fr.lbarthon.computorv2.validator;

import fr.lbarthon.computorv2.exceptions.ParseException;
import fr.lbarthon.computorv2.utils.StringUtils;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class Validator {

    private final String str;
    private char[] charArray;

    private char[] getAsCharArray() {
        if (this.charArray == null) {
            this.charArray = this.str.toCharArray();
        }
        return this.charArray;
    }

    public Validator brackets() throws ParseException {
        char start = StringUtils.DEPTH_START, end = StringUtils.DEPTH_END;
        int depth = 0;
        for (char c : this.getAsCharArray()) {
            if (c == start) depth++;
            if (c == end) depth--;
        }

        if (depth != 0) {
            List<Integer> indexes = StringUtils.indexesOf(this.str, depth > 0 ? start : end);
            indexes.sort(Comparator.comparingInt(n -> n));
            if (depth < 0) {
                indexes.sort(Comparator.reverseOrder());
            }
            this.parseError(indexes.get(0));
        }
        return this;
    }

    public Validator matrice() throws ParseException {
        char start = '[', end = ']';
        char[] arr = this.getAsCharArray();
        if (arr[0] != start) this.parseError(0);
        if (arr[arr.length - 1] != end) this.parseError(arr.length - 1);
        if (this.str.length() < 2) this.parseError(0);

        // Looping on every row to check if all sizes are alike
        List<Integer> startIndexes = StringUtils.indexesOf(this.str.substring(1), start);
        List<Integer> endIndexes = StringUtils.indexesOf(this.str.substring(0, this.str.length() - 1), end);
        List<Integer> sizes = new ArrayList<>();


        while (!startIndexes.isEmpty() && !endIndexes.isEmpty()) {
            int starti = startIndexes.remove(0);
            int endi = endIndexes.remove(0);
            String row = this.str.substring(starti, endi);
            // TODO
        }

        return this;
    }

    private void parseError(int index) throws ParseException {
        throw new ParseException(this.str, index);
    }
}
