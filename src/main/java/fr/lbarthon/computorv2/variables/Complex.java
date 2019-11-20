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
    private boolean neutral = false;

    public Complex(Double real) {
        this(real, 0D);
    }

    public Complex(Double real, Double img) {
        this.real = real;
        this.img = img;
    }

    /**
     * Allows the user to get a neutral complex, that will avoid any calculation on it
     *
     * @return Complex number
     */
    public static Complex neutral() {
        Complex nbr = new Complex(0D);
        nbr.setNeutral(true);
        return nbr;
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
        Double nbr = null;

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

    public Complex add(@NonNull Complex c) {
        if (this.isNeutral()) return c;
        if (c.isNeutral()) return this;

        this.real += c.getReal();
        this.img += c.getImg();
        return this;
    }

    public Complex sub(@NonNull Complex c) {
        if (this.isNeutral()) return c.opposite();
        if (c.isNeutral()) return this;

        this.real -= c.getReal();
        this.img -= c.getImg();
        return this;
    }

    public Complex mult(@NonNull Complex c) {
        if (this.isNeutral()) return c;
        if (c.isNeutral()) return this;

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
    public Complex div(@NonNull Complex c) throws ArithmeticException {
        if (this.isNeutral()) return c;
        if (c.isNeutral()) return this;

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

    public Complex modulo(@NonNull Complex c) throws ArithmeticException {
        if (this.isNeutral()) return c;
        if (c.isNeutral()) return this;

        if (this.isComplex() || c.isComplex()) {
            throw new ArithmeticException("Modulo of complex numbers not handled");
        }

        this.real %= c.getReal();

        return this;
    }

    public Complex pow(@NonNull Complex c) throws ArithmeticException {
        if (this.isNeutral()) return c;
        if (c.isNeutral()) return this;

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
            this.setReal(1D);
        } else {
            Complex curr = this.clone();

            for (int i = 1; i < c.getReal(); i++) {
                this.mult(curr);
            }
        }

        return this;
    }

    public Complex opposite() {
        System.out.println("Opposite called -> " + this.real + " becomes " + -this.real);
        this.real = -this.real;
        this.img = -this.img;
        this.patchNegZeros();
        return this;
    }

    public boolean isComplex() {
        return this.getImg() != null && this.getImg() != 0;
    }

    public void patchNegZeros() {
        if (this.real == -0D) {
            this.real = 0D;
        }
        if (this.img == -0D) {
            this.img = 0D;
        }
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
                return this.real + (this.img < 0D ? " - " : " + ") + this.img + "i";
            } else {
                return this.img + "i";
            }
        }
        return this.real.toString();
    }
}
