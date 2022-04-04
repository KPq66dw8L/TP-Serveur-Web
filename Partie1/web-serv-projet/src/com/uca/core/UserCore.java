package com.uca.core;

import com.uca.dao.UserDAO;
import com.uca.entity.UserEntity;

import java.sql.SQLException;
import java.util.ArrayList;

/*
 * ***
 * Model
 * ***
 **/
public class UserCore {

    public static ArrayList<UserEntity> getAllUsers() {
        return new UserDAO().getAllUsers();
    }

    public static UserEntity create(UserEntity obj) throws SQLException {
        //System.out.println("Etape 3");
        return new UserDAO().create(obj);
    }

    public static void delete(String firstname, String lastname) throws SQLException {
        new UserDAO().delete(firstname, lastname);
    }
}
