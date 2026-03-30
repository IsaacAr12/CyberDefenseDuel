package client;

import com.google.gson.Gson;
import network.Message;
import network.MessageType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;
        Gson gson = new Gson();

        try (
            Socket socket = new Socket(host, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Conectado al servidor.");
            System.out.println("1 = REGISTER");
            System.out.println("2 = LOGIN");
            System.out.print("Elige opción: ");
            String option = scanner.nextLine();

            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            Message request;

            if ("1".equals(option)) {
                request = new Message(MessageType.REGISTER, username, password);
            } else {
                request = new Message(MessageType.LOGIN, username, password);
            }

            String jsonRequest = gson.toJson(request);
            out.write(jsonRequest);
            out.newLine();
            out.flush();

            String jsonResponse = in.readLine();
            Message response = gson.fromJson(jsonResponse, Message.class);

            System.out.println("Respuesta del servidor:");
            System.out.println("Tipo: " + response.getType());
            System.out.println("Mensaje: " + response.getMessage());

        } catch (IOException e) {
            System.out.println("Error del cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}