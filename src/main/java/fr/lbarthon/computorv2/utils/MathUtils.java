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
}
