package fr.lbarthon.computorv2.variables;

public interface IVariable {
    IVariable clone();

    // Method to opposite all the Variable
    IVariable negate();

    // All methods to execute basic operations
    IVariable sub(IVariable v) throws ArithmeticException;
    IVariable add(IVariable v) throws ArithmeticException;
    IVariable modulo(IVariable v) throws ArithmeticException;
    IVariable pow(IVariable v) throws ArithmeticException;
    IVariable mult(IVariable v) throws ArithmeticException;
    IVariable div(IVariable v) throws ArithmeticException;
}
