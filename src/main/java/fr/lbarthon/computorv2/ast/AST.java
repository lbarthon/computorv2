package fr.lbarthon.computorv2.ast;

import fr.lbarthon.computorv2.exceptions.*;
import fr.lbarthon.computorv2.parser.Parser;
import fr.lbarthon.computorv2.variables.Function;
import fr.lbarthon.computorv2.variables.IVariable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AST {

    private static final Pattern ASK = Pattern.compile("(=\\s*\\?)$");

    private final Parser parser;
    @Getter
    private final Node head;
    @Setter @Getter
    private Exception exception = null;
    @Getter
    private boolean ask = false;

    // Each unknown variable with a boolean if it's meant to be unknown or not
    private Map<String, Boolean> unknowns = new HashMap<>();

    public void createFrom(String data) throws ParseException, ComplexFormatException, MatrixFormatException, UnknownFunctionException {
        data = data.trim();
        Matcher matcher = ASK.matcher(data);
        if (matcher.matches()) {
            this.ask = true;
            // Here we remove the " = ?" part
            data = matcher.replaceAll("").trim();
        }
        this.head.setTemp(data);
        this.parser.parse(this.head);
    }

    public IVariable solve() throws ArithmeticException, UnknownVariableException {
        try {
            return this.head.solve();
        } catch (StopCalculationException e) {
            System.out.println("Calculation stopped");
            return null;
        }
    }

    public AST clone() {
        AST clone = new AST(this.parser, this.head.clone());
        if (!this.unknowns.isEmpty()) {
            clone.unknowns.putAll(this.unknowns);
        }
        // Resetting exception on clone
        clone.exception = null;
        return clone;
    }

    @Override
    public String toString() {
        return this.head.toString();
    }

    public boolean isFunctionAssignation() {
        return this.unknowns.values().stream().anyMatch(b -> b);
    }

    public void validateUnknown(String str) {
        System.out.println("Unknown " + str + " validated");
        this.unknowns.put(str, true);
    }

    public Set<String> getWithStatus(boolean bool) {
        return this.unknowns.entrySet().stream()
                .filter(el -> el.getValue() == bool)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public boolean getUnknownStatus(String str) {
        return this.unknowns.computeIfAbsent(str, s -> false);
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
