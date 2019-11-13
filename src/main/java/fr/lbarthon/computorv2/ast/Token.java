package fr.lbarthon.computorv2.ast;

import fr.lbarthon.computorv2.exceptions.NodeCalculationException;
import fr.lbarthon.computorv2.variables.IVariable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Token {
    PLUS('+', false),
    LESS('-', false),
    MULT('*', true),
    DIV('/', true),
    MOD('%', true),
    POW('^', true);

    char token;
    boolean prio;

    public static List<Token> getPrioValues() {
        return Arrays.stream(values()).filter(Token::isPrio).collect(Collectors.toList());
    }

    public static List<Token> getNonPrioValues() {
        return Arrays.stream(values()).filter(t -> !t.isPrio()).collect(Collectors.toList());
    }

    public static IVariable apply(IVariable left, IVariable right, Token token) throws NodeCalculationException {
        // TODO
        switch (token) {
            case PLUS:
                break;
            case LESS:
                break;
            case MULT:
                break;
            case DIV:
                break;
            case MOD:
                break;
            case POW:
                break;
            default:
                throw new NodeCalculationException(null);
        }
        return left;
    }
}
