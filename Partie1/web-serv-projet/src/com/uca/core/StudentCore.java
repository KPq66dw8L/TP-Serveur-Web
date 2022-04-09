package com.uca.core;

import com.uca.dao.StudentDAO;
import com.uca.entity.StudentEntity;
import com.uca.entity.UserEntity;

import java.sql.SQLException;
import java.util.ArrayList;

/*
 * ***
 * Model
 * ***
 **/
public class StudentCore {

    public static ArrayList<StudentEntity> getAllUsers() {
        return new StudentDAO().getAllUsers();
    }

    public static StudentEntity create(StudentEntity obj) throws SQLException {
        return new StudentDAO().create(obj);
    }

    public static void delete(StudentEntity obj) throws SQLException {
        new StudentDAO().delete(obj);
    }
}
