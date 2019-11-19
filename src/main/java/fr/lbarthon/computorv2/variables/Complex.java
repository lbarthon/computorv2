package fr.lbarthon.computorv2.variables;

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
    public static Complex valueOf(String data) throws ParseException {
        try {
            data = data.trim();
            int imgIndex = data.indexOf('i');
            if (imgIndex == -1) {
                return new Complex(Double.parseDouble(data), 0D);
            }

            String leftStr = data.substring(0, imgIndex),
                    rightStr = data.substring(imgIndex + 1);
            Double left = null, right = null;
            try {
                left = Double.parseDouble(leftStr);
                right = Double.parseDouble(rightStr);
            } catch (NumberFormatException ignored) {
            }

            if (left != null && right != null) {
                throw new ParseException(data, 0);
            }
            if (left == null && right == null) {
                return new Complex(0D, 1D);
            }

            return new Complex(0D, left == null ? right : left);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Complex add(@NonNull Complex c) {
        this.real += c.getReal();
        this.img += c.getImg();
        return this;
    }

    public Complex sub(@NonNull Complex c) {
        this.real -= c.getReal();
        this.img -= c.getImg();
        return this;
    }

    public Complex mult(@NonNull Complex c) {
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
        if (this.isComplex() || c.isComplex()) {
            throw new ArithmeticException("Modulo of complex numbers not handled");
        }

        this.real %= c.getReal();

        return this;
    }

    public Complex pow(@NonNull Complex c) throws ArithmeticException {
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
