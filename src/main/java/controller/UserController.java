package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import Util.JsonUtil;
import model.User;
import service.UserService;

import java.io.IOException;
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
            User created=service.createUser(user);
            sendJsonResponse(exchange, JsonUtil.usertoJson(created), 201);
        } else if ("PUT".equals(method) && path.matches("/api/users/\\d+")) {
            int id = Integer.parseInt(path.substring("/api/users/".length()));
            String json=readRequestBody(exchange);
            User user=JsonUtil.jsontoUser(json);
            User updated=service.updateUser(User.create(id,user.name(),user.email(),user.createdAt()));
            sendJsonResponse(exchange, JsonUtil.usertoJson(updated), 200);
        } else if ("DELETE".equals(method) && path.matches("/api/users/\\d+")) {
            int id = Integer.parseInt(path.substring("/api/users/".length()));
            boolean t=service.deleteUser(id);
            if (t) {
                sendJsonResponse(exchange, "deleted!", 200);
            }else {
                sendErrorResponse(exchange, "\"error\":\"User not found\"", 404);
            }
        }else {
            sendErrorResponse(exchange, "\"error\":\"Not Found\"", 404);
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (var is = exchange.getRequestBody()) {
            return new String(is.readAllBytes());
        }
    }
    private void sendJsonResponse(HttpExchange exchange, String json, int statusCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        try (var os = exchange.getResponseBody()) {
            os.write(json.getBytes());
        }
    }
    private void sendErrorResponse(HttpExchange exchange, String msg, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, msg.length());
        exchange.getResponseBody().write(msg.getBytes());
        exchange.getResponseBody().close();
    }
}
