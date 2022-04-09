package com.uca.dao;

import com.uca.entity.StudentEntity;
import com.uca.entity.UserEntity;

import java.sql.*;
import java.util.ArrayList;

/*
 * ***
 * DAO
 * ***
 **/
public class StudentDAO extends _Generic<StudentEntity> {

    public ArrayList<StudentEntity> getAllUsers() {
        ArrayList<StudentEntity> entities = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM students ORDER BY id ASC;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                StudentEntity entity = new StudentEntity();
                entity.setId(resultSet.getInt("id"));
                entity.setFirstName(resultSet.getString("firstname"));
                entity.setLastName(resultSet.getString("lastname"));
                entity.setGroup(resultSet.getString("department"));

                entities.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entities;
    }

    @Override
    public StudentEntity create(StudentEntity obj) throws SQLException {
        //TODO !
        try {
            PreparedStatement statement;
            statement = this.connect.prepareStatement("INSERT INTO students(firstname, lastname, department) VALUES(?, ?, ?);");
            statement.setString(1, obj.getFirstName());
            statement.setString(2, obj.getLastName());
            statement.setString(3, obj.getGroup());
            statement.executeUpdate();
            return obj;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(StudentEntity obj) throws SQLException {

    }
}
