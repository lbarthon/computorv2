package fr.lbarthon.computorv2;

import fr.lbarthon.computorv2.ast.AST;
import fr.lbarthon.computorv2.ast.Node;
import fr.lbarthon.computorv2.ast.Token;
import fr.lbarthon.computorv2.exceptions.ComplexFormatException;
import fr.lbarthon.computorv2.exceptions.ParseException;
import fr.lbarthon.computorv2.parser.Parser;
import fr.lbarthon.computorv2.validator.Validator;
import fr.lbarthon.computorv2.variables.Complex;
import fr.lbarthon.computorv2.variables.IVariable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Computor {

    private List<String> oldStrings;
    private int historyIndex;
    private Map<String, IVariable> variables;
    @Getter
    private Parser parser;
    @Getter
    private AST ast;

    public Computor() {
        this.variables = new HashMap<>();
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

        try {
            new Validator(str)
                    .brackets();

            this.ast = new AST(this.parser, new Node(this));
            this.ast.createFrom(str);
            Complex res = this.ast.solve();
            if (res == null) {
                if (this.ast.isEquation()) {
                    return "Handle equations - Todo";
                } else {
                    return "Sorry, an error occured...";
                }
            } else {
                return res.toString();
            }
        } catch (ParseException | ComplexFormatException | ArithmeticException e) {
            return e.getClass().getSimpleName() + " - " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, an error occured... Is your input valid ?";
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
        return this.variables.get(str);
    }

    public void putVariable(String str, IVariable v) {
        this.variables.put(str, v);
    }
}
