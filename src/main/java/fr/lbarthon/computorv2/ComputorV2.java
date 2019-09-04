package fr.lbarthon.computorv2;

import java.io.Console;

public class ComputorV2 {

    public static final String PROMPT = "> ";

    public static void main(String[] args) {
        Computor computorInstance = new Computor();

        run(computorInstance);
    }

    private static void run(Computor computorInstance) {
        Console console = System.console();
        if (console != null) {
            String str;
            do {
                console.printf(PROMPT);
                str = console.readLine();
                computorInstance.handle(str);
            } while (str != null);
        } else {
            System.out.println("Error. Null console.");
        }
    }
}
