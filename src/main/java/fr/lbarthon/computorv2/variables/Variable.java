package fr.lbarthon.computorv2.variables;

import lombok.Getter;
import lombok.Setter;

public class Variable<T extends IVariable> {

    @Getter @Setter
    private T value;

    public Variable(T value) {
        this.value = value;
    }

    public VariableType getType() {
        return this.value.TYPE;
    }
}
