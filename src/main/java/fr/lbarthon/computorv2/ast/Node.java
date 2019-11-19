package fr.lbarthon.computorv2.ast;

import fr.lbarthon.computorv2.Computor;
import fr.lbarthon.computorv2.utils.StringUtils;
import fr.lbarthon.computorv2.variables.Complex;
import fr.lbarthon.computorv2.variables.IVariable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Node {
    final Computor computor;
    String temp;
    Object token;
    Node left;
    Node right;

    public Node(Computor computor, String temp) {
        this.computor = computor;
        this.temp = temp;
    }

    public String getTempAndClear() {
        String temp = this.temp;
        this.temp = null;
        return temp;
    }

    public Complex solve() {
        if (this.token instanceof Complex) {
            return (Complex) this.token;
        }
        if (this.token instanceof AST) {
            return ((AST) this.token).solve();
        }
        if (this.token instanceof String) {
            IVariable var = this.computor.getVariable((String) this.token);
            if (var == null) return null;
            if (var instanceof Complex) {
                return (Complex) var.clone();
            } else {
                // TODO: Handle others variable types
            }
        }

        if (this.token instanceof Token) {
            Complex left = this.left.solve();
            Complex right = this.right.solve();

            if (this.token == Token.EQUAL) {
                if (this.left.token instanceof String && StringUtils.isAlphabetic((String) this.left.token)) {
                    this.computor.putVariable((String) this.left.token, right.clone());
                    return right;
                } else {
                    this.computor.getAst().setEquation(true);
                }
            }

            switch ((Token) this.token) {
                case POW:
                    return left.pow(right);
                case MULT:
                    return left.mult(right);
                case DIV:
                    return left.div(right);
                case MOD:
                    return left.modulo(right);
                case PLUS:
                    return left.add(right);
                case LESS:
                    return left.sub(right);
            }
        }

        return null;
    }

    @Override
    public String toString() {
        if (this.token instanceof Token) {

        }
        return this.token.toString();
    }
}
