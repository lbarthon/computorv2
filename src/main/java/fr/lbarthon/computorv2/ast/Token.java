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
    EQUAL('=', 3),
    PLUS('+', 2),
    LESS('-', 2),
    MULT('*', 1),
    DIV('/', 1),
    MOD('%', 1),
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
