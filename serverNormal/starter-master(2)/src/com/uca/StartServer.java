package com.uca;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uca.dao._Initializer;
import com.uca.entity.Gommette;
import com.uca.gui.ProfGUI;
import com.uca.gui.StudentGUI;
import com.uca.security.doLogin;

import javax.servlet.MultipartConfigElement;
import java.util.ArrayList;
import java.util.Objects;

import static spark.Spark.*;

// La réponse est B Jean-Pierre, et c'est mon ultime bafouille !

public class StartServer {

    public static void main(String[] args) {
        //Configure Spark
        staticFiles.location("/static/");
        port(8081);

        _Initializer.Init();
        /*
        * ***
        * Router, defining our routes
        * ***
        **/

        //Redirect to "/users"
        get("/", (req, res) -> {
            res.redirect("/users");
            return null;
        });

        //Print all students, and more features to logged in profs
        get("/users", (req, res) -> {
            String token = req.cookie("user");
            if (token == null) {
                return StudentGUI.getAllUsers(false);
            }
            try {
                doLogin.introspec(token);
            } catch (Exception e) {
                return StudentGUI.getAllUsers(false);
            }
            /*
             * If the user exists in our db and the cookie is written, then he is logged-in and he can see the students + add gommettes etc
             * Else he can only see the list of students
             **/
            return StudentGUI.getAllUsers(true);
        });

        //Get a precise student page, listing its gommettes and associated descriptions
        get("/users/:id", (req, res) -> {
            String id = req.params(":id");

            String token = req.cookie("user");
            if (token == null) {
                return StudentGUI.getUser(id, false);
            }
            try {
                doLogin.introspec(token);
            } catch (Exception e) {
                return StudentGUI.getUser(id, false);
            }

            return StudentGUI.getUser(id, true);
        });

        //List all profs
        get("/register", (req, res) -> {
            String token = req.cookie("user");
            if (token == null) {
                return ProfGUI.registerPage(false);
            }
            try {
                doLogin.introspec(token);
            } catch (Exception e) {
                return ProfGUI.registerPage(false);
            }
            return ProfGUI.registerPage(true);
        });

        //Print the login page (for the profs)
        get("/login", (req, res) -> {
            String token = req.cookie("user");
            if (token == null) {
                return ProfGUI.loginPage(false);
            }
            try {
                doLogin.introspec(token);
            } catch (Exception e) {
                return ProfGUI.loginPage(false);
            }
            return ProfGUI.loginPage(true);
        });

        //Create a student
        post("/protected/users", (req, res) -> {

            String firstname, lastname, group;
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            // on recup les infos du formulaire
            firstname = req.queryParams("firstname");
            lastname = req.queryParams("lastname");
            group = req.queryParams("group");

            String token = req.cookie("user");

            if (token == null) {
                halt(401);
            }
            try {
                doLogin.introspec(token);
            } catch (Exception e) {
                halt(401);
            }

            res.redirect("/users");

            return StudentGUI.create(firstname, lastname, group);
        });

        //Register a prof
        post("/register", (req, res) -> {

            String firstname, lastname, username, newPassword;
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            firstname = req.queryParams("firstname");
            lastname = req.queryParams("lastname");
            username = req.queryParams("username");
            newPassword = req.queryParams("password");

            try {
                ProfGUI.create(firstname, lastname, username, newPassword);
            } catch (Exception e) {
                e.printStackTrace();
                halt(500);
            }

            ArrayList<String> tokenAndPage = ProfGUI.login(username, newPassword);
            if (tokenAndPage == null) {
                return "User does not exists.";
            }
            try {
                res.cookie("user", tokenAndPage.get(0));
            } catch (Exception e){
                e.printStackTrace();
            }
            return tokenAndPage.get(1);
        });

        //Login prof
        post("/login", (req, res) -> {

            String username, newPassword;
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            username = req.queryParams("username");
            newPassword = req.queryParams("password");

            ArrayList<String> tokenAndPage = ProfGUI.login(username, newPassword);
            if (tokenAndPage == null) {
                return "User does not exists.";
            }
            try {
                res.cookie("user", tokenAndPage.get(0));
            } catch (Exception e){
                e.printStackTrace();
            }
            return tokenAndPage.get(1);
        });

        //Add a gommette to a user
        put("/protected/users", (req, res) -> {

            Gommette gomTmp = null;
            ObjectMapper mapper = new ObjectMapper();
            try {
                /*
                 * JSON from String to Object.
                 * the following automatically map the values in the JSON.stringify to the variable with the same name in the class instance (gomTmp).
                 * BTW: we put an id in the Gommette instance, the id is the one of the student, we juste store it there for now
                 **/
                gomTmp = mapper.readValue(req.body(), Gommette.class);
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            int id_prof = -1;
            String token = req.cookie("user");
            if (token == null) {
                halt(401);
            }
            try {
                id_prof = Integer.parseInt(Objects.requireNonNull(doLogin.introspec(token)).get("uuid"));
            } catch (Exception e) {
                halt(401);
            }

            return StudentGUI.addGommette(gomTmp.getColour(), gomTmp.getDescription(), String.valueOf(gomTmp.getId()), id_prof);
        });

        //Modify a specific gommette
        put("/protected/users/:id", (req, res) -> {

            Gommette gomTmp = null;
            ObjectMapper mapper = new ObjectMapper();
            try {
                /*
                 * JSON from String to Object.
                 * the following automatically map the values in the JSON.stringify to the variable with the same name in the class instance (gomTmp).
                 * BTW: we put an id in the Gommette instance, the id is the one of the student, we juste store it there for now
                 **/
                gomTmp = mapper.readValue(req.body(), Gommette.class);
            } catch ( Exception e ) {
                e.printStackTrace();
            }

            return StudentGUI.modifyGommette(gomTmp.getColour(), gomTmp.getDescription(), String.valueOf(gomTmp.getId()));
        });

        //Delete a specific student
        delete("/protected/users/:id/delete", (req, res) -> {
            String id = req.params(":id");
            return StudentGUI.delete(id);
        });

        //Delete a gommette from a student
        delete("/protected/gommette/:id/delete", (req, res) -> {
            String id = req.params(":id");
            return StudentGUI.deleteGommette(id);
        });

        //Delete a prof
        delete("/protected/prof/:id/delete", (req, res) -> {
            String id_to_del = req.params(":id");

            String id_prof = "";
            String token = req.cookie("user");
            if (token == null) {
                halt(401);
            }
            try {
                id_prof = Objects.requireNonNull(doLogin.introspec(token)).get("uuid");
            } catch (Exception e) {
                halt(401);
            }
            return ProfGUI.delete(id_prof, id_to_del);
        });
    }
}