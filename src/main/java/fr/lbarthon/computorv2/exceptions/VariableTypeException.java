package fr.lbarthon.computorv2.exceptions;

import fr.lbarthon.computorv2.variables.VariableType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VariableTypeException extends Exception {
    private VariableType type;
    private VariableType wantedType;
}
