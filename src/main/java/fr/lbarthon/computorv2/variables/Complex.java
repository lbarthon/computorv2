package fr.lbarthon.computorv2.variables;

import fr.lbarthon.computorv2.exceptions.ComplexFormatException;
import fr.lbarthon.computorv2.exceptions.ParseException;
import fr.lbarthon.computorv2.utils.MathUtils;
import lombok.Data;
import lombok.NonNull;

@Data
public class Complex implements IVariable {

    private Double real;
    private Double img;

    public Complex(Double real) {
        this(real, 0D);
    }

    public Complex(Double real, Double img) {
        this.real = real;
        this.img = img;
    }

    /**
     * String to complex method
     *
     * @param data input string
     * @return Complex number
     * @throws ParseException
     */
    public static Complex valueOf(String data) throws ParseException, ComplexFormatException {
        data = data.trim();
        int imgIndex = data.indexOf('i');
        if (imgIndex == -1) {
            try {
                return new Complex(Double.parseDouble(data), 0D);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        String leftStr = data.substring(0, imgIndex).trim(),
                rightStr = data.substring(imgIndex + 1).trim();
        Double nbr;

        if (!rightStr.isEmpty()) {
            throw new ParseException(data, imgIndex);
        }

        try {
            nbr = Double.parseDouble(leftStr);
        } catch (NumberFormatException ignored) {
            throw new ComplexFormatException("\"" + leftStr + "\" isn't a valid double");
        }

        return new Complex(0D, nbr);
    }

    public IVariable add(@NonNull IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            Complex c = (Complex) var;
            this.real += c.getReal();
            this.img += c.getImg();
        } else if (var instanceof Matrix) {
            throw new ArithmeticException("A " + this.getType() + " cannot be added to a matrix");
        }
        return this;
    }

    public IVariable sub(@NonNull IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            Complex c = (Complex) var;
            this.real -= c.getReal();
            this.img -= c.getImg();
        } else if (var instanceof Matrix) {
            throw new ArithmeticException("A " + this.getType() + " cannot be subtracted to a matrix");
        }
        return this;
    }

    public IVariable mult(@NonNull IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            Complex c = (Complex) var;

            Double newReal = this.real * c.getReal() - this.img * c.getImg();
            Double newImg = this.img * c.getReal() + this.real * c.getImg();

            this.real = newReal;
            this.img = newImg;
        } else if (var instanceof Matrix) {
            if (this.isComplex()) {
                throw new ArithmeticException("A " + this.getType() + " cannot be multiplied by a matrix");
            } else {
                return var.mult(this);
            }
        }

        return this;
    }

    /**
     * @param var Other Variable
     * @return this
     * @link Complex division {http://uel.unisciel.fr/physique/outils_nancy/outils_nancy_ch04/co/apprendre_03_04.html}
     */
    public IVariable div(@NonNull IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            Complex c = (Complex) var;

            double diviser = MathUtils.square(c.getReal()) + MathUtils.square(c.getImg());

            if (diviser == 0) {
                throw new ArithmeticException("Division by 0");
            }

            Double newReal = (this.real * c.getReal() + this.img * c.getImg()) / diviser;
            Double newImg = (c.getReal() * this.img - this.real * c.getImg()) / diviser;

            this.real = newReal;
            this.img = newImg;
        } else {
            throw new ArithmeticException("A " + this.getType() + " cannot be divided by a matrix");
        }

        return this;
    }

    public IVariable modulo(@NonNull IVariable var) throws ArithmeticException {
        if (var instanceof Complex) {
            Complex c = (Complex) var;

            if (this.isComplex() || c.isComplex()) {
                throw new ArithmeticException("Modulo of complex numbers not handled");
            }

            this.real %= c.getReal();
        } else {
            throw new ArithmeticException("A " + this.getType() + " cannot take a matrix as modulo");
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
                throw new ArithmeticException("Negative power");
            }

            if (c.getReal() == 0) {
                this.real = 1D;
                this.img = 0D;
            } else {
                Complex curr = this.clone();

                for (int i = 1; i < c.getReal(); i++) {
                    this.mult(curr);
                }
            }
        } else {
            throw new ArithmeticException("A " + this.getType() + " cannot take a matrix as power");
        }

        return this;
    }

    public IVariable negate() {
        this.real = -this.real;
        this.img = -this.img;
        this.patchNegZeros();
        return this;
    }

    public boolean isComplex() {
        return this.getImg() != null && this.getImg() != 0;
    }

    public String getType() {
        return this.isComplex() ? "complex" : "double";
    }

    public Complex patchNegZeros() {
        if (this.real == -0D) {
            this.real = 0D;
        }
        if (this.img == -0D) {
            this.img = 0D;
        }
        return this;
    }

    @Override
    public Complex clone() {
        this.patchNegZeros();
        return new Complex(this.real, this.img);
    }

    @Override
    public String toString() {
        this.patchNegZeros();
        if (this.isComplex()) {
            if (this.real != 0D) {
                return this.real + (this.img < 0D ? " - " : " + ") + Math.abs(this.img) + "i";
            } else {
                return this.img + "i";
            }
        }
        return this.real.toString();
    }
}
