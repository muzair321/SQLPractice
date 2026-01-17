package org.example;
import java.sql.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        connect();
        createNewUsersTable();
        insertData("Uzair", 20, "Programming Fundamentals");
        insertData("Ahmed", 18, "OOP Basics");
        printOutData();
        deleteById(1);
    }

    private static void deleteById(int i) {
        String url = "jdbc:sqlite:test.db";
        String sql = "DELETE FROM users WHERE id = ?";
        try(Connection conn = DriverManager.getConnection(url)){
            conn.createStatement().execute("PRAGMA foreign_keys = ON");
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, 1);
            int rows = stmt.executeUpdate();
            if(rows > 0){
                System.out.println("User Deleted");
            }else{
                System.out.println("User Does Not Exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printOutData() {
        String url = "jdbc:sqlite:test.db";
        String sql = "SELECT name, age FROM users";
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            conn.createStatement().execute("PRAGMA foreign_keys = ON");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("name") + " | " + rs.getInt("age"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertData(String name, int age, String book) {
        String url = "jdbc:sqlite:test.db";
        String sql1 = "INSERT INTO users(name, age) VALUES(?, ?)";
        String sql2 = "INSERT INTO books(name, author, user_id) VALUES(?, ?, ?)";
        try(Connection conn = DriverManager.getConnection(url)){
            conn.setAutoCommit(false);
            conn.createStatement().execute("PRAGMA foreign_keys = ON");
            PreparedStatement stmt = conn.prepareStatement(sql1);

            PreparedStatement stmt1 = conn.prepareStatement(sql2);

            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if(!keys.next()){
                conn.rollback();
                System.out.println("Failed To Get user_id");
            }
            int userId = keys.getInt(1);
            stmt1.setString(1, book);
            stmt1.setString(2, name);
            stmt1.setInt(3, userId);
            stmt1.executeUpdate();
            conn.commit();

            System.out.println("Added");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createNewUsersTable() {

        String url = "jdbc:sqlite:test.db";
        String sql1 = """
        CREATE TABLE IF  NOT EXISTS users (
        id INTEGER PRIMARY KEY,
        name TEXT,
        age INTEGER
        );
        """;
        String sql2 = """
        CREATE TABLE IF NOT EXISTS books(
        id INTEGER PRIMARY KEY,
        name TEXT,
        author TEXT,
        user_id INTEGER,
        FOREIGN KEY (user_id)
           REFERENCES users(id)
           ON DELETE CASCADE
        );
        """;
        try(Connection conn = DriverManager.getConnection(url)){
            conn.createStatement().execute("PRAGMA foreign_keys = ON");
            java.sql.Statement stmt = conn.createStatement();
            stmt.execute(sql1);
            stmt.execute(sql2);
            System.out.println("table created");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void connect() {
        String url = "jdbc:sqlite:test.db";
        try(Connection conn = DriverManager.getConnection(url)){
            conn.createStatement().execute("PRAGMA foreign_keys = ON");
            System.out.println("Connected");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}