package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class TestClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;

        try (
            Socket socket = new Socket(host, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Conectado al servidor.");
            System.out.println("Escribe JSON y presiona Enter.");
            System.out.println("Escribe salir para terminar.");

            while (true) {
                System.out.print("JSON> ");
                String line = scanner.nextLine();

                if ("salir".equalsIgnoreCase(line)) {
                    break;
                }

                out.write(line);
                out.newLine();
                out.flush();

                String response = in.readLine();
                System.out.println("Servidor> " + response);
            }

        } catch (IOException e) {
            System.out.println("Error del cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}