package fr.lbarthon.computorv2.ast;

import fr.lbarthon.computorv2.exceptions.ComplexFormatException;
import fr.lbarthon.computorv2.exceptions.ParseException;
import fr.lbarthon.computorv2.exceptions.UnknownVariableException;
import fr.lbarthon.computorv2.parser.Parser;
import fr.lbarthon.computorv2.variables.IVariable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class AST {

    private final Parser parser;
    @Getter
    private final Node head;
    @Setter @Getter
    private Exception exception = null;

    // Each unknown variable with a boolean if it's meant to be unknown or not
    private Map<String, Boolean> unknowns = new HashMap<>();

    public void createFrom(String data) throws ParseException, ComplexFormatException {
        this.head.setTemp(data);
        this.parser.parse(this.head);
    }

    public IVariable solve() throws ArithmeticException, UnknownVariableException {
        return this.head.solve();
    }

    @Override
    public String toString() {
        return this.head.toString();
    }

    public boolean isEquation() {
        return this.unknowns.values().stream().anyMatch(b -> b);
    }

    public void validateUnknown(String str) {
        System.out.println("Unknown " + str + " validated");
        this.unknowns.put(str, true);
    }

    public void addUnknown(String str) {
        System.out.println("Unknown " + str + " added");
        this.unknowns.computeIfAbsent(str, s -> false);
    }

    public void removeUnknown(String str) {
        System.out.println("Unknown " + str + " removed");
        this.unknowns.remove(str);
    }
}
