package org.example;
import java.sql.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        connect();
        createNewUsersTable();
        createNewBooksTable();
        insertUserData("Uzair", 20);
        insertUserData("Ahmed", 18);
        printOutData();
        deleteById(1);
    }

    private static void createNewBooksTable() {
        String url = "jdbc:sqlite:test.db";
        String sql = "CREATE TABLE IF NOT EXISTS books(" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT UNIQUE," +
                "author TEXT," +
                "user_id INTEGER," +
                "FOREIGN KEY (user_id)" +
                "   REFERENCES users(id)" +
                "   ON DELETE CASCADE" +
                ");";

    }

    private static void deleteById(int i) {
        String url = "jdbc:sqlite:test.db";
        String sql = "DELETE FROM users WHERE id = ?";
        try(Connection conn = DriverManager.getConnection(url);
        PreparedStatement stmt = conn.prepareStatement(sql)){
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
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            while(rs.next()){
                System.out.println(rs.getString("name") + " | " + rs.getInt("age"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertUserData(String name, int age) {
        String url = "jdbc:sqlite:test.db";
        String sql = "INSERT INTO users(name, age) VALUES(?, ?)";
        try(Connection conn = DriverManager.getConnection(url);
        java.sql.PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.executeUpdate();
            System.out.println("Added");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createNewUsersTable() {

        String url = "jdbc:sqlite:test.db";
        String sql = """
        CREATE TABLE IF  NOT EXISTS users (
        id INTEGER PRIMARY KEY,
        name TEXT,
        age INTEGER
        );
        """;
        try(Connection conn = DriverManager.getConnection(url);
        java.sql.Statement stmt = conn.createStatement()){
            stmt.execute(sql);
            System.out.println("table created");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void connect() {
        String url = "jdbc:sqlite:test.db";
        try(Connection conn = DriverManager.getConnection(url)){
            conn.createStatement().execute("PRAGMA foreign_keys = ON");
            if(conn != null){
                System.out.println("Connected");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}