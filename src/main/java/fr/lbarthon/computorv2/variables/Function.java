package fr.lbarthon.computorv2.variables;

import fr.lbarthon.computorv2.Computor;
import fr.lbarthon.computorv2.ast.AST;
import fr.lbarthon.computorv2.ast.Node;
import fr.lbarthon.computorv2.exceptions.UnknownVariableException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class Function {
    private List<String> params;
    private AST data;

    public IVariable call(Computor computor, Node...nodes) throws ArithmeticException, UnknownVariableException {
        return this.call(computor, Arrays.stream(nodes).map(n -> new AST(computor.getParser(), n)).toArray(AST[]::new));
    }

    public IVariable call(Computor computor, AST ...args) throws ArithmeticException, UnknownVariableException {
        AST function = this.data.clone();
        // Initialize temp variables (x / y)
        for (int i = 0; i < args.length; i++) {
            AST ast = args[i];
            IVariable res = ast.solve();
            computor.getTempVariables().put(this.params.get(i), res);
        }
        return function.solve();
    }
}
