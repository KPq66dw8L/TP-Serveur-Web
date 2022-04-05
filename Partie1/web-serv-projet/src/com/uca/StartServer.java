package com.uca;

import com.uca.dao.UserDAO;
import com.uca.dao._Initializer;
import com.uca.entity.UserEntity;
import com.uca.gui.*;

import javax.servlet.MultipartConfigElement;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static java.lang.Integer.parseInt;
import static spark.Spark.*;

public class StartServer {

    public static void main(String[] args) {
        //Configure Spark
        staticFiles.location("/static/");
        port(8081);


        _Initializer.Init();
        /*
        * ***
        * Router
        * ***
        **/
        //Defining our routes
        get("/users", (req, res) -> {
            return UserGUI.getAllUsers();
        });


        /*
         * ***
         * L'URL ressemblera a: localhost:8081/users/add?firstname=&lastname=&prof=&blanche=&rouge=&verte=
         * ***
         **/
//        get("/users/add", (req, res) -> {
//
//            res.redirect("/users");
//            return UserGUI.create(req.queryParams("firstname"), req.queryParams("lastname"), req.queryParams("prof"), req.queryParams("blanche"), req.queryParams("rouge"), req.queryParams("verte"));
//        });

        /*
         * ***
         * Pourquoi:
         * curl -X POST -F 'firstname=test' -F 'lastname=julien' -F 'prof=Mireille' -F 'blanche=0' -F 'rouge=0' -F 'verte=0' http://localhost:8081/users
         * Ne marche pas?
         * ***
         **/
        post("/users", (req, res) -> {

            String firstname = new String(), lastname = new String(), prof = new String(), blanche = new String(), rouge = new String(), verte =new String();
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp")); // ne sait pas ce que ce truc fait yet

            /*
             * ***
             * Mettre dans ds try ou qq chosae comme ca:
             * ***
             **/
            firstname = req.queryParams("firstname");
            lastname = req.queryParams("lastname");
            prof = req.queryParams("prof");
            blanche = req.queryParams("blanche");
            rouge = req.queryParams("rouge");
            verte = req.queryParams("verte");


            res.redirect("/users");
            return UserGUI.create(firstname, lastname, prof, blanche, rouge, verte);
        });

        /*
         * ***
         * L'URL ressemblera a: localhost:8081/users/delete?firstname=julien&lastname=herbaux
         * ***
         **/
        get("/users/delete", (req, res) -> {

            res.redirect("/users");
            return UserGUI.delete(req.queryParams("firstname"), req.queryParams("lastname"));
        });
    }
}