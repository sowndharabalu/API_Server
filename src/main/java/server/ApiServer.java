package server;

import com.sun.net.httpserver.HttpServer;
import controller.UserController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class ApiServer {
    HttpServer server;
    int PORT;
    public ApiServer(int port, UserController controller) throws IOException {
        PORT = port;
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.createContext("/api/users", controller);
        server.createContext("/api/users/", controller);
    }
    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        server.start();
    }
    public void shutdown() {
        server.stop(1);
    }
}
