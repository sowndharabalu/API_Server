import controller.UserController;
import repository.UserRepository;
import repository.UserRepositoryImpl;
import server.ApiServer;
import service.UserService;

void main() throws IOException {
    UserRepository repo = new UserRepositoryImpl();
    UserService service = new UserService(repo);
    UserController controller = new UserController(service);

    ApiServer server = new ApiServer(9090, controller);
    server.start();

    IO.println("Server running on http://localhost:9090/api/users");
}