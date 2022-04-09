package com.uca;

import com.uca.dao.StudentDAO;
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
            return StudentGUI.getAllUsers();
        });

        get("/usersTest", (req, res) -> {
            return StudentGUI.getAllUsers(req);
        });

        get("/register", (req, res) -> {
            return ProfGUI.getAllUsers();
        });

        get("/login", (req, res) -> {
            return ProfGUI.loginPage();
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


        post("/users", (req, res) -> {

            String firstname, lastname, group;
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp")); // ne sait pas ce que ce truc fait yet, mais fonctionne pas sans

            // on recup les infos du formulaire
            firstname = req.queryParams("firstname");
            lastname = req.queryParams("lastname");
            group = req.queryParams("group");

            res.redirect("/users");
            return StudentGUI.create(firstname, lastname, group);
        });

        post("/register", (req, res) -> {

            String firstname, lastname, username, newPassword;
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp")); // ne sait pas ce que ce truc fait yet, mais fonctionne pas sans

            // try?
            firstname = req.queryParams("firstname");
            lastname = req.queryParams("lastname");
            username = req.queryParams("username");
            newPassword = req.queryParams("password");

            return ProfGUI.create(firstname, lastname, username, newPassword);
        });

        post("/login", (req, res) -> {

            String firstname, lastname, username, newPassword;
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp")); // ne sait pas ce que ce truc fait yet, mais fonctionne pas sans

            username = req.queryParams("username");
            newPassword = req.queryParams("password");

            return ProfGUI.login(username, newPassword, res); //route ?
        });

        /*
         * ***
         * L'URL ressemblera a: localhost:8081/users/delete?firstname=julien&lastname=herbaux
         * ***
         **/
        delete("/users/delete", (req, res) -> {
            String firstname = req.queryParams("firstname");
            String lastname = req.queryParams("lastname");

            return StudentGUI.delete(firstname, lastname);
        });
    }
}