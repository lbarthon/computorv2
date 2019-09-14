package fr.lbarthon.computorv2;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

public class ComputorV2 {

    public static final String PROMPT = "> ";

    public static void main(String[] args) {
        Computor computorInstance = new Computor();

        run(computorInstance);
    }

    private static void run(Computor computorInstance) {
        TerminalBuilder builder = TerminalBuilder.builder();
        builder.system(false).streams(System.in, System.out);

        try {
            Terminal terminal = builder.build();
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .parser(null)
                    .build();

            reader.setOpt(LineReader.Option.ERASE_LINE_ON_FINISH);

            String str;

            do {
                str = reader.readLine(PROMPT, null, (MaskingCallback) null, null);
                computorInstance.handle(str.trim());
            } while (str != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
