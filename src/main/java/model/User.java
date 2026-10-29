package model;

import java.time.LocalDateTime;

public record User(Integer id, String name, String email, LocalDateTime createdAt) {
    public User{
        if (name == null) {
            throw new IllegalArgumentException("User's name cannot be null");
        }
        if (!email.contains("@") && !email.contains(".")) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }
    public static User create(Integer id, String name, String email, LocalDateTime createdAt) {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        return new User(id, name, email, createdAt);
    }
}
