package Util;

import model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtil {
    public static String usertoJson(User user) {
        return String.format("{\"id\": \"%d\",\"name\": \"%s\",\"email\": \"%s\",\"createdAt\": \"%s\"}",user.id(),user.name(),user.email(),user.createdAt().toString());
    }

    public static String extractJsonField(String json, String field) {
        String regex="\""+field+"\"\\s*:\\s*(?:\"([^\"]*)\"|(\\d+))";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(json);
        if(matcher.find()){
            if (matcher.group(1) != null) {
                return matcher.group(1);
            }else if (matcher.group(2) != null) {
                return matcher.group(2);
            }
        }
        return null;
    }

    public static User jsontoUser(String json) {
        String strId=extractJsonField(json, "id");
        Integer id=(strId!=null) ? Integer.parseInt(strId) : null;
        String name=extractJsonField(json, "name");
        String email=extractJsonField(json, "email");
        String strTime=extractJsonField(json, "createdAt");
        LocalDateTime createdAt=(strTime!=null) ? LocalDateTime.parse(strTime) : null;
        return User.create(id,name,email,createdAt);
    }

    public static String userListToJson(List<User> users) {
        StringBuilder json = new StringBuilder("[");
        for (User user : users) {
            json.append(usertoJson(user)).append(",");
        }
        json.deleteCharAt(json.length()-1).append("]");
        return json.toString();
    }
}
