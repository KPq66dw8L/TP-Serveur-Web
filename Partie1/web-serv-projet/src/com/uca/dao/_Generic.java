package com.uca.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class _Generic<T> {

    public Connection connect = _Connector.getInstance();

    /**
     * Permet de créer une entrée dans la base de données
     * par rapport à un objet
     * @param obj
     */
    public abstract T create(T obj) throws SQLException;

    /**
     * Permet la suppression d'une entrée de la base
     * @param id
     * @param gommettes_id
     */
    public abstract void delete(int id, ArrayList<Integer> gommettes_id) throws SQLException;

}
