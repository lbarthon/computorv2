package fr.lbarthon.computorv2.ast;

import fr.lbarthon.computorv2.Computor;
import fr.lbarthon.computorv2.exceptions.StopCalculationException;
import fr.lbarthon.computorv2.exceptions.UnknownVariableException;
import fr.lbarthon.computorv2.utils.StringUtils;
import fr.lbarthon.computorv2.variables.CallableFunction;
import fr.lbarthon.computorv2.variables.Function;
import fr.lbarthon.computorv2.variables.IVariable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collections;

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

    public Node clone() {
        Node clone = new Node(this.computor, this.temp);
        clone.left = this.left == null ? null : this.left.clone();
        clone.right = this.right == null ? null : this.right.clone();
        if (this.token instanceof Token) {
            clone.token = this.token;
        } else if (this.token instanceof IVariable) {
            clone.token = ((IVariable) this.token).clone();
        } else if (this.token instanceof String) {
            clone.token = String.copyValueOf(((String) this.token).toCharArray());
        } else if (this.token instanceof AST) {
            clone.token = ((AST) this.token).clone();
        } else if (this.token instanceof Function) {
            clone.token = ((Function) this.token).clone();
        } else {
            clone.token = null;
        }
        return clone;
    }

    public IVariable solve() throws ArithmeticException, UnknownVariableException, StopCalculationException {
        if (this.token instanceof IVariable) {
            return (IVariable) this.token;
        }
        if (this.token instanceof AST) {
            return ((AST) this.token).solve();
        }
        if (this.token instanceof CallableFunction) {
            return ((CallableFunction) this.token).solve(this.computor);
        }
        if (this.token instanceof String) {
            String tokenStr = ((String) this.token).trim();
            if (tokenStr.isEmpty()) return null;
            if (this.computor.getAst().getUnknownStatus(tokenStr)) {
                throw new StopCalculationException();
            }
            IVariable var = this.computor.getVariable(tokenStr);
            if (var == null) {
                this.computor.getAst().addUnknown(tokenStr);
            }
            return var;
        }

        if (this.token instanceof Token) {
            IVariable left = null, right = null;
            boolean needStop = false;
            try {
                left = this.left.solve();
            } catch (StopCalculationException e) {
                needStop = true;
            }

            // Handling function definition
            if (this.token == Token.EQUAL
                    && this.left.token instanceof String
                    && !StringUtils.isAlphabetic((String) this.left.token)) {

                String leftStr = (String) this.left.token;
                // Removing the f(x) unknown (avoid further error handling for no reason)
                this.computor.getAst().removeUnknown(leftStr);
                // We remove the parentheses around f(x)
                String variable = StringUtils.removeDepth(leftStr, 1);
                // Validate x unknown
                if (variable != null && !variable.isEmpty()) {
                    this.computor.getAst().validateUnknown(variable);
                }
                this.computor.getFunctions().put(
                        leftStr.substring(0, leftStr.indexOf('(')),
                        new Function(Collections.singletonList(variable), new AST(this.computor.getParser(), this.right))
                );
            }

            try {
                right = this.right.solve();
            } catch (StopCalculationException e) {
                needStop = true;
            }

            if (needStop) {
                throw new StopCalculationException();
            }

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
