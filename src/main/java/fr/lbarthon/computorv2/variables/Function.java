package fr.lbarthon.computorv2.variables;

import fr.lbarthon.computorv2.Computor;
import fr.lbarthon.computorv2.ast.AST;
import fr.lbarthon.computorv2.ast.Node;
import fr.lbarthon.computorv2.exceptions.StopCalculationException;
import fr.lbarthon.computorv2.exceptions.UnknownVariableException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class Function {
    protected List<String> params;
    protected AST data;

    public Function clone() {
         return new Function(new ArrayList<>(this.params), this.data.clone());
    }

    public IVariable call(Computor computor, Node...nodes) throws ArithmeticException, UnknownVariableException, StopCalculationException {
        return this.call(computor, Arrays.stream(nodes).map(n -> new AST(computor.getParser(), n)).toArray(AST[]::new));
    }

    public IVariable call(Computor computor, AST ...args) throws ArithmeticException, UnknownVariableException, StopCalculationException {
        AST function = this.data.clone();
        // Initialize temp variables (x / y)
        for (int i = 0; i < args.length; i++) {
            AST ast = args[i];
            IVariable res = ast.solveNoCatch();
            computor.getTempVariables().put(this.params.get(i), res);
        }

        IVariable ret = function.solveNoCatch();
        computor.getTempVariables().clear();
        return ret;
    }
}
