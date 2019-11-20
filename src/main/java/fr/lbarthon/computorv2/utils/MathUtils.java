package fr.lbarthon.computorv2.utils;

public class MathUtils {
    private MathUtils() {
    }

    public static Double pow(Double nbr, int pow) {
        if (pow == 0) return 1D;
        if (pow < 0) return 1 / pow(nbr, -pow);
        return nbr * pow(nbr, pow - 1);
    }

    public static Double square(Double nbr) {
        return pow(nbr, 2);
    }

    public static Double factorial(Double nbr) {
        if (nbr < 0) return -1D;
        if (nbr <= 1) return 1D;
        return nbr * factorial(nbr - 1);
    }

    public static Double sqrt(Double x) {
        if (x <= 0) return 0D;
        double last = 0.0;
        double res = 1.0;
        while (res != last) {
            last = res;
            res = (res + x / res) / 2;
        }
        return res;
    }
}
