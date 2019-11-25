package fr.lbarthon.computorv2.ast;

import fr.lbarthon.computorv2.Computor;
import fr.lbarthon.computorv2.exceptions.ParseException;
import fr.lbarthon.computorv2.exceptions.UnknownVariableException;
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

    public IVariable solve() throws ArithmeticException, UnknownVariableException {
        if (this.token instanceof IVariable) {
            return (IVariable) this.token;
        }
        if (this.token instanceof AST) {
            return ((AST) this.token).solve();
        }
        if (this.token instanceof String) {
            String tokenStr = ((String) this.token).trim();
            if (tokenStr.isEmpty()) return null;
            IVariable var = this.computor.getVariable(tokenStr);
            if (var == null) {
                this.computor.getAst().addUnknown(tokenStr);
            }
            return var;
        }

        if (this.token instanceof Token) {
            IVariable left = this.left.solve();
            if (this.token == Token.EQUAL
                    && this.left.token instanceof String
                    && !StringUtils.isAlphabetic((String) this.left.token)) {
                String leftStr = (String) this.left.token;
                // Removing the f(x) unknown (avoid further error handling for no reason)
                this.computor.getAst().removeUnknown(leftStr);
                // We remove the parentheses around f(x)
                leftStr = StringUtils.removeDepth(leftStr, 1);
                // Validate x unknown
                this.computor.getAst().validateUnknown(leftStr);
            }

            IVariable right = this.right.solve();

            if (this.token == Token.EQUAL) {
                if (!(this.left.token instanceof String)) {
                    // TODO: Handle error
                    return null;
                }
                String leftStr = (String) this.left.token;

                if (StringUtils.isAlphabetic(leftStr)) {
                    this.computor.putVariable(leftStr, right.clone());
                    return right;
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
                    return left == null ? right.negate() : left.sub(right);
            }
        }

        return null;
    }
}
