package repository;

import DBConnection.DBConnect;
import model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public User save(User user) {
        if (user.id() == null) {
            return insert(user);
        }
        return update(user);
    }

    @Override
    public Optional<User> findById(int id) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try(Connection con=DBConnect.getConnection();
        PreparedStatement ps=con.prepareStatement(query);){
            ps.setInt(1, id);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    return Optional.of(mapToUser(rs));
                }
            }
        }catch (SQLException e){
            throw new RuntimeException("User id not found: "+e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM users ORDER BY user_id";
        List<User> users = new ArrayList<>();
        try (Connection con=DBConnect.getConnection();
        PreparedStatement ps=con.prepareStatement(query);){
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()){
                    users.add(mapToUser(rs));
                }
            }
        }catch (SQLException e){
            throw new RuntimeException("Users not found: "+e.getMessage());
        }
        return users;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection con=DBConnect.getConnection();
        PreparedStatement ps=con.prepareStatement(query);){
            ps.setString(1, email);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    return Optional.of(mapToUser(rs));
                }
            }
        }catch (SQLException e){
            throw new RuntimeException("User email not found: "+e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByemail(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (Connection con=DBConnect.getConnection();
        PreparedStatement ps=con.prepareStatement(query);){
            ps.setInt(1, id);
            int count=ps.executeUpdate();
            if(count==0){
                return false;
            }
            return true;
        }catch (SQLException e){
            throw new RuntimeException("User not found: "+e.getMessage());
        }
    }

    private static User insert(User user) {
        String query = "INSERT INTO users (name, email, created_at) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps=conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, user.name());
            ps.setString(2, user.email());
            ps.setObject(3, user.createdAt());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return mapToUser(rs);
            }
        }catch(SQLException e){
            throw new RuntimeException("Unable to insert user: " + e.getMessage());
        }
    }

    private static User update(User user) {
        String query = "UPDATE users SET name = ?, email = ?, created_at = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
        PreparedStatement ps=conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, user.name());
            ps.setString(2, user.email());
            ps.setObject(3, user.createdAt());
            ps.setInt(4, user.id());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return mapToUser(rs);
            }
        }catch (SQLException e){
            throw new RuntimeException("Unable to update user: " + e.getMessage());
        }
    }

    private static User mapToUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        LocalDateTime createdAt = rs.getObject("created_at", LocalDateTime.class);
        return User.create(id, name, email, createdAt);
    }
}
