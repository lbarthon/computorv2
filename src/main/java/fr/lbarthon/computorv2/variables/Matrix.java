package fr.lbarthon.computorv2.variables;

import fr.lbarthon.computorv2.exceptions.MatrixFormatException;
import fr.lbarthon.computorv2.exceptions.ParseException;
import fr.lbarthon.computorv2.utils.StringUtils;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
        return this.setLine(line, Arrays.stream(elements).map(Complex::new).toArray(Complex[]::new));
    }

    public Matrix setLine(int line, Complex ...elements) {
        if (line >= this.x || elements.length != y) return this;
        this.tab[line] = elements;
        return this;
    }

    public Stream<Complex> streamAll() {
        return Arrays.stream(this.tab).map(Arrays::stream).flatMap(c -> c);
    }

    public static Matrix valueOf(String str) throws ParseException, MatrixFormatException {
        char start = '[', end = ']';
        char[] arr = str.toCharArray();
        if (str.length() < 2 || arr[0] != start) throw new ParseException(str, 0);
        if (arr[arr.length - 1] != end) throw new ParseException(str, arr.length - 1);

        // Looping on every row to check if all sizes are alike
        List<Integer> startIndexes = StringUtils.indexesOf(str.substring(1), start);
        List<Integer> endIndexes = StringUtils.indexesOf(str.substring(0, str.length() - 1), end);

        List<List<Double>> numbers = new ArrayList<>();

        while (!startIndexes.isEmpty() && !endIndexes.isEmpty()) {
            // + 2 compensates the substring on startIndexes input (1) and removes the bracket (1 more)
            int starti = startIndexes.remove(0) + 2;
            int endi = endIndexes.remove(0);
            if (!startIndexes.isEmpty()) {
                String sep = str.substring(endi + 1, startIndexes.get(0) + 1).trim();
                if (sep.length() != 1 || sep.charAt(0) != ';') {
                    throw new ParseException(str, endi + 1);
                }
            }
            String row = str.substring(starti, endi);
            List<Integer> commas = StringUtils.indexesOf(row, ',');
            commas.add(endi - starti);
            int prevIndex = 0;
            List<Double> line = new ArrayList<>();
            for (int commaIndex : commas) {
                String nbrStr = row.substring(prevIndex, commaIndex);
                try {
                    line.add(Double.parseDouble(nbrStr));
                } catch (NumberFormatException e) {
                    throw new ParseException(str, starti + prevIndex);
                }
                prevIndex = commaIndex + 1;
            }
            numbers.add(line);
        }

        // Handling empty list, to avoid exceptions
        if (numbers.isEmpty()) return null;
        int wantedSize = numbers.get(0).size();
        for (int i = 1; i < numbers.size(); i++) {
            if (numbers.get(i).size() != wantedSize) {
                throw new MatrixFormatException(i + 1, numbers.get(i).size(), wantedSize);
            }
        }

        Matrix ret = new Matrix(numbers.size(), wantedSize);
        int lineNbr = 0;
        for (List<Double> line : numbers) {
            ret.setLine(lineNbr++, line.toArray(new Double[0]));
        }

        return ret;
    }

    public Matrix clone() {
        Matrix clone = new Matrix(this.x, this.y);
        for (int i = 0; i < this.x; i++) {
            for (int j = 0; j < this.y; j++) {
                clone.tab[i][j] = this.tab[i][j].clone();
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

    public IVariable sub(@NonNull IVariable var) throws ArithmeticException {
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

    public IVariable add(@NonNull IVariable var) throws ArithmeticException {
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

    public IVariable modulo(@NonNull IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            Complex c = (Complex) var;
            throw new ArithmeticException("Cannot calculate the modulo of a matrix by a " + c.getType());
        } else if (var instanceof Matrix) {
            throw new ArithmeticException("Cannot calculate the modulo of two matrices");
        }
        return this;
    }

    public IVariable pow(@NonNull IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            Complex c = (Complex) var;

            if (c.isComplex()) {
                throw new ArithmeticException("Complex number as power");
            }
            if (c.getReal() % 1 != 0) {
                throw new ArithmeticException("Non integer power");
            }
            if (c.getReal() < 0) {
                // We handle matrix inversion
                if (c.getReal() == -1) {
                    throw new ArithmeticException("Matrix inversion not handled yet");
                } else {
                    throw new ArithmeticException("Negative power");
                }
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

    /**
     * @param var Other variable
     * @return this
     * @throws ArithmeticException
     * @link Matricial product {https://fr.wikipedia.org/wiki/Produit_matriciel_de_Hadamard}
     */
    public IVariable mult(@NonNull IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            Complex c = (Complex) var;
            if (c.isComplex()) {
                throw new ArithmeticException("Cannot multiply a matrix with a " + c.getType());
            }
            this.streamAll().forEach(el -> el.mult(c));
        } else if (var instanceof Matrix) {
            Matrix m = (Matrix) var;
            if (m.x != this.x || m.y != this.y) {
                throw new ArithmeticException("Cannot multiply two matrices of different sizes");
            }
            for (int i = 0; i < this.x; i++) {
                for (int j = 0; j < this.y; j++) {
                    this.tab[i][j].mult(m.tab[i][j]);
                }
            }
        }
        return this;
    }

    public IVariable dblmult(@NonNull IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            throw new ArithmeticException("Cannot process a matricial product with " + ((Complex) var).getType());
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

                    ret.tab[i][j] = tmp;
                }
            }
            return ret;
        }
        return this;
    }

    public IVariable div(@NonNull IVariable var) throws ArithmeticException {
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

    @Override
    public String toString() {
        return Arrays.stream(this.tab)
                .map(row -> "[" + Arrays.stream(row).map(Complex::toString).collect(Collectors.joining(",")) + "]")
                .collect(Collectors.joining("\n"));
    }
}
