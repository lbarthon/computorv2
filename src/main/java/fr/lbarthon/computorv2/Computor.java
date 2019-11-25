package fr.lbarthon.computorv2;

import fr.lbarthon.computorv2.ast.AST;
import fr.lbarthon.computorv2.ast.Node;
import fr.lbarthon.computorv2.exceptions.ComplexFormatException;
import fr.lbarthon.computorv2.exceptions.ParseException;
import fr.lbarthon.computorv2.exceptions.UnknownVariableException;
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

        this.oldStrings.add(str);
        this.historyIndex = this.oldStrings.size();

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
                    return "Function created successfully!";
                } else if (!wrongVariables.isEmpty()) {
                    throw new UnknownVariableException(wrongVariables);
                } else if (this.ast.getException() != null) {
                    throw this.ast.getException();
                } else {
                    return "Sorry, an error occured...";
                }
            } else {
                return res.toString();
            }
        } catch (ParseException | ComplexFormatException | ArithmeticException | UnknownVariableException e) {
            e.printStackTrace();
            return e.getClass().getSimpleName() + " - " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            if (this.ast.getException() != null) {
                Exception ex = this.ast.getException();
                ex.printStackTrace();
                return ex.getClass().getSimpleName() + " - " + ex.getMessage();
            } else {
                e.printStackTrace();
                return "Sorry, an error occured... Is your input valid ?";
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
        if (this.tempVariables.containsKey(str)) {
            return this.tempVariables.get(str);
        }
        return this.variables.get(str);
    }

    public void putVariable(String str, IVariable v) {
        this.variables.put(str, v);
    }
}
