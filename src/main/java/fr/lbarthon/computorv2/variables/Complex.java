package fr.lbarthon.computorv2.variables;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Complex implements IVariable {
    public static final VariableType TYPE = VariableType.NUMBER;

    private double real;
    private double img;
}
