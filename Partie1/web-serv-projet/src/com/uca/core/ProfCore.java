package com.uca.core;

import com.uca.dao.ProfDAO;
import com.uca.dao.StudentDAO;
import com.uca.entity.ProfEntity;
import com.uca.entity.StudentEntity;

import java.sql.SQLException;
import java.util.ArrayList;

/*
 * ***
 * Model(s)
 * ***
 **/
public class ProfCore {

    public static ArrayList<ProfEntity> getAllUsers() {

        return new ProfDAO().getAllUsers();
    }

    public static ProfEntity create(ProfEntity obj) throws SQLException {

        return new ProfDAO().create(obj);
    }

    public static void delete(ProfEntity obj) throws SQLException {
        new ProfDAO().delete(0, null);
    }
}
