package pl.camp.it.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class App {
    public static Connection connection = null;
    public static void main(String[] args) {
        connect();
        User user = new User(0, "zbychu",
                "zbychu123", "Zbigniew", "Mailowski");
        saveUser2(user);
        System.out.println(user);
        //updateUser(user);
        //deleteUser(3);
        Optional<User> userBox = getUserById(5);
        if(userBox.isPresent()) {
            System.out.println(userBox.get());
        } else {
            System.out.println("Nie ma takiego usera !!");
        }
        List<User> users = getAllUsers();
        System.out.println(users);
    }

    public static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test10", "root", "");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("NIe udało się podpiąć do bazy !!!");
            e.printStackTrace();
        }
    }

    public static void saveUser(User user) {
        try {
            String sql = new StringBuilder()
                    .append("INSERT INTO tuser (login, password, name, surname)")
                    .append(" VALUES ('")
                    .append(user.getLogin())
                    .append("', '")
                    .append(user.getPassword())
                    .append("', '")
                    .append(user.getName())
                    .append("', '")
                    .append(user.getSurname())
                    .append("');")
                    .toString();

            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveUser2(User user) {
        try {
            String sql = "INSERT INTO tuser (login, password, name, surname) VALUES (?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getSurname());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            user.setId(rs.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateUser(User user) {
        try {
            String sql = "UPDATE tuser SET login=?, password=?, name=?, surname=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getSurname());
            ps.setInt(5, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteUser(int id) {
        try {
            String sql = "DELETE FROM tuser WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<User> getUserById(int id) {
        try {
            String sql = "SELECT * FROM tuser WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return Optional.of(new User(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("surname")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public static List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tuser;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                result.add(new User(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("surname")));
            }
        } catch (SQLException e) {
        }
        return result;
    }
}
