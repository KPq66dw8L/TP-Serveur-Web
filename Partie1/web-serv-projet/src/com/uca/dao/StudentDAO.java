package com.uca.dao;

import com.uca.entity.GivenGommettes;
import com.uca.entity.Gommette;
import com.uca.entity.StudentEntity;
import com.uca.entity.UserEntity;

import java.sql.*;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/*
 * ***
 * DAO
 * ***
 **/
public class StudentDAO extends _Generic<StudentEntity> {

    public ArrayList<StudentEntity> getAllUsers() {
        ArrayList<StudentEntity> entities = new ArrayList<>();
        ArrayList<Gommette> goms = new ArrayList<>();
        int id_gommette_buffer;
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM students ORDER BY id ASC;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                StudentEntity entity = new StudentEntity();
                entity.setId(resultSet.getInt("id"));
                entity.setFirstName(resultSet.getString("firstname"));
                entity.setLastName(resultSet.getString("lastname"));
                entity.setGroup(resultSet.getString("department"));

                System.out.println("getAllUser DAO 1: " + entity.getFirstName());

                entities.add(entity);
            }
            preparedStatement = this.connect.prepareStatement("SELECT * FROM gommettes ORDER BY id ASC;");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Gommette entityGommette = new Gommette();
                entityGommette.setId(resultSet.getInt("id"));
                entityGommette.setColour(resultSet.getString("colour"));
                entityGommette.setDescription(resultSet.getString("description"));

                System.out.println("getAllUser DAO 2: " + entityGommette.getColour());

                goms.add(entityGommette);
            }

            preparedStatement = this.connect.prepareStatement("SELECT * FROM givenGommettes ORDER BY id ASC;");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                GivenGommettes entityGivenGommettes = new GivenGommettes();
                entityGivenGommettes.setId(resultSet.getInt("id"));
                entityGivenGommettes.setId_student(resultSet.getInt("id_student"));
                entityGivenGommettes.setId_prof(resultSet.getInt("id_prof"));
                id_gommette_buffer = resultSet.getInt("id_gommette");
                entityGivenGommettes.setDate(resultSet.getString("gommetteDate"));
                System.out.println("getAllUser DAO 3.1: " + resultSet.getInt("id"));
                System.out.println("getAllUser DAO 3.2: " + resultSet.getInt("id_student"));
                System.out.println("getAllUser DAO 3.3: " + resultSet.getInt("id_prof"));
                System.out.println("getAllUser DAO 3.4: " + id_gommette_buffer);
                System.out.println("getAllUser DAO 3.5: " + resultSet.getString("gommetteDate"));

                for (Gommette tmpGom : goms){
                    if (tmpGom.getId() == id_gommette_buffer){
                        entityGivenGommettes.setGommette(tmpGom);
                        System.out.println("getAllUser DAO CHECK 1");
                    }
                }

                for (StudentEntity tmpStudent : entities){
                    System.out.println("getAllUser DAO CHECK ULTIME: " + entityGivenGommettes.getId_student() + " & " + tmpStudent.getId());
                    System.out.println(entityGivenGommettes.getId_student() == tmpStudent.getId());
                    if (entityGivenGommettes.getId_student() == tmpStudent.getId()){
                        tmpStudent.addGommete(entityGivenGommettes);
                        System.out.println("getAllUser DAO CHECK 2");
                    }
                }

                StudentEntity.everyGommettes.add(entityGivenGommettes);
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

    // 'cascade' for delete
    @Override
    public void delete(StudentEntity obj) throws SQLException {

    }

    public void addGommette(GivenGommettes addedGommette){
        try {
            PreparedStatement statement;
            statement = this.connect.prepareStatement("INSERT INTO gommettes(colour, description) VALUES(?, ?);");
            statement.setString(1, addedGommette.getGommette().getColour());
            statement.setString(2, addedGommette.getGommette().getDescription());
            statement.executeUpdate();

            //            statement = this.connect.prepareStatement("SELECT MAX(id) FROM gommettes;");
            statement = this.connect.prepareStatement("SELECT * FROM gommettes WHERE id=(SELECT MAX(id) FROM gommettes);");
            ResultSet resultSet = statement.executeQuery();
            int id_gommette = 0;

            while (resultSet.next()){
                try {
//            System.out.println("Oeeeeee" + resultSet.getInt(1));
//            System.out.println("ID=====: " + resultSet.getInt("id"));
                    id_gommette = resultSet.getInt("id");
                    System.out.println("id gommette: " + id_gommette);
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            statement = this.connect.prepareStatement("INSERT INTO givenGommettes(id_student, id_prof, id_gommette, gommetteDate) VALUES(?, ?, ?, ?);");
            System.out.println("addGommete DAO 1: " + addedGommette.getId_student());
            System.out.println("addGommete DAO 2: " + addedGommette.getId_prof());
            System.out.println("addGommete DAO 3: " + id_gommette);
            System.out.println("addGommete DAO 4: " + addedGommette.getDate());

            statement.setInt(1, addedGommette.getId_student());
            statement.setInt(2, addedGommette.getId_prof());
            statement.setInt(3, id_gommette);
            statement.setString(4, addedGommette.getDate());
            statement.executeUpdate();
            System.out.println("addGommette dans StudentDAO ACTION !!");
            //return obj;
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Raté");
        }
        //return null;
    }
}
