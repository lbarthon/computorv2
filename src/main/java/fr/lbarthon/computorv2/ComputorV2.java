package fr.lbarthon.computorv2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ComputorV2 {

    public static final String PROMPT = "> ";
    private static final List<Thread> sockets = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);

            initCleaner();
            initListener(serverSocket);

            while (true) {
                try {
                    Thread.sleep(100);
                    // Maybe do print stuff here
                } catch (InterruptedException ignored) {
                }
            }

        } catch (IOException e) {
            System.err.println("Error creating socket on port 8888.");
        }
    }

    private static void initListener(ServerSocket serverSocket) {
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("A user connected from " + socket.getInetAddress().getHostAddress());
                    Thread t = new Thread(new SocketRunnable(socket));
                    sockets.add(t);
                    t.start();
                } catch (IOException e) {
                    // TODO: Handle exception
                }
            }
        }).start();
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
            Computor computor = new Computor();
            try {
                in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                out = new PrintWriter(this.socket.getOutputStream(), true);
                while (true) {
                    String data = in.readLine();

                    if (data == null || "exit".equalsIgnoreCase(data) || data.isEmpty()) {
                        break;
                    }
                    data = data.trim();

                    System.out.println("Recieved: " + data);
                    String ret = computor.handle(data);
                    out.println(ret);
                }

                this.socket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
