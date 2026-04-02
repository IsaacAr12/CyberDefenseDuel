package client;

import network.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.Socket;

public class GameClient {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    private MessageListener listener;

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        Thread receiver = new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    if (listener != null) {
                        listener.onMessage(Message.fromJson(line));
                    }
                }
            } catch (IOException e) {
                System.out.println("Conexión cerrada.");
            }
        });

        receiver.setDaemon(true);
        receiver.start();
    }

    public void disconnect() {
        try {
            if (in != null) in.close();
        } catch (Exception ignored) {}

        try {
            if (out != null) out.close();
        } catch (Exception ignored) {}

        try {
            if (socket != null) socket.close();
        } catch (Exception ignored) {}
    }

    public void sendMessage(Message msg) {
        try {
            out.write(msg.toJson());
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    public interface MessageListener {
        void onMessage(Message message);
    }
}