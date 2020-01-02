package fr.lbarthon.computorv2;

import fr.lbarthon.computorv2.ast.AST;
import fr.lbarthon.computorv2.ast.Node;
import fr.lbarthon.computorv2.ast.Token;
import fr.lbarthon.computorv2.computorv1.ComputorV1;
import fr.lbarthon.computorv2.exceptions.*;
import fr.lbarthon.computorv2.parser.Parser;
import fr.lbarthon.computorv2.variables.Function;
import fr.lbarthon.computorv2.variables.IVariable;
import lombok.Getter;

import java.util.*;

public class Computor {

    private List<String> oldStrings;
    private int historyIndex;
    private Map<String, IVariable> variables;
    /**
     * TempVariables are used by functions to store args value
     */
    @Getter
    private Map<String, IVariable> tempVariables;
    @Getter
    private Map<String, Function> functions;
    @Getter
    private Parser parser;
    @Getter
    private AST ast;

    public Computor() {
        this.variables = new HashMap<>();
        this.tempVariables = new HashMap<>();
        this.functions = new HashMap<>();
        this.oldStrings = new ArrayList<>();

        this.parser = new Parser(this);
    }

    public String handle(String str) {
        // Here we handle history commands & cli builtins
        if (str.startsWith("history@")) {
            return handleHistory(str.substring(8).equals("up"));
        }
        if (str.startsWith("builtin@")) {
            this.oldStrings.add(str.substring(8));
            return "";
        }

        this.oldStrings.add(str);
        this.historyIndex = this.oldStrings.size();

        if (str.equalsIgnoreCase("listvariables")) {
            if (this.variables.isEmpty()) return "I have no variables, try creating one!";
            // Returning a list of all variables joined by a newline
            StringBuilder builder = new StringBuilder();
            this.variables.forEach((k, v) -> {
                if (builder.length() != 0) {
                    builder.append('\n');
                }
                builder.append(k)
                        .append(" -> ")
                        .append(v.toString());
            });
            return builder.toString();
        }

        try {
            new Validator(str)
                    .brackets()
                    .brackets('[', ']');

            this.ast = new AST(this.parser, new Node(this));
            this.ast.createFrom(str);
            IVariable res = this.ast.solve();
            if (res == null) {
                Set<String> wrongVariables = this.ast.getWithStatus(false);
                if (this.ast.isFunctionAssignation()) {
                    return str;
                } else if (this.ast.getException() != null) {
                    throw this.ast.getException();
                // Handle computorv1
                } else if (this.ast.isAsk() && this.ast.getHead().getToken().equals(Token.EQUAL)) {
                    this.ast.replaceVariables();
                    return ComputorV1.solve(this.ast.toString());
                } else if (!wrongVariables.isEmpty()) {
                    throw new UnknownVariableException(wrongVariables);
                } else {
                    throw new Exception("Handle down there");
                }
            } else {
                return res.toString();
            }
        } catch (AssignationException | ParseException | ComplexFormatException | MatrixFormatException | ArithmeticException | UnknownFunctionException | UnknownVariableException e) {
            // Handling all my exceptions
            e.printStackTrace();
            return e.getClass().getSimpleName() + " - " + e.getMessage();
        } catch (Exception e) {
            // Handling unexpected exceptions
            if (this.ast.getException() != null) {
                Exception ex = this.ast.getException();
                e.printStackTrace();
                ex.printStackTrace();
                return ex.getClass().getSimpleName() + " - " + ex.getMessage();
            } else {
                // Handle computorv1
                if (this.ast.isAsk() && this.ast.getHead().getToken().equals(Token.EQUAL)) {
                    this.ast.replaceVariables();
                    return ComputorV1.solve(this.ast.toString());
                } else {
                    e.printStackTrace();
                    return "Sorry, an error occured... Is your input valid ?";
                }
            }
        }
    }

    private String handleHistory(boolean up) {
        if (up && this.historyIndex > 0) {
            historyIndex--;
        }
        if (!up && this.historyIndex < this.oldStrings.size()) {
            historyIndex++;
        }
        if (this.historyIndex != this.oldStrings.size()) {
            return this.oldStrings.get(this.historyIndex);
        }
        return "";
    }

    public IVariable getVariable(String str) {
        IVariable ret = this.tempVariables.containsKey(str) ? this.tempVariables.get(str) : this.variables.get(str);
        // Null check and then we clone the variable
        return ret == null ? null : ret.clone();
    }

    public void putVariable(String str, IVariable v) {
        this.variables.put(str, v);
    }
}
