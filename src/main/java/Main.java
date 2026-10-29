import controller.UserController;
import repository.UserRepository;
import repository.UserRepositoryImpl;
import server.ApiServer;
import service.UserService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        UserRepository repo = new UserRepositoryImpl();
        UserService service = new UserService(repo);
        UserController controller = new UserController(service);

        ApiServer server = new ApiServer(9090, controller);
        server.start();

        System.out.println("Server running on http://localhost:9090/api/users");
    }
}