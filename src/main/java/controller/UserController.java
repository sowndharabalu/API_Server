package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import Util.JsonUtil;
import model.User;
import service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserController implements HttpHandler {
    UserService service;
    public  UserController(UserService service) {
        this.service = service;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        if ("GET".equals(method) && path.equals("/api/users")) {
            String json=JsonUtil.userListToJson(service.getAllUsers());
            sendJsonResponse(exchange, json, 200);
        } else if ("GET".equals(method) && path.matches("/api/users/\\d+")) {
            int id = Integer.parseInt(path.substring("/api/users/".length()));
            Optional<User> user=service.getUserById(id);
            if (user.isPresent()) {
                String json=JsonUtil.usertoJson(user.get());
                sendJsonResponse(exchange, json, 200);
            }else {
                sendErrorResponse(exchange, "\"error\":\"User not found\"", 404);
            }
        } else if ("POST".equals(method) && path.equals("/api/users")) {
            String json= readRequestBody(exchange);
            User user=JsonUtil.jsontoUser(json);
            service.createUser(user);
            sendJsonResponse(exchange, json+" created!", 201);
        } else if ("PUT".equals(method) && path.matches("/api/users/\\d+")) {
            int id = Integer.parseInt(path.substring("/api/users/".length()));
            String json=readRequestBody(exchange);
            String name=JsonUtil.extractJsonField(json, "name");
            String email=JsonUtil.extractJsonField(json, "email");
            LocalDateTime time=LocalDateTime.parse(JsonUtil.extractJsonField(json, "time"));
            service.UpdateUser(User.create(id, name, email, time));
        } else if ("DELETE".equals(method) && path.matches("/api/users/\\d+")) {
            int id = Integer.parseInt(path.substring("/api/users/".length()));
            boolean t=service.deleteUser(id);
            if (t) {
                sendJsonResponse(exchange, "deleted!", 200);
            }else {
                sendErrorResponse(exchange, "\"error\":\"User not found\"", 404);
            }
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        return exchange.getRequestBody().toString();
    }
    private void sendJsonResponse(HttpExchange exchange, String json, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, json.length());
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();
    }
    private void sendErrorResponse(HttpExchange exchange, String msg, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, msg.length());
        exchange.getResponseBody().write(msg.getBytes());
        exchange.getResponseBody().close();
    }
}
