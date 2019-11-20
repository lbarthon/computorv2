package fr.lbarthon.computorv2.variables;

import fr.lbarthon.computorv2.exceptions.ParseException;

import java.util.Arrays;
import java.util.stream.Stream;

public class Matrix implements IVariable {

    private final int x;
    private final int y;
    private Complex[][] tab;

    public Matrix(int x, int y) {
        this.x = x;
        this.y = y;
        this.tab = new Complex[x][y];
    }

    public Matrix setLine(int line, Double ...elements) {
        return this.setLine(line, (Complex[]) Arrays.stream(elements).map(Complex::new).toArray());
    }

    public Matrix setLine(int line, Complex ...elements) {
        if (line >= this.x || elements.length != y) return this;
        this.tab[line] = elements;
        return this;
    }

    public Stream<Complex> streamAll() {
        return Arrays.stream(this.tab).map(Arrays::stream).flatMap(c -> c);
    }

    public static Matrix valueOf(String str) throws ParseException {

        return null;
    }

    public Matrix clone() {
        Matrix clone = new Matrix(this.x, this.y);
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                clone.tab[x][y] = this.tab[x][y].clone();
            }
        }
        return clone;
    }

    public IVariable negate() {
        for (int i = 0; i < this.x; i++) {
            for (int j = 0; j < this.y; j++) {
                this.tab[i][j].negate();
            }
        }
        return this;
    }

    public IVariable sub(IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            Complex c = (Complex) var;
            throw new ArithmeticException("Cannot subtract a " + c.getType() + " to a matrix");
        } else if (var instanceof Matrix) {
            Matrix m = (Matrix) var;
            if (m.x != this.x || m.y != this.y) {
                throw new ArithmeticException("Cannot subtract matrices of different sizes");
            }
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    this.tab[x][y].sub(m.tab[x][y]);
                }
            }
        }
        return this;
    }

    public IVariable add(IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            Complex c = (Complex) var;
            throw new ArithmeticException("Cannot add a " + c.getType() + " to a matrix");
        } else if (var instanceof Matrix) {
            Matrix m = (Matrix) var;
            if (m.x != this.x || m.y != this.y) {
                throw new ArithmeticException("Cannot add matrices of different sizes");
            }
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    this.tab[x][y].add(m.tab[x][y]);
                }
            }
        }
        return this;
    }

    public IVariable modulo(IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            Complex c = (Complex) var;
            throw new ArithmeticException("Cannot calculate the modulo of a matrix by a " + c.getType());
        } else if (var instanceof Matrix) {
            throw new ArithmeticException("Cannot calculate the modulo of two matrices");
        }
        return this;
    }

    public IVariable pow(IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            Complex c = (Complex) var;

            if (c.isComplex()) {
                throw new ArithmeticException("Complex number as power");
            }
            if (c.getReal() % 1 != 0) {
                throw new ArithmeticException("Non integer power");
            }
            if (c.getReal() < 0) {
                throw new ArithmeticException("Negative power");
            }

            if (c.getReal() == 0) {
                if (this.x != this.y) {
                    throw new ArithmeticException("Cannot process the power 0 of a non square matrix");
                }
                return identity(this.x);
            }
            Matrix current = this.clone();
            for (int i = 1; i < c.getReal(); i++) {
                this.mult(current);
            }
        } else if (var instanceof Matrix) {
            throw new ArithmeticException("Cannot calculate the power of two matrices");
        }
        return this;
    }

    public IVariable mult(IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            Complex c = (Complex) var;
            if (c.isComplex()) {
                throw new ArithmeticException("Cannot multiply a matrix with a " + c.getType());
            }
            this.streamAll().forEach(el -> el.mult(c));
        } else if (var instanceof Matrix) {
            Matrix m = (Matrix) var;
            if (this.y != m.x) {
                throw new ArithmeticException("Cannot process A x B if A columns != B rows");
            }
            Matrix ret = new Matrix(this.x, m.y);
            for (int i = 0; i < ret.x; i++) {
                for (int j = 0; j < ret.y; j++) {
                    Complex tmp = new Complex(0D);

                    // We loop over the number that is common to the two matrices
                    for (int k = 0; k < this.y; k++) {
                        tmp.add(this.tab[i][k].clone().mult(m.tab[k][j]));
                    }

                    ret.tab[x][y] = tmp;
                }
            }
            return ret;
        }
        return this;
    }

    public IVariable div(IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            Complex c = (Complex) var;
            if (c.isComplex()) {
                throw new ArithmeticException("Cannot divide a matrix by a " + c.getType());
            }
            if (c.getReal() == 0) {
                throw new ArithmeticException("Division by 0");
            }
            this.streamAll().forEach(el -> el.div(c));
        } else if (var instanceof Matrix) {
            Matrix m = (Matrix) var;
            if (m.x != this.x || m.y != this.y) {
                throw new ArithmeticException("Cannot divide matrices of different sizes");
            }
            // TODO: Handle if possible
        }
        return this;
    }

    public static Matrix identity(int size) {
        Matrix identity = new Matrix(size, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                identity.tab[i][j] = new Complex(i == j ? 1D : 0D);
            }
        }
        return identity;
    }
}
