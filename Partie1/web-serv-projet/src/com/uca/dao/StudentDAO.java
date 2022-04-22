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

    /*
     * Retrieve all students and all gommettes to be able to restore every student it's gommettes.
     * Return only the list of students.
     **/
    public ArrayList<StudentEntity> getAllUsers() {
        ArrayList<StudentEntity> entities = new ArrayList<>(); //arrayList of students
        ArrayList<Gommette> goms = new ArrayList<>(); //arrayList of gommettes
        int id_gommette_buffer;

        try {
            /*
             * Retrieve all students
             **/
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM students ORDER BY id ASC;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                StudentEntity entity = new StudentEntity();
                entity.setId(resultSet.getInt("id"));
                entity.setFirstName(resultSet.getString("firstname"));
                entity.setLastName(resultSet.getString("lastname"));
                entity.setGroup(resultSet.getString("department"));

//                System.out.println("getAllUser DAO 1: " + entity.getFirstName());

                entities.add(entity);
            }
            /*
             * Retrieve all gommettes
             **/
            preparedStatement = this.connect.prepareStatement("SELECT * FROM gommettes ORDER BY id ASC;");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Gommette entityGommette = new Gommette();
                entityGommette.setId(resultSet.getInt("id"));
                entityGommette.setColour(resultSet.getString("colour"));
                entityGommette.setDescription(resultSet.getString("description"));

//                System.out.println("getAllUser DAO 2: " + entityGommette.getColour());

                goms.add(entityGommette);
            }

            /*
             * Retrieve all givenGommettes
             **/
            preparedStatement = this.connect.prepareStatement("SELECT * FROM givenGommettes ORDER BY id ASC;");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                GivenGommettes entityGivenGommettes = new GivenGommettes();
                entityGivenGommettes.setId(resultSet.getInt("id"));
                entityGivenGommettes.setId_student(resultSet.getInt("id_student"));
                entityGivenGommettes.setId_prof(resultSet.getInt("id_prof"));
                id_gommette_buffer = resultSet.getInt("id_gommette");
                entityGivenGommettes.setDate(resultSet.getString("gommetteDate"));
//                System.out.println("getAllUser DAO 3.1: " + resultSet.getInt("id"));
//                System.out.println("getAllUser DAO 3.2: " + resultSet.getInt("id_student"));
//                System.out.println("getAllUser DAO 3.3: " + resultSet.getInt("id_prof"));
//                System.out.println("getAllUser DAO 3.4: " + id_gommette_buffer);
//                System.out.println("getAllUser DAO 3.5: " + resultSet.getString("gommetteDate"));

                /*
                 * Using the id_gommette_buffer (used in db to link givenGommettes and gommettes) to link the gommette to a student's givenGommet list.
                 * Because in Java code we don't use a gommette id but the Gommette obj directly, in the GivenGommete.
                 **/
                for (Gommette tmpGom : goms){
                    if (tmpGom.getId() == id_gommette_buffer){
                        entityGivenGommettes.setGommette(tmpGom);
//                        System.out.println("getAllUser DAO CHECK 1");
                    }
                }

                /*
                 * Give every student it's own GivenGommettes list
                 **/
                for (StudentEntity tmpStudent : entities){
//                    System.out.println("getAllUser DAO CHECK ULTIME: " + entityGivenGommettes.getId_student() + " & " + tmpStudent.getId());
//                    System.out.println(entityGivenGommettes.getId_student() == tmpStudent.getId());
                    if (entityGivenGommettes.getId_student() == tmpStudent.getId()){
                        tmpStudent.addGommete(entityGivenGommettes);
//                        System.out.println("getAllUser DAO CHECK 2");
                    }
                }

                /*
                 * Feed the global GivenGommetes list, containing every given gommettes.
                 **/
                StudentEntity.everyGommettes.add(entityGivenGommettes);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entities;
    }

    /*
     * Handle the creation of a new student in the db.
     **/
    @Override
    public StudentEntity create(StudentEntity obj) throws SQLException {
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
    public void delete(int idStudent, ArrayList<Integer> gommettes_id) throws SQLException {
        try {
            PreparedStatement statement;
            statement = this.connect.prepareStatement("DELETE FROM givenGommettes WHERE id_student=?"); // ou CASCADE?
            statement.setInt(1, idStudent);
            statement.executeUpdate();

            for (int id : gommettes_id){
                statement = this.connect.prepareStatement("DELETE FROM gommettes WHERE id=?");
                statement.setInt(1, id);
                statement.executeUpdate();
            }

            statement = this.connect.prepareStatement("DELETE FROM students WHERE id=?");
            statement.setInt(1, idStudent);
            statement.executeUpdate();
            System.out.println("Student deleted from db.");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /*
     * Handle the addition of gommette to a student.
     **/
    public void addGommette(GivenGommettes addedGommette){
        try {
            /*
             * Create a new gommette
             **/
            PreparedStatement statement;
            statement = this.connect.prepareStatement("INSERT INTO gommettes(colour, description) VALUES(?, ?);");
            statement.setString(1, addedGommette.getGommette().getColour());
            statement.setString(2, addedGommette.getGommette().getDescription());
            statement.executeUpdate();

            /*
             * Get the last gommette id (the one we just created). We need to retrieve it because the id is created in the db.
             * But we need it for the creation of the new givenGommetes.
             **/
            statement = this.connect.prepareStatement("SELECT * FROM gommettes WHERE id=(SELECT MAX(id) FROM gommettes);");
            ResultSet resultSet = statement.executeQuery();
            int id_gommette = 0;
            // Even though the request only returns one result, we need the following while loop
            while (resultSet.next()){
                try {
                    id_gommette = resultSet.getInt("id");
//                    System.out.println("id gommette: " + id_gommette);
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            /*
             * Create the new givenGommettes in the db
             **/
            statement = this.connect.prepareStatement("INSERT INTO givenGommettes(id_student, id_prof, id_gommette, gommetteDate) VALUES(?, ?, ?, ?);");
//            System.out.println("addGommete DAO 1: " + addedGommette.getId_student());
//            System.out.println("addGommete DAO 2: " + addedGommette.getId_prof());
//            System.out.println("addGommete DAO 3: " + id_gommette);
//            System.out.println("addGommete DAO 4: " + addedGommette.getDate());

            statement.setInt(1, addedGommette.getId_student());
            statement.setInt(2, addedGommette.getId_prof());
            statement.setInt(3, id_gommette);
            statement.setString(4, addedGommette.getDate());
            statement.executeUpdate();
//            System.out.println("addGommette dans StudentDAO ACTION !!");

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void modifyGommette(int gommetteId, String newColour, String newDescription){

        try {
            PreparedStatement statement;
            statement = this.connect.prepareStatement("UPDATE gommettes SET colour=?, description=? WHERE id=?;");
            statement.setString(1, newColour);
            statement.setString(2, newDescription);
            statement.setInt(3, gommetteId);
            statement.executeUpdate();
        } catch (SQLException e){
            //e.printStackTrace();
            System.out.println(gommetteId + " " + newColour + " " + newDescription);
        }
    }

    public void deleteGommette(int idGommette){
        try {
            PreparedStatement statement;
            statement = this.connect.prepareStatement("DELETE FROM givenGommettes WHERE id_gommette=?");
            statement.setInt(1, idGommette);
            statement.executeUpdate();

            statement = this.connect.prepareStatement("DELETE FROM gommettes WHERE id=?");
            statement.setInt(1, idGommette);
            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
