package fr.lbarthon.computorv2.utils;

public class MathUtils {
    private MathUtils() {}

    public static double pow(double nbr, int pow) {
        if (pow == 0) return 1;
        return nbr * pow(nbr, pow - 1);
    }
}
