package fr.lbarthon.computorv2.variables;

import fr.lbarthon.computorv2.Computor;
import fr.lbarthon.computorv2.ast.AST;
import fr.lbarthon.computorv2.exceptions.AssignationException;
import fr.lbarthon.computorv2.exceptions.StopCalculationException;
import fr.lbarthon.computorv2.exceptions.UnknownVariableException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class gives us an implementation of functions that stores an AST
 * for each arg, so it can be solved
 */
@Getter
public class CallableFunction extends Function {
    private List<AST> args;
    private String name;

    public CallableFunction(List<String> params, AST data, String name) {
        super(params, data);
        this.args = new ArrayList<>();
        this.name = name;
    }

    public CallableFunction(Function fct, String name) {
        this(new ArrayList<>(fct.params), fct.data.clone(), name);
    }

    public void addArg(AST ast) {
        this.args.add(ast);
    }

    public IVariable solve(Computor computor) throws ArithmeticException, UnknownVariableException, StopCalculationException, AssignationException {
        return this.call(computor, this.args.toArray(new AST[]{}));
    }

    public CallableFunction clone() {
        CallableFunction clone = new CallableFunction(new ArrayList<>(this.params), this.data.clone(), this.name);
        clone.args = new ArrayList<>(this.args);
        return clone;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
