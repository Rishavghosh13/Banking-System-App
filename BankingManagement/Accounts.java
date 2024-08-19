package BankingManagement;

import java.sql.*;
import java.util.*;
public class Accounts {

    private final Connection con;
    private final Scanner sc;

    public Accounts(Connection con,Scanner sc)
    {
        this.con=con;
        this.sc=sc;
    }

    public void open_account()
    {
        while(true)
        {
            System.out.println("Enter \n1.OPEN ACCOUNT \n2.EXIT");
            int c=sc.nextInt();
            sc.nextLine();
            switch (c)
            {
                case 1:
                {
                    get_account_number();
                    break;
                }
                case 2:
                {
                    System.out.println("Thank you for using our system");
                    System.exit(0);
                }
                default:
                {
                    System.out.println("Wrong choice");
                    break;
                }
            }
        }
    }

    public boolean account_exist(String email,String source)
    {
        try {
            String query = "SELECT email FROM " + source + " WHERE email = ?;";
            PreparedStatement ps= con.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs= ps.executeQuery();

            if(rs.next())
            {
                    return true;
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    public void get_account_number()
    {
        System.out.println("Enter email");
        String email=sc.nextLine();
        if(account_exist(email,"accounts") && account_exist(email,"user"))
        {
            //ekhane then tor account manager e redirect hoye jabe;
           Account_Manager ac=new Account_Manager(con,sc);
           while(true)
           {
               System.out.println("\nEnter \n1.CREDIT MONEY \n2.DEBIT MONEY \n3.TRANSFER MONEY \n4.CHECK BALANCE \n5.EXIT");
               int c=sc.nextInt();
               sc.nextLine();
               switch (c)
               {
                   case 1:
                   {
                       ac.credit_money();
                       break;
                   }
                   case 2:
                   {
                       ac.debit_money();
                       break;
                   }
                   case 3:
                   {
                       ac.transfer_money();
                       break;
                   }
                   case 4:
                   {
                     ac.check_balance();
                     break;
                   }
                   case 5:
                   {
                       System.out.println("THANK YOU FOR USING OUR SYSTEM");
                       System.exit(0);
                       break;
                   }
                   default:
                   {
                       System.out.println("WRONG CHOICE");
                       break;
                   }
               }

           }
        }
        else if(account_exist(email,"user") && !account_exist(email,"accounts"))
        {
            try{
                System.out.println("Enter full name");
                String name = sc.nextLine();
                System.out.println("Enter balance");
                double balance = sc.nextDouble();
                sc.nextLine();
                System.out.println("Enter security pin of 4 digits");
                String pin = sc.nextLine();
                long number = generate_account_number();
                String query = "INSERT INTO accounts(account_number,full_name,email,balance,security_pin)" +
                        " VALUES(?,?,?,?,?);";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setLong(1,number);
                ps.setString(2,name);
                ps.setString(3,email);
                ps.setDouble(4,balance);
                ps.setString(5,pin);
                int rows=ps.executeUpdate();

                if(rows>0) {
                    System.out.println("Entry successful");
                    System.out.println("ACCOUNT NUMBER = "+number);
                }
                else
                    System.out.println("Entry unsucessful");
            }catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
        else {
            System.out.println("Enter the email with which you registered");
        }
    }

    public long generate_account_number()
    {
        try{
            String query="SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1;";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs= ps.executeQuery();
            if(rs.next())
                return rs.getLong("account_number")+1;

        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return 100000;
    }
}
