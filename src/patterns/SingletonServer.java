package patterns;
import java.io.IOException;
import java.net.ServerSocket;

/** 
 * Ejemplo de como podriamos usar el Singleton para el servidor.
 * Meramente un boceto de como se podria hacer el server.
 */
public class SingletonServer {
    private static volatile SingletonServer instance;
    private ServerSocket serverSocket;
    private int port;

    private SingletonServer(int port) {
        this.port = port;
    }

    public static SingletonServer getInstance(int port) {
        if (instance == null) {
            synchronized (SingletonServer.class) {
                if (instance == null) {
                    instance = new SingletonServer(port);
                }
            }
        }
        return instance;
    }

    public void start() throws IOException {
        if (serverSocket == null || serverSocket.isClosed()) {
            serverSocket = new ServerSocket(port);
            // aceptar conexiones en otro hilo en la implementación real
        }
    }

    public void stop() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
}