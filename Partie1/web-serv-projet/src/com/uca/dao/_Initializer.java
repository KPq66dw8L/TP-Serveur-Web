package com.uca.dao;

import java.sql.*;

public class _Initializer {

    public static void Init(){
        Connection connection = _Connector.getInstance();

        try {
            PreparedStatement statement;

            //Init articles table
//            statement = connection.prepareStatement("DROP TABLE users;");
//            statement.executeUpdate();
//            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (" +
//                    "id int primary key auto_increment, " +
//                    "firstname varchar(100), " +
//                    "lastname varchar(100), " +
//                    "prof varchar(20), " +
//                    "gommette_blanche int, " +
//                    "gommette_rouge int, " +
//                    "gommette_verte int); ");
//            statement.executeUpdate();

//            statement = connection.prepareStatement("DROP TABLE *;");
//            statement.executeUpdate();

            //department = class
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS students (" +
                    "id int primary key auto_increment, " +
                    "firstname varchar(100), " +
                    "lastname varchar(100), " +
                    "department varchar(100)); ");
            statement.executeUpdate();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS profs (" +
                    "id int primary key auto_increment," +
                    "firstname varchar(100)," +
                    "lastname varchar(100)," +
                    "username varchar(100)," +
                    "salt varchar(50)," +
                    "hashedPassword varchar(100));");
            statement.executeUpdate();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS gommettes (" +
                    "id int primary key auto_increment," +
                    "colour varchar(100)," +
                    "description varchar(280));");
            statement.executeUpdate();

            //SQL -> DATE - format YYYY-MM-DD
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS givenGommettes (" +
                    "id int primary key auto_increment," +
                    "id_student int," +
                    "id_prof int," +
                    "id_gommette int," +
                    "gommetteDate DATE," +
                    "foreign key (id_student) references students (id)," +
                    "foreign key (id_prof) references profs (id)," +
                    "foreign key (id_gommette) references gommettes (id));");
            statement.executeUpdate();


        } catch (Exception e){
            System.out.println(e.toString());
            throw new RuntimeException("could not create database !");
        }
    }
}
