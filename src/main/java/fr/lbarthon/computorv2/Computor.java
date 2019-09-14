package fr.lbarthon.computorv2;

import fr.lbarthon.computorv2.parser.Parser;
import fr.lbarthon.computorv2.variables.Variable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Computor {

    private Map<String, Variable> variables;
    private List<String> oldStrings;
    @Getter
    private Parser parser;

    public Computor() {
        this.variables = new HashMap<>();
        this.oldStrings = new ArrayList<>();

        this.parser = new Parser(this);
    }

    public void handle(String str) {
        if (str == null || "exit".equals(str) || "quit".equals(str)) {
            this.exit();
            return;
        }

        this.oldStrings.add(str);

        System.out.println(parser.parse(str));
    }

    public Variable getVariable(String str) {
        return this.variables.get(str);
    }

    private void exit() {
        // Properly exits the program
        System.exit(1);
    }
}
