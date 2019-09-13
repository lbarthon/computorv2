package fr.lbarthon.computorv2.token;

import fr.lbarthon.computorv2.variables.Complex;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tokenizer {

    private static final char COMPLEX_CHAR = 'i';
    private static final Set<Character> DOT_CHARS = new HashSet<>();
    private static final Set<Character> OPERATORS = new HashSet<>();

    static {
        DOT_CHARS.add('.');
        DOT_CHARS.add(',');

        OPERATORS.add('+');
        OPERATORS.add('-');
        OPERATORS.add('*');
        OPERATORS.add('/');
        OPERATORS.add('%');
    }

    @Getter
    private final List<Token> tokens = new ArrayList<>();
    private String str;
    @Getter
    private boolean ask;

    public Tokenizer(String str) {
        this.str = str;
        this.ask = false;

        this.checkAsk();
        this.parse();
    }

    private void checkAsk() {
        if (this.str.matches("=\\s*?\\s*$")) {
            System.out.println("Ask");
            this.ask = true;
            for (int i = str.length(); i > 0; i--) {
                if (str.charAt(i) == '=') {
                    this.str = this.str.substring(0, i);
                    return;
                }
            }
            System.out.println("Error pas trouvé wtf bro");
        } else {
            System.out.println("Pas ask");
        }
    }

    private void parse() {
        Complex emptyComplex = new Complex(null);
        int functionsToEnd = 0;
        int parenthesisInFunctions = 0;
        int startIndex;
        boolean isNumber;

        char[] chars = this.str.toCharArray();

        for (int i = 0; i < chars.length; ) {
            while (Character.isSpaceChar(chars[i])) i++;
            isNumber = false;
            startIndex = i;

            while (i < chars.length && (Character.isDigit(chars[i]) || this.isDotChar(chars[i]))) {
                isNumber = true;
                i++;
            }
            // Handling strings ending with a number
            if (i == chars.length) i--;

            if (chars[i] == COMPLEX_CHAR) {
                this.newToken(startIndex, i, Token.Type.COMPLEX);
            } else if (isNumber) {
                this.newToken(startIndex, i, Token.Type.NUMBER);
            } else if (chars[i] == '=') {
                this.newToken(startIndex, i, Token.Type.ASSIGNATION);
            } else if (this.isOperator(chars[i])) {
                this.newToken(startIndex, i, Token.Type.OPERATION);
            } else if (this.isParenthesis(chars[i])) {
                if (chars[i] == '(' && functionsToEnd > 0) parenthesisInFunctions++;
                if (chars[i] == ')' && functionsToEnd > 0) {
                    if (parenthesisInFunctions == 0) {
                        this.newToken(startIndex, i, Token.Type.FUNCTION_END);
                    } else {
                        parenthesisInFunctions--;
                        this.newToken(startIndex, i, Token.Type.PARENTHESIS);
                    }
                } else {
                    this.newToken(startIndex, i, Token.Type.PARENTHESIS);
                }
            } else {
                boolean entered = false;
                while (i < chars.length && !this.isOperator(chars[i]) && !this.isParenthesis(chars[i]) && !Character.isSpaceChar(chars[i])) {
                    entered = true;
                    i++;
                }

                // Handling strings ending with a string (variable name) with 1st condition
                if (i != chars.length && entered && this.isParenthesis(chars[i])) {
                    functionsToEnd++;

                    this.newToken(startIndex, ++i, Token.Type.FUNCTION_START);
                } else if (entered) {
                    this.newToken(startIndex, i, Token.Type.VARIABLE);
                } else {
                    this.newToken(startIndex, i, Token.Type.UNKNOWN);
                }
            }

            if (i == startIndex) {
                i++;
            }
        }
    }

    private void newToken(int start, int end, Token.Type type) {
        if (start == end) end++;

        this.tokens.add(new Token(this.str.substring(start, end), type));
    }

    public boolean isDotChar(Character c) {
        return DOT_CHARS.contains(c);
    }

    public boolean isOperator(Character c) {
        return OPERATORS.contains(c);
    }

    public boolean isParenthesis(Character c) {
        return c == '(' || c == ')';
    }
}
