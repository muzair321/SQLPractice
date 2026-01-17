package org.example;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        connect();
        createNewTable();
        insertData("Uzair", 20);
        insertData("Ahmed", 18);
    }

    private static void insertData(String name, int age) {
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

    private static void createNewTable() {

        String url = "jdbc:sqlite:test.db";
        String sql = """
        CREATE TABLE IF  NOT EXISTS users (
        id INTEGER PRIMARY KEY,
        name TEXT NOT NULL,
        age INTEGER NOT NULL
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
            if(conn != null){
                System.out.println("Connected");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}