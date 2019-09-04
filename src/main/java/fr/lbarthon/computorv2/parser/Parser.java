package fr.lbarthon.computorv2.parser;

import fr.lbarthon.computorv2.Computor;
import fr.lbarthon.computorv2.variables.IVariable;
import fr.lbarthon.computorv2.variables.Variable;
import fr.lbarthon.computorv2.variables.VariableType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Parser {

    private Computor computor;

    public Parser(Computor computor) {
        this.computor = computor;
    }

    public String parse(String str) {
        int index = str.indexOf('=');
        if (index > 0) {
            VariableType type = VariableType.getTypeOfString(str);
        }

        return str;
    }
}
