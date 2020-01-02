package fr.lbarthon.computorv2.computorv1;

import fr.lbarthon.computorv2.computorv1.exceptions.DegreeLimitExceededException;
import fr.lbarthon.computorv2.computorv1.exceptions.InvalidPowerException;
import fr.lbarthon.computorv2.computorv1.exceptions.ParserException;

public class ComputorV1 {

    public static String solve(String input) {
        try {
            return Solver.solve(Parser.parse(input));
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (DegreeLimitExceededException e) {
            return e.getClass().getCanonicalName() + " - The polynomial degree is strictly greater than 2, I can't solve.";
        } catch (InvalidPowerException e) {
            return e.getClass().getCanonicalName() + " - Unhandled power: " + e.getPower();
        } catch (Throwable ignored) {}

        return "Sorry, an error occured... Is your input valid ?";
    }
}
