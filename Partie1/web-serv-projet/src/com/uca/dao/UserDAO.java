package com.uca.dao;

import com.uca.entity.UserEntity;

import java.sql.*;
import java.util.ArrayList;

/*
 * ***
 * DAO
 * ***
 **/
public class UserDAO extends _Generic<UserEntity> {

    public ArrayList<UserEntity> getAllUsers() {
        ArrayList<UserEntity> entities = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM users ORDER BY id ASC;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UserEntity entity = new UserEntity();
                entity.setId(resultSet.getInt("id"));
                entity.setFirstName(resultSet.getString("firstname"));
                entity.setLastName(resultSet.getString("lastname"));
                entity.setProf(resultSet.getString("prof"));
                entity.setBlanche(resultSet.getInt("gommette_blanche"));
                entity.setRouge(resultSet.getInt("gommette_rouge"));
                entity.setVerte(resultSet.getInt("gommette_verte"));

                entities.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entities;
    }

    @Override
    public UserEntity create(UserEntity obj) throws SQLException {
        //TODO !
        try {
            PreparedStatement statement;
            statement = this.connect.prepareStatement("INSERT INTO users(firstname, lastname, prof, gommette_blanche, gommette_rouge, gommette_verte) VALUES(?, ?, ?, ?, ?, ?);");
            statement.setString(1, obj.getFirstName());
            statement.setString(2, obj.getLastName());
            statement.setString(3, obj.getProf());
            statement.setInt(4, obj.getBlanche());
            statement.setInt(5, obj.getRouge());
            statement.setInt(6, obj.getVerte());
            statement.executeUpdate();
            //System.out.println("Etape 4");
            return obj;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(UserEntity obj) throws SQLException {

    }

    @Override
    public void delete(String firstname, String lastname) throws SQLException {
        //TODO !
        try {
            PreparedStatement statement;
            statement = this.connect.prepareStatement("DELETE FROM users WHERE firstname=? AND lastname=?;");
            statement.setString(1, firstname);
            statement.setString(2, lastname);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
