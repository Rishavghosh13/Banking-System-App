package BankingManagement;

import java.util.*;
import java.sql.*;

public class Account_Manager {

    private final Connection con;
    private final Scanner sc;

    public Account_Manager(Connection con,Scanner sc)
    {
        this.con=con;
        this.sc=sc;

    }

    public void credit_money()
    {
        try {
            System.out.println("Enter account number");
            long acc_number = sc.nextLong();
            sc.nextLine();
            if (account_exist(acc_number)) {
                System.out.println("Enter the amount you want to credit");
                double amt = sc.nextDouble();
                sc.nextLine();
                System.out.println("Enter security pin");
                String pin = sc.nextLine();
                if(check_password(pin,acc_number))
                {
                    con.setAutoCommit(false);
                    String credit_query="UPDATE accounts SET balance = balance+? WHERE account_number=?";
                    PreparedStatement ps_credit=con.prepareStatement(credit_query);
                    ps_credit.setDouble(1,amt);
                    ps_credit.setLong(2,acc_number);
                    ps_credit.executeUpdate();
                    con.commit();
                    System.out.println("Amount Credited Successfully");
                }
                else {
                    System.out.println("Wrong password");
                    con.rollback();
                }
            }
            else {
                System.out.println("Wrong account number");
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void debit_money()
    {
        try {
            System.out.println("Enter account number");
            long acc_number = sc.nextLong();
            sc.nextLine();
            if (account_exist(acc_number)) {
                System.out.println("Enter the amount you want to debit");
                double amt = sc.nextDouble();
                sc.nextLine();
                System.out.println("Enter security pin");
                String pin = sc.nextLine();
                if(check_password(pin,acc_number))
                {
                    con.setAutoCommit(false);
                    String debit_query="UPDATE accounts SET balance = balance-? WHERE account_number=?";
                    PreparedStatement ps_credit=con.prepareStatement(debit_query);
                    ps_credit.setDouble(1,amt);
                    ps_credit.setLong(2,acc_number);
                    ps_credit.executeUpdate();
                    con.commit();
                    System.out.println("Amount Debited Successfully");
                }
                else {
                    System.out.println("Wrong password");
                    con.rollback();
                }
            }
            else {
                System.out.println("Wrong account number");
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


    public void transfer_money()
    {
        try {

            System.out.println("Enter account number");
            long acc_number=sc.nextLong();
            sc.nextLine();
            if(account_exist(acc_number))
            {
                System.out.println("Enter the amount you want to send");
                double amt=sc.nextDouble();
                sc.nextLine();
                System.out.println("Enter the reciever's account number ");
                long reciever =sc.nextLong();
                sc.nextLine();
                if(account_exist(reciever)) {
                    System.out.println("Enter security pin");
                    String pin = sc.nextLine();
                    if(check_password(pin,acc_number))
                    {
                        con.setAutoCommit(false);
                        String credit_query="UPDATE accounts SET balance = balance+? WHERE account_number=?";
                        PreparedStatement ps_credit=con.prepareStatement(credit_query);
                        ps_credit.setDouble(1,amt);
                        ps_credit.setLong(2,reciever);
                        ps_credit.executeUpdate();

                        String debit_query="UPDATE accounts SET balance = balance-? WHERE account_number=?";
                        PreparedStatement ps_debit=con.prepareStatement(debit_query);
                        ps_debit.setDouble(1,amt);
                        ps_debit.setLong(2,acc_number);
                        ps_debit.executeUpdate();
                        con.commit();

                    }
                }
                else {
                    System.out.println("Account doesnt exist");
                    con.rollback();
                }
            }
            else {
                System.out.println("Wrong account number");
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public boolean account_exist(long number)
    {
        try {
            String query = "SELECT account_number FROM accounts WHERE account_number=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1,number);
            ResultSet rs=ps.executeQuery();
            if(rs.next())
                return true;
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean check_password(String pass,long num)
    {
        try {
            String query = "SELECT security_pin FROM accounts WHERE account_number=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1,num);
            ResultSet rs=ps.executeQuery();
            if(rs.next()) {
                if((rs.getString("security_pin").equals(pass)))
                    return true;
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void check_balance()
    {
       try{
           System.out.println("Enter account number");
           long acc_number=sc.nextLong();
           sc.nextLine();
           if(account_exist(acc_number))
           {
               System.out.println("Enter security pin");
               String pin = sc.nextLine();
               if(check_password(pin,acc_number))
               {
                   String query="SELECT balance FROM accounts WHERE account_number=?";
                   PreparedStatement ps=con.prepareStatement(query);
                   ps.setLong(1,acc_number);
                   ResultSet rs=ps.executeQuery();
                   if(rs.next())
                       System.out.println("Balance = \n"+rs.getDouble("balance"));
               }
               else {
                   System.out.println("WRONG PASSWORD");
               }

           }
           else {
               System.out.println("Account doesnt exist");
           }

       }catch (SQLException e)
       {
           System.out.println(e.getMessage());
       }
    }
}
