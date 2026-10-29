package repository;

import model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(int id);
    List<User> findAll();
    Optional<User> findByEmail(String email);
    boolean existsByemail(String email);
    boolean delete(int id);
}
