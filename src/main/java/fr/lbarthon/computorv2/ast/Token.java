package fr.lbarthon.computorv2.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Token {
    PLUS('+', true),
    LESS('-', true),
    MULT('*', false),
    DIV('/', false),
    MOD('%', false),
    POW('^', false);

    char token;
    boolean prio;

    public static List<Token> getPrioValues() {
        return Arrays.stream(values()).filter(Token::isPrio).collect(Collectors.toList());
    }

    public static List<Token> getNonPrioValues() {
        return Arrays.stream(values()).filter(t -> !t.isPrio()).collect(Collectors.toList());
    }
}
