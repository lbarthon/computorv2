package fr.lbarthon.computorv2.ast;

import fr.lbarthon.computorv2.exceptions.*;
import fr.lbarthon.computorv2.parser.Parser;
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

    private static final Pattern ASK = Pattern.compile("^(?<data>.*)(=\\s*\\?)$");

    private final Parser parser;
    @Getter
    private final Node head;
    @Setter @Getter
    private Exception exception = null;
    @Getter
    private boolean ask = false;

    // Each unknown variable with a boolean if it's meant to be unknown or not
    private Map<String, Boolean> unknowns = new HashMap<>();

    public void createFrom(String data) throws ParseException, ComplexFormatException, MatrixFormatException {
        data = data.trim();
        Matcher matcher = ASK.matcher(data);
        if (matcher.matches()) {
            this.ask = true;
            // Here we remove the " = ?" part
            data = matcher.group("data").trim();
        }
        this.head.setTemp(data);
        this.parser.parse(this.head);
    }

    public IVariable solve() throws ArithmeticException, UnknownVariableException, AssignationException {
        try {
            return this.solveNoCatch();
        } catch (StopCalculationException e) {
            System.out.println("Calculation stopped");
            return null;
        }
    }

    /**
     * This solve method don't try / catch the StopCalculationException but throws it
     *
     * @return
     * @throws ArithmeticException
     * @throws UnknownVariableException
     * @throws StopCalculationException
     */
    public IVariable solveNoCatch() throws ArithmeticException, UnknownVariableException, StopCalculationException, AssignationException {
        return this.head.solve();
    }

    public void replaceVariables() {
        this.head.replaceVariables();
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
        this.unknowns.putIfAbsent(str, false);
    }

    public void removeUnknown(String str) {
        this.unknowns.remove(str);
    }

    public void removeUnknowns(List<String> arr) {
        arr.forEach(this.unknowns::remove);
    }
}
