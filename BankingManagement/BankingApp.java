package BankingManagement;

import java.util.*;
import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BankingApp {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            props.load(input);
        } catch (IOException e) {
            System.out.println("Failed to load configuration file: " + e.getMessage());
            return;
        }

        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection con = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connection established");
            Scanner sc = new Scanner(System.in);
            User user = new User(con, sc);
            Account_Manager account_manager = new Account_Manager(con, sc);

            while (true) {
                System.out.println("\nEnter your choice \n1.REGISTER \n2.LOGIN \n3.EXIT \n");
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        user.login();
                        break;
                    case 3:
                        con.close();
                        System.out.println("\nThank you for using our system");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("\nIncorrect Choice");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
