package fr.lbarthon.computorv2;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.*;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ComputorV2 {

    private static final List<Thread> sockets = new ArrayList<>();

    public static final String PROMPT = "> ";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);

            initCleaner();

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("A user connected from " + socket.getInetAddress().getHostAddress());
                Thread t = new Thread(new SocketRunnable(socket));
                sockets.add(t);
                t.start();
            }
            // System.out.println("Shutting down socket server...");
            // serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error creating socket on port 8888.");
        }


        // Computor computorInstance = new Computor();
        //run(computorInstance);
    }

    private static void initCleaner() {
        new Thread(() -> {
            while (true) {
                sockets.removeIf(t -> t.getState() != Thread.State.RUNNABLE);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    private static void run(Computor computorInstance) {
        TerminalBuilder builder = TerminalBuilder.builder();
        builder.system(false).streams(System.in, System.out);

        try {
            Terminal terminal = builder.build();
            System.out.println(terminal.getClass().getName());
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .parser(null)
                    .build();

            reader.setOpt(LineReader.Option.ERASE_LINE_ON_FINISH);

            String str;

            do {
                str = reader.readLine(PROMPT);
                computorInstance.handle(str.trim());
            } while (str != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class SocketRunnable implements Runnable {

        private Socket socket;

        SocketRunnable(Socket socket) {
            super();
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                out = new PrintWriter(this.socket.getOutputStream(), true);
                while (true) {
                    String data = in.readLine();

                    if (data == null || "exit".equalsIgnoreCase(data) || data.isEmpty()) {
                        break;
                    }

                    System.out.println("Recieved: " + data);
                    out.println("Hi : " + data);
                }

                this.socket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                } catch (IOException ignored) {}
            }
        }
    }
}
