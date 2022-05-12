package org.sherlock.netty.server.autorization;

import java.sql.*;

public class SQLHandler {
    private static Connection connection; //для подключения к БД
    private static PreparedStatement psGetNickName;
    private static PreparedStatement psRegistration;
    private static PreparedStatement psCheckLogin;

    public static boolean connect(){
        try{
            Class.forName("org.sqlite.JDBC"); //позволяет по имени класса загружать его в память
            connection = DriverManager.getConnection("jdbc:sqLite:NetworkStorageGB.db"); //url или путь в зависимости от типа базы
            prepareAllStatements();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private static void prepareAllStatements() throws SQLException{
        psGetNickName = connection.prepareStatement("SELECT login FROM users WHERE login = ? AND password = ?;");
        psRegistration = connection.prepareStatement("INSERT INTO users (login, password) VALUES ( ? , ?);");
        psCheckLogin = connection.prepareStatement("SELECT login FROM users WHERE login = ?;");
    }

    public static Boolean isCurrentLogin(String login, String password){
        boolean res = false;
        try{
            psGetNickName.setString(1,login);
            psGetNickName.setString(2,password);
            ResultSet rs = psGetNickName.executeQuery();
            if(rs.next()){
                res = true;
            }
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public static boolean registration(String login, String password){
        try{
            psRegistration.setString(1, login);
            psRegistration.setString(2, password);
            psRegistration.executeUpdate();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static void disconnect(){
        try {
            psRegistration.close();
            psGetNickName.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
