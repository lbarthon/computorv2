package fr.lbarthon.computorv2.computorv1;

import fr.lbarthon.computorv2.computorv1.exceptions.DegreeLimitExceededException;

import java.util.List;

public class Solver {
    private Solver() {}

    public static String solve(Equation equation) throws
            DegreeLimitExceededException
    {
        StringBuilder ret = new StringBuilder();
        if (!equation.isReduced()) {
            equation.reduce();
        }

        ret.append("Reduced form: ").append(equation.toString()).append('\n');

        long degree = equation.getDegree();
        ret.append("Polynomial degree: ").append(degree).append('\n');

        if (degree > 2) {
            throw new DegreeLimitExceededException(degree);
        }

        List<Equation.Entry> entries = equation.getLeftPart().getEntries();

        double a = entries.stream().filter(e -> e.getPower() == 2)
                .map(Equation.Entry::getNbr).findFirst()
                .orElse(0D);
        double b = entries.stream().filter(e -> e.getPower() == 1)
                .map(Equation.Entry::getNbr).findFirst()
                .orElse(0D);
        double c = entries.stream().filter(e -> e.getPower() == 0)
                .map(Equation.Entry::getNbr).findFirst()
                .orElse(0D);

        // If the degree is 1
        if (a == 0) {
            // If the degree is 0
            if (b == 0) {
                if (c == 0) {
                    ret.append("All reals are solutions.");
                } else {
                    ret.append("There's no solution to this equation.");
                }
                return ret.toString();
            }

            ret.append("The solution is:").append('\n')
                    .append(-c / b);
            return ret.toString();
        }

        double delta = b * b - 4 * a * c;
        double deltaSqrt = Utils.sqrt(delta);

        if (delta > 0) {
            ret.append("Discriminant is strictly positive, the two solutions are:").append('\n')
                    .append((-b - deltaSqrt) / (2 * a) + 0.0).append('\n')
                    .append((-b + deltaSqrt) / (2 * a) + 0.0);
        } else if (delta == 0) {
            ret.append("Discriminant is zero, the solution is:").append('\n')
                    .append((-b / (2 * a)) + 0.0);
        } else {
            ret.append("Discriminant is negative, imaginary solutions are:").append('\n')
                    .append(-b / (2 * a)).append(" + i√").append(-delta).append("/").append(2 * a).append('\n')
                    .append(-b / (2 * a)).append(" - i√").append(-delta).append("/").append(2 * a);
        }

        return ret.toString();
    }
}
