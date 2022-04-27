package com.uca.core;

import com.uca.dao.ProfDAO;
import com.uca.dao.StudentDAO;
import com.uca.entity.GivenGommettes;
import com.uca.entity.ProfEntity;
import com.uca.entity.StudentEntity;

import java.sql.SQLException;
import java.util.ArrayList;

/*
 * ***
 * Model(s)
 * ***
 **/
public class StudentCore {

    public static ArrayList<StudentEntity> getAllUsers() {
        return new StudentDAO().getAllUsers();
    }

    public static boolean create(StudentEntity obj) throws SQLException {
        boolean check = new StudentDAO().create(obj) != null;
        return check;
    }

    public static void delete(int idStudent, ArrayList<Integer> gommettes_id) throws SQLException {
        new StudentDAO().delete(idStudent, gommettes_id);
    }

    public static void addGommette(GivenGommettes donneLeGommette){
        new StudentDAO().addGommette(donneLeGommette);
    }

    public static void modifyGommette(int gommetteId, String newColour, String newDescription){
        new StudentDAO().modifyGommette(gommetteId, newColour, newDescription);
    }

    public static void deleteGommette(int idGommette){
        new StudentDAO().deleteGommette(idGommette);
    }
}
