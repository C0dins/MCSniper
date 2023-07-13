package net.mcsniper.database;

import net.mcsniper.Main;
import net.mcsniper.objects.Account;
import net.mcsniper.utils.LogType;
import net.mcsniper.utils.Logger;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private Connection con = null;
    private String connectionUrl, username, password;

    public DatabaseManager(String uri, String username, String password) {
        this.connectionUrl = uri;
        this.username = username;
        this.password = password;

        try{
            con = DriverManager.getConnection(connectionUrl, username, password);
        } catch (SQLException e){
           e.printStackTrace();
        }
    }

    public void addAccount(String email, String password){
        String query = "INSERT INTO Accounts (email, password, microsoft) VALUES (?, ?, 1)";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteAccount(String email){
        String query = "DELETE FROM Accounts WHERE email='" + email + "'";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void resumeAccount(String email){
        String query = "UPDATE Accounts SET reserved = 0 WHERE email='" + email + "'";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void reserveAccount(String email){
        String query = "UPDATE Accounts SET reserved = 1 WHERE email='" + email + "'";
        try{
            PreparedStatement ps = con.prepareStatement(query);
           ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void successAccount(String email){
        String query = "UPDATE Accounts SET sniped = 1 WHERE email='" + email + "'";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addSnipe(String name, long droptime){
        String query = "INSERT INTO Snipes (name, droptime, card, queued) VALUES (?, ?, 0, 1)";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setLong(2, droptime);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removeSnipe(String name){
        String query = "DELETE FROM Snipes WHERE name ='" + name + "'";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void failSnipe(String name){
        String query = "UPDATE Snipes SET success= 0, queued= 0 WHERE name = '" + name + "'";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void successfulSnipe(String name){
        String query = "UPDATE Snipes SET success= 1, queued= 0 WHERE name = '" + name + "'";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setToken(String email, String token){
        String query = "UPDATE Accounts SET token = '" + token + "' WHERE email = '" + email + "'";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTotalAccountsMS(){
        String query = "SELECT * FROM Accounts WHERE microsoft = '1' AND reserved = '0' AND sniped = '0'";
        int total = 0;
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                total++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public int getTotalSnipedNames(){
        String query = "SELECT * FROM Snipes WHERE success=1";
        int total = 0;
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                total++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
    public int getTotalFailedSnipes(){
        String query = "SELECT * FROM Snipes WHERE success=0 AND queued= 0";
        int total = 0;
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                total++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
    public int getQueuedNames(){
        String query = "SELECT * FROM Snipes WHERE queued= 1";
        int total = 0;
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                total++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
    public int getSnipedCode(String name){
        String query = "SELECT * FROM Snipes WHERE name = '" + name + "'";
        try{
            Statement ps = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = ps.executeQuery(query);
            while (rs.next()){
                return rs.getInt(4);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public String getToken(String email){
        String query = "SELECT * FROM Accounts WHERE email = '" + email + "'";
        try{
            Statement ps = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = ps.executeQuery(query);
            if(rs.next()) {
                return rs.getString(3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public ArrayList<String> getReservedAccounts(){
        ArrayList<String> emails = new ArrayList<String>();
        String query = "SELECT * FROM Accounts WHERE reserved = 1 AND sniped = 0";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                emails.add(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emails;
    }
    public ArrayList<String> getSnipedAccounts(){
        ArrayList<String> emails = new ArrayList<String>();
        String query = "SELECT * FROM Accounts WHERE sniped = 1";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                emails.add(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emails;
    }
    public ArrayList<Account> getAccountsMS(){
        ArrayList<Account> accounts = new ArrayList<Account>();
        String query = "SELECT * FROM Accounts WHERE microsoft = '1' AND reserved = '0' AND sniped = '0'";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                Account account = new Account(resultSet.getString("email"), resultSet.getString("password"), false, false);
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;

    }
    public ArrayList<String> getAllTokens(){
        ArrayList<String> tokens = new ArrayList<String>();
        String query = "SELECT * FROM Accounts";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                tokens.add(resultSet.getString("token"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tokens;
    }
    public ArrayList<String> getQueuedList(){
        ArrayList<String> names = new ArrayList<String>();
        String query = "SELECT * FROM Snipes WHERE queued= 1";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                names.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return names;
    }

    public void close() throws SQLException {
        if(!con.isClosed()){
            Logger.log(LogType.SUCCESS, "Successfully Closed database");
        } else {
            Logger.log(LogType.ERROR, "Error while closing database conn.");
        }
    }
}
