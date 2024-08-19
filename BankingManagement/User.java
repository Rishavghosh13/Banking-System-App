package BankingManagement;



import  java.sql.*;
import java.util.*;

public class User {

    private final Connection con;
    private  final Scanner sc;

    public User(Connection con,Scanner sc)
    {
        this.con=con;
        this.sc=sc;
    }



    public void register()
    {

        String query="INSERT INTO user(full_name,email,password) VALUES(?,?,?);";

        try {
            System.out.println("Enter your name");
            String name = sc.nextLine();
            System.out.println("Email");
            String email = sc.nextLine();
            System.out.println("Enter your password");
            String password = sc.nextLine();

            if (!user_exist(email)) {

                PreparedStatement ps=con.prepareStatement(query);
                ps.setString(1,name);
                ps.setString(2,email);
                ps.setString(3,password);

                ps.executeUpdate();
            }
            else{
                System.out.println("USER ALREADY EXIST");
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public boolean user_exist(String email) {
        try {
            String query = "SELECT email FROM user;";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                if((rs.getString("email")).equals(email))
                    return true;
            }
            return false;
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean login()
    {
        System.out.println("Enter email id");
        String email=sc.nextLine();
        System.out.println("Enter password");
        String password=sc.nextLine();
        if(user_exist(email))
        {
            try {
                String query = "Select password from user where email=? ;";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if(rs.next())
                {
                    if ((rs.getString("password")).equals(password)) {
                        System.out.println("CORRECT PASSWORD");
                        Accounts accounts= new Accounts(con,sc);
                        accounts.open_account();

                    }
                    else {
                        System.out.println("INCORRECT PASSWORD");
                        System.out.println("\nLogin failed");
                    }
                }
            }catch (SQLException e)
            {
                System.out.println(e.getMessage());
                return false;
            }

        }
        else
            System.out.println("EMAIL DOESNOT EXIST");
        return false;
    }
}
