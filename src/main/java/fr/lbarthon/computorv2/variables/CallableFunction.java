package fr.lbarthon.computorv2.variables;

import fr.lbarthon.computorv2.Computor;
import fr.lbarthon.computorv2.ast.AST;
import fr.lbarthon.computorv2.exceptions.UnknownVariableException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class gives us an implementation of functions that stores an AST
 * for each arg, so it can be solved
 */
public class CallableFunction extends Function {
    private List<AST> args;

    public CallableFunction(List<String> params, AST data) {
        super(params, data);
        this.args = new ArrayList<>();
    }

    public CallableFunction(Function fct) {
        this(new ArrayList<>(fct.params), fct.data.clone());
    }

    public void addArg(AST ast) {
        this.args.add(ast);
    }

    public IVariable solve(Computor computor) throws UnknownVariableException {
        AST[] args = new AST[]{};
        return this.call(computor, this.args.toArray(args));
    }

    public CallableFunction clone() {
        CallableFunction clone = (CallableFunction) super.clone();
        clone.args = new ArrayList<>(this.args);
        return clone;
    }
}
