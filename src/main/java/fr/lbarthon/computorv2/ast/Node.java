package fr.lbarthon.computorv2.ast;

import fr.lbarthon.computorv2.Computor;
import fr.lbarthon.computorv2.exceptions.AssignationException;
import fr.lbarthon.computorv2.exceptions.StopCalculationException;
import fr.lbarthon.computorv2.exceptions.UnknownVariableException;
import fr.lbarthon.computorv2.utils.StringUtils;
import fr.lbarthon.computorv2.variables.CallableFunction;
import fr.lbarthon.computorv2.variables.Function;
import fr.lbarthon.computorv2.variables.IVariable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public void replaceVariables() {
        if (this.token instanceof String) {
            String tokenStr = ((String) this.token).trim();
            if (tokenStr.isEmpty()) return;
            IVariable var = this.computor.getVariable(tokenStr);
            if (var != null) {
                this.token = var.clone();
            }
        } else {
            if (this.left != null) this.left.replaceVariables();
            if (this.right != null) this.right.replaceVariables();
        }
    }

    public IVariable solve() throws ArithmeticException, UnknownVariableException, StopCalculationException, AssignationException {
        if (this.token instanceof IVariable) {
            return (IVariable) this.token;
        }
        if (this.token instanceof AST) {
            return ((AST) this.token).solveNoCatch();
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
            // Handling function definition
            if (this.token == Token.EQUAL &&
                    ((this.left.token instanceof String && !StringUtils.isAlphabetic((String) this.left.token)) || this.left.token instanceof CallableFunction)) {
                // If we want to reassign a CallableFunction, we get it's data within the object name var
                String leftStr = this.left.token instanceof CallableFunction
                        ? ((CallableFunction) this.left.token).getName()
                        : (String) this.left.token;
                // Removing the f(x) unknown (avoid further error handling for no reason)
                this.computor.getAst().removeUnknown(leftStr);
                // We remove the parentheses around f(x)
                String variable = StringUtils.removeDepth(leftStr, 1);
                List<String> variables = null;
                // Validate unknowns
                if (variable != null && !variable.isEmpty()) {
                    variables = Arrays.stream(variable.split(",")).map(String::trim).collect(Collectors.toList());
                    variables.forEach(this.computor.getAst()::validateUnknown);
                }
                this.computor.getFunctions().put(
                        leftStr.substring(0, leftStr.indexOf('(')),
                        new Function(variables, new AST(this.computor.getParser(), this.right))
                );
                return null;
            }

            IVariable left, right;
            try {
                left = this.left.solve();
            } catch (StopCalculationException e) {
                throw new StopCalculationException();
            }
            try {
                right = this.right.solve();
            } catch (StopCalculationException e) {
                throw new StopCalculationException();
            }

            if (this.token == Token.EQUAL) {
                if (!(this.left.token instanceof String)) {
                    return null;
                    // TODO: Find a way to fix this
                    // throw new AssignationException(this.left.token.toString() + " isn't a valid variable name");
                }
                String leftStr = (String) this.left.token;

                if (StringUtils.isAlphabetic(leftStr)) {
                    if (right == null) {
                        throw new StopCalculationException();
                    }
                    this.computor.putVariable(leftStr, right.clone());
                    return right;
                }
            }

            switch ((Token) this.token) {
                case POW:
                    return left.pow(right);
                case MULT:
                    return left.mult(right);
                case DBL_MULT:
                    return left.dblmult(right);
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

    @Override
    public String toString() {
        if (this.token instanceof Token) {
            return ' ' + this.left.toString() + ' ' + ((Token) this.token).getToken() + ' ' + this.right.toString();
        }
        if (this.token instanceof AST) {
            return " (" + this.token.toString() + ')';
        }
        return ' ' + this.token.toString();
    }
}
