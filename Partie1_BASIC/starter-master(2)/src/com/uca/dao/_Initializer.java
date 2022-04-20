package com.uca.dao;

import java.sql.*;

public class _Initializer {

    public static void Init(){
        Connection connection = _Connector.getInstance();

        try {
            PreparedStatement statement;

//            statement = connection.prepareStatement("DROP TABLE givenGommettes;");
//            statement.executeUpdate();
//
//            statement = connection.prepareStatement("DROP TABLE students;");
//            statement.executeUpdate();
            //department = class
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS students (" +
                    "id int primary key auto_increment, " +
                    "firstname varchar(100), " +
                    "lastname varchar(100), " +
                    "department varchar(100)); ");
            statement.executeUpdate();

//            statement = connection.prepareStatement("DELETE FROM givenGommettes cascade;");
//            statement.executeUpdate();
//            statement = connection.prepareStatement("DELETE FROM students cascade;");
//            statement.executeUpdate();
//            statement = connection.prepareStatement("DELETE FROM gommettes cascade;");
//            statement.executeUpdate();

//            statement = connection.prepareStatement("DELETE FROM profs cascade;");
//            statement.executeUpdate();

//            statement = connection.prepareStatement("DROP TABLE profs;");
//            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS profs (" +
                    "id int primary key auto_increment," +
                    "firstname varchar(100)," +
                    "lastname varchar(100)," +
                    "username varchar(100)," +
                    "salt varchar(50)," +
                    "hashedPassword varchar(100));");
            statement.executeUpdate();

//            statement = connection.prepareStatement("DROP TABLE gommettes;");
//            statement.executeUpdate();
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
                    "gommetteDate varchar(20)," +
                    "foreign key (id_student) references students (id)," +
                    "foreign key (id_prof) references profs (id)," +
                    "foreign key (id_gommette) references gommettes (id));");
            statement.executeUpdate();

//            statement = connection.prepareStatement("ALTER TABLE students AUTO_INCREMENT = 1;");
//            statement.executeUpdate();
//            statement = connection.prepareStatement("ALTER TABLE gommettes AUTO_INCREMENT = 1;");
//            statement.executeUpdate();
//            statement = connection.prepareStatement("ALTER TABLE givenGommettes AUTO_INCREMENT = 1;");
//            statement.executeUpdate();

//            statement = connection.prepareStatement("ALTER TABLE profs AUTO_INCREMENT = 1;");
//            statement.executeUpdate();


        } catch (Exception e){
            System.out.println(e.toString());
            throw new RuntimeException("could not create database !");
        }
    }
}