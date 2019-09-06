package fr.lbarthon.computorv2.variables;

import fr.lbarthon.computorv2.utils.MathUtils;
import lombok.Data;

@Data
public class Complex {
    private Double real;
    private Double img;

    public Complex(Double real) {
        this(real, null);
    }

    public Complex(Double real, Double img) {
        this.real = real;
        this.img = img;
    }

    public Complex add(Complex c) {
        this.real += c.getReal();
        this.img += c.getImg();
        return this;
    }

    public Complex sub(Complex c) {
        this.real -= c.getReal();
        this.img -= c.getImg();
        return this;
    }

    public Complex mult(Complex c) {
        Double newReal = this.real * c.getReal() - this.img * c.getImg();
        Double newImg = this.img * c.getReal() + this.real * c.getImg();

        this.real = newReal;
        this.img = newImg;

        return this;
    }
    /**
     * @param c Other complex number
     * @return this
     * @link http://uel.unisciel.fr/physique/outils_nancy/outils_nancy_ch04/co/apprendre_03_04.html
     */
    public Complex div(Complex c) throws ArithmeticException {
        double diviser = MathUtils.square(c.getReal()) + MathUtils.square(c.getImg());

        if (diviser == 0) {
            throw new ArithmeticException("Division by 0");
        }

        Double newReal = (this.real * c.getReal() + this.img * c.getImg()) / diviser;
        Double newImg = (c.getReal() * this.img - this.real * c.getImg()) / diviser;

        this.real = newReal;
        this.img = newImg;

        return this;
    }

    public Complex modulo(Complex c) {
        if (this.isComplex() || c.isComplex()) {

            return this;
        }

        this.real %= c.getReal();

        return this;
    }

    public Complex pow(Complex c) throws ArithmeticException {
        if (c.isComplex()) {
            throw new ArithmeticException("Complex number as power");
        }
        if (c.getReal() % 1 != 0) {
            throw new ArithmeticException("Non integer power");
        }
        if (c.getReal() >= 0) {
            throw new ArithmeticException("Negative power");
        }

        Complex curr = this.clone();

        for (int i = 1; i < c.getReal(); i++) {
            this.mult(curr);
        }

        return this;
    }

    public boolean isComplex() {
        return this.getImg() != null && this.getImg() != 0;
    }

    public Complex clone() {
        return new Complex(this.real, this.img);
    }
}
