package com.uca.dao;

import java.sql.*;

public class _Initializer {

    public static void Init(){
        Connection connection = _Connector.getInstance();

        try {
            PreparedStatement statement;

            //Init articles table
//            statement = connection.prepareStatement("DROP TABLE users;");
////            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (" +
                    "id int primary key auto_increment, " +
                    "firstname varchar(100), " +
                    "lastname varchar(100), " +
                    "prof varchar(20), " +
                    "gommette_blanche int, " +
                    "gommette_rouge int, " +
                    "gommette_verte int); ");
            statement.executeUpdate();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS students (" +
                    "id int primary key auto_increment," +
                    "firstname varchar(100)," +
                    "lastname varchar(100)," +
                    "classe varchar(100)," +
                    ")");
            statement.executeUpdate();

            //Todo Remove me !
//            statement = connection.prepareStatement("INSERT INTO users(firstname, lastname, prof, gommette_blanche, gommette_rouge, gommette_verte) VALUES(?, ?, ?, ?, ?, ?);");
//            statement.setString(1, "nomEleve");
//            statement.setString(2, "prenomEleve");
//            statement.setString(3, "Myrtille ");
//            System.out.println("Go");
//            statement.setInt(4, 2);
//            System.out.println("Go");
//            statement.setInt(5, 3);
//            System.out.println("Go");
//            statement.setInt(6, 0);
//            statement.executeUpdate();

        } catch (Exception e){
            System.out.println(e.toString());
            throw new RuntimeException("could not create database !");
        }
    }
}
