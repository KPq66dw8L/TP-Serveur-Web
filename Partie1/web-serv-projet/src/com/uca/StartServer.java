package com.uca;

import com.uca.core.ProfCore;
import com.uca.dao._Initializer;
import com.uca.entity.ProfEntity;
import com.uca.gui.ProfGUI;
import com.uca.gui.StudentGUI;

import javax.servlet.MultipartConfigElement;
import java.util.ArrayList;

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

        /*
         * Print all students, and more features to logged in profs
         **/
        get("/users", (req, res) -> {

            /*
             * From the infos in the cookie, we check if the user exists in our db
             **/
            //need authentificate()
            ArrayList<ProfEntity> profs = ProfCore.getAllUsers();
            ProfEntity currentProf = null;
            String tmpUsername = new String();
            String tmpPassword = new String();
            String[] parts;
            try {
                parts = req.cookie("user").split("----");
                tmpUsername = parts[0];
                tmpPassword = parts[1];
            } catch (Exception e){
            }
            for (ProfEntity prof : profs){
                if (tmpUsername.equals(prof.getUsername()) && tmpPassword.equals(prof.getHashedPassword())){
                    currentProf = prof;
                    break;
                }
            }
            /*
             * If the user exists in our db and the cookie is written, then he is logged-in and he can see the students(everyone can) + add gommettes etc
             * Else he can only see the list of students
             **/
            if (currentProf != null){
                return StudentGUI.getAllUsers(true);
            } else {
                return StudentGUI.getAllUsers(false);
            }
        });

        /*
         * Get a precise student page, listing its gommettes with their description
         **/
        get("/users/:id", (req, res) -> {
            String id = req.params(":id");

            return StudentGUI.getUser(id);
        });

        /*
         * List all profs
         **/
        get("/register", (req, res) -> {
            return ProfGUI.getAllUsers();
        });

        /*
         * Print the login page
         **/
        get("/login", (req, res) -> {
            return ProfGUI.loginPage();
        });

        /*
         * Handle post request from 1st form to add a student & 2nd form to add a gommette
         **/
        post("/users", (req, res) -> {
//            System.out.println("CHECK");

            String firstname, lastname, group, gommette, description, studentID, tmpUsername = null, tmpPassword = null;
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp")); // ne sait pas ce que ce truc fait yet, mais fonctionne pas sans


            // on recup les infos du 1er formulaire
            firstname = req.queryParams("firstname");
            lastname = req.queryParams("lastname");
            group = req.queryParams("group");

            // on recup les infos du 2eme formulaire
            gommette = req.queryParams("gommette");
            description = req.queryParams("description");
            studentID = req.queryParams("studentName");
//            System.out.println(gommette + " " + description + " " + studentID);

            //need authentificate()
            ArrayList<ProfEntity> profs = ProfCore.getAllUsers();
            ProfEntity currentProf = null;
            String[] parts;
            try {
                parts = req.cookie("user").split("----");
                tmpUsername = parts[0];
                tmpPassword = parts[1];
            } catch (Exception e){
            }
            for (ProfEntity prof : profs){
                if (tmpUsername.equals(prof.getUsername()) && tmpPassword.equals(prof.getHashedPassword())){
                    currentProf = prof;
                    break;
                }
            }

            res.redirect("/users");

            // en fonction du formulaire remplit, on appelle la bonne fonction
            if (firstname != null && lastname != null && group != null){
                return StudentGUI.create(firstname, lastname, group);
            } else if (gommette != null && description != null && studentID != null){
                return StudentGUI.addGommette(gommette, description, studentID, currentProf.getId());
            } else {
                return null;
            }
        });

        put("/users", (req, res) -> {
//            System.out.println(req.body());

            String tmp = req.body().replaceAll("\"", "");

            String[] formParts = null;
            try {
                formParts = tmp.split("----");
            } catch (Exception e){
                System.out.println(e);
            }

            String gommette, description, studentID, tmpUsername = null, tmpPassword = null;

            //need authentificate()
            ArrayList<ProfEntity> profs = ProfCore.getAllUsers();
            ProfEntity currentProf = null;
            String[] parts;
            try {
                parts = req.cookie("user").split("----");
                tmpUsername = parts[0];
                tmpPassword = parts[1];
            } catch (Exception e){
            }
            for (ProfEntity prof : profs){
                if (tmpUsername.equals(prof.getUsername()) && tmpPassword.equals(prof.getHashedPassword())){
                    currentProf = prof;
                    break;
                }
            }


            return StudentGUI.addGommette(formParts[0], formParts[1], formParts[2], currentProf.getId());
        });

        /*
         * Call create() to handle registration from form
         **/
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

        /*
         * Call login(), also passes 'response' to be able to write a cookie
         **/
        post("/login", (req, res) -> {

            String firstname, lastname, username, newPassword;
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp")); // ne sait pas ce que ce truc fait yet, mais fonctionne pas sans

            username = req.queryParams("username");
            newPassword = req.queryParams("password");

            return ProfGUI.login(username, newPassword, res);
        });

        /*
         * .queryParam -> localhost:8081/users/delete?firstname=julien&lastname=herbaux
         * .params -> /users/:id/delete
         **/
        delete("/users/:id/delete", (req, res) -> {
            String id = req.params(":id");

            return StudentGUI.delete(id);
        });

        delete("/gommette/:id/delete", (req, res) -> {
            String id = req.params(":id");

            return StudentGUI.deleteGommette(id);
        });
    }
}