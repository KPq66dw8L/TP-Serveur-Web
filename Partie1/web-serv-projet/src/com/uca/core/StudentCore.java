package com.uca.core;

import com.uca.dao.StudentDAO;
import com.uca.entity.GivenGommettes;
import com.uca.entity.Gommette;
import com.uca.entity.StudentEntity;
import com.uca.entity.UserEntity;

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

    public static StudentEntity create(StudentEntity obj) throws SQLException {
        return new StudentDAO().create(obj);
    }

    public static void delete(int idStudent, ArrayList<Integer> gommettes_id) throws SQLException {
        new StudentDAO().delete(idStudent, gommettes_id);
    }

    public static void addGommette(GivenGommettes donneLeGommette){
//        System.out.println("addGommette dans StudentCore Action");
        new StudentDAO().addGommette(donneLeGommette);
    }
}
