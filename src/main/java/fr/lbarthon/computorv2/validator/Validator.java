package fr.lbarthon.computorv2.validator;

import fr.lbarthon.computorv2.exceptions.ParseException;
import fr.lbarthon.computorv2.utils.StringUtils;
import lombok.RequiredArgsConstructor;

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
            throw new ParseException(this.str, indexes.get(0));
        }
        return this;
    }

    public Validator matrice() throws ParseException {
        int rowLength = -1;
        for (char c : this.getAsCharArray()) {

        }
        return this;
    }
}
