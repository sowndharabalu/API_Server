package service;

import model.User;
import repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepo;
    public UserService(UserRepository userRepository) {
       userRepo = userRepository;
    }
    public User createUser(User user) {
        if (user.name() == null || user.email() == null) {
            throw new IllegalArgumentException("User's name or email cannot be null");
        }
        if (userRepo.existsByemail(user.email())) {
            throw new IllegalArgumentException("User with email " + user.email() + " already exists");
        }
        return userRepo.save(user);
    }

    public User updateUser(User user) {
        if (user.name() == null || user.email() == null) {
            throw new IllegalArgumentException("User's name or email cannot be null");
        }
        return userRepo.save(user);
    }

    public Optional<User> getUserById(int id) {
        return userRepo.findById(id);
    }

    public boolean deleteUser(int id) {
        return userRepo.delete(id);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
