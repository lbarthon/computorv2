package fr.lbarthon.computorv2.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Token {
    EQUAL('=', 2),
    PLUS('+', 1),
    LESS('-', 1),
    MULT('*', 0),
    DIV('/', 0),
    MOD('%', 0),
    POW('^', 0);

    char token;
    int prio;

    public static int getMaxPrio() {
        return Arrays.stream(values()).max(Comparator.comparingInt(Token::getPrio)).get().getPrio();
    }

    public static List<Token> getPrioValues(int prio) {
        return Arrays.stream(values()).filter(t -> t.prio == prio).collect(Collectors.toList());
    }

    public static Token fromChar(char c) {
        return Arrays.stream(values()).filter(t -> t.getToken() == c).findFirst().orElse(null);
    }
}
