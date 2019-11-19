package fr.lbarthon.computorv2.utils;

import fr.lbarthon.computorv2.exceptions.ParseException;

public class StringUtils {
    public static final char DEPTH_START = '(';
    public static final char DEPTH_END = ')';
    private StringUtils() {
    }

    public static int getDepthCheck(String str, int index) throws ParseException {
        int depth = getDepth(str, index);
        int revDepth = getRevDepth(str, index);
        if (depth == revDepth) return depth;
        // Handle indexes in a better way
        throw new ParseException(str, 0);
    }

    public static int getDepth(String str, int index) {
        int depth = 0;
        for (int i = 0; i < str.length(); i++) {
            if (i == index) {
                return depth;
            }
            char c = str.charAt(i);
            if (DEPTH_START == c) {
                depth++;
            }
            if (DEPTH_END == c) {
                depth--;
            }
        }
        return -1;
    }

    public static int getRevDepth(String str, int index) {
        int depth = 0;
        for (int i = str.length() - 1; i >= 0; i--) {
            if (i == index) {
                return depth;
            }
            char c = str.charAt(i);
            if (DEPTH_START == c) {
                depth--;
            }
            if (DEPTH_END == c) {
                depth++;
            }
        }
        return -1;
    }

    public static String removeDepth(String str, int depth) {
        int index = str.indexOf(DEPTH_START);
        if (index == -1) return null;
        String ret = str.substring(index + 1);
        char[] chars = ret.toCharArray();
        for (int i = ret.length() - 1; i > 0; i--) {
            if (chars[i] == DEPTH_END) {
                depth--;
                ret = ret.substring(0, i);
                return depth == 0 ? ret : removeDepth(str, depth);
            }
        }
        return null;
    }

    public static int lastIndexOf(String str, char c) {
        if (str.indexOf(c) == -1) return -1;
        char[] chars = str.toCharArray();

        for (int i = str.length() - 1; i >= 0; i--) {
            if (chars[i] == c) {
                return i;
            }
        }

        return -1;
    }

    public static boolean isAlphabetic(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isAlphabetic(c)) {
                return false;
            }
        }
        return true;
    }
}
