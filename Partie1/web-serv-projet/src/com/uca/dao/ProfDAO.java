package com.uca.dao;

import com.uca.entity.ProfEntity;
import com.uca.entity.StudentEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * ***
 * DAO
 * ***
 **/
public class ProfDAO extends _Generic<ProfEntity> {

    /*
     * Retrieve the list of all profs from the db.
     **/
    public ArrayList<ProfEntity> getAllUsers() {
        ArrayList<ProfEntity> entities = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM profs ORDER BY id ASC;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ProfEntity entity = new ProfEntity();

                entity.setId(resultSet.getInt("id"));
                entity.setFirstName(resultSet.getString("firstname"));
                entity.setLastName(resultSet.getString("lastname"));
                entity.setUsername(resultSet.getString("username"));
                entity.setSalt(resultSet.getString("salt"));
                entity.setHashedPassword(resultSet.getString("hashedPassword"));


                entities.add(entity);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entities;
    }

    /*
     * Create a new prof in the db.
     **/
    @Override
    public ProfEntity create(ProfEntity obj) throws SQLException {
        //TODO !
        try {
            PreparedStatement statement;
            statement = this.connect.prepareStatement("INSERT INTO profs(firstname, lastname, username, salt, hashedPassword) VALUES(?, ?, ?, ? , ?);");
            statement.setString(1, obj.getFirstName());
            statement.setString(2, obj.getLastName());
            statement.setString(3, obj.getUsername());
            statement.setString(4, obj.getSalt());
            statement.setString(5, obj.getHashedPassword());
            statement.executeUpdate();

            return obj;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(int id, ArrayList<Integer> gommettes_id) throws SQLException {

    }

}
