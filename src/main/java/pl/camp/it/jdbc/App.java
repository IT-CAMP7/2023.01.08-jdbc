package pl.camp.it.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class App {
    public static Connection connection = null;
    public static void main(String[] args) {
        connect();
        User user = new User(3, "asdfassdf",
                "asdffffff", "sdfgsdfgsdfg", "sdfgsdfgsdfgsdfg");
        //saveUser2(user);
        //updateUser(user);
        deleteUser(3);
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
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getSurname());
            ps.executeUpdate();
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
        return Optional.empty();
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>();
    }
}
