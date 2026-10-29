package Util;

import model.User;

import java.time.LocalDateTime;
import java.util.List;

public class JsonUtil {
    public static String usertoJson(User user) {
        String json = String.format("{\"id\": \"%d\",\"name\": \"%s\",\"email\": \"%s\",\"createdAt\": \"%s\"}",user.id(),user.name(),user.email(),user.createdAt().toString());
        return json;
    }

    public static String extractJsonField(String json, String field) {
        String jsonField = "\""+field+"\":";
        int index = json.indexOf(jsonField);
        if (index == -1) {
            return null;
        }
        int start = json.indexOf("\"",index+jsonField.length());
        if (start == -1) {
            return null;
        }
        int end = json.indexOf("\"",start+1);
        if (end == -1) {
            return null;
        }
        return json.substring(start+1, end);
    }

    public static User jsontoUser(String json) {
        int id=Integer.parseInt(extractJsonField(json, "id"));
        String name=extractJsonField(json, "name");
        String email=extractJsonField(json, "email");
        LocalDateTime createdAt=LocalDateTime.parse(extractJsonField(json, "createdAt"));
        return User.create(id,name,email,createdAt);
    }

    public static String userListToJson(List<User> users) {
        StringBuilder json = new StringBuilder("[");
        for (User user : users) {
            json.append(usertoJson(user)).append(",");
        }
        json = json.deleteCharAt(json.length()-1).append("]");
        return json.toString();
    }
}
