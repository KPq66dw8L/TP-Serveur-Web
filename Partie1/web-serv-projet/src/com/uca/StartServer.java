package com.uca;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uca.core.ProfCore;
import com.uca.dao._Initializer;
import com.uca.entity.Gommette;
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

        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

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
            System.out.println("Called get /users");
            return StudentGUI.getAllUsers(false);
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
         * Handle post request from 1st form to add a student & 2nd form to add a gommette
         **/
        post("/users", (req, res) -> {

            //need authentificate()
//            ArrayList<ProfEntity> profs = ProfCore.getAllUsers();
//            ProfEntity currentProf = null;
//            String[] parts;
//            try {
//                parts = req.cookie("user").split("----");
//                tmpUsername = parts[0];
//                tmpPassword = parts[1];
//            } catch (Exception e){
//            }
//            for (ProfEntity prof : profs){
//                if (tmpUsername.equals(prof.getUsername()) && tmpPassword.equals(prof.getHashedPassword())){
//                    currentProf = prof;
//                    break;
//                }
//            }

            if (StudentGUI.create(req.body())) {
                res.status(201);
            } else {
                res.status(500);
            }
            return res;
        });

        /*
         * Call create() to handle registration from form
         **/
        post("/register", (req, res) -> {

            ProfGUI.create(req.body());

            return res;
        });

        /*
         * Call login(), also passes 'response' to be able to write a cookie
         **/
        post("/login", (req, res) -> {

            String tmpProf = ProfGUI.login(req.body());
//            res.header("user", tmpProf);
            res.body(tmpProf);
            if (tmpProf == null) {
                res.status(401);
            }
            System.out.println(res.body());
            return res;
        });

        /*
         * Add a gommette to a user
         **/
        put("/users", (req, res) -> {


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
                System.out.println(e);
            }




//            String tmp = req.body().replaceAll("\"", "");
//            // a l'ancienne
//            String[] formParts = null;
//            try {
//                formParts = tmp.split("----");
//            } catch (Exception e){
//                System.out.println(e);
//            }

            String gommette, description, studentID, tmpUsername = null, tmpPassword = null;

            //need authentificate()
//            ArrayList<ProfEntity> profs = ProfCore.getAllUsers();
//            ProfEntity currentProf = null;
//            String[] parts;
//            try {
//                parts = req.cookie("user").split("----");
//                tmpUsername = parts[0];
//                tmpPassword = parts[1];
//            } catch (Exception e){
//            }
//            for (ProfEntity prof : profs){
//                if (tmpUsername.equals(prof.getUsername()) && tmpPassword.equals(prof.getHashedPassword())){
//                    currentProf = prof;
//                    break;
//                }
//            }


//            return StudentGUI.addGommette(formParts[0], formParts[1], formParts[2], currentProf.getId());




//            StudentGUI.addGommette(gomTmp.getColour(), gomTmp.getDescription(), String.valueOf(gomTmp.getId()), currentProf.getId());
            return res;
        });

        /*
         * Modify a specific gommette
         **/
        put("/users/:id", (req, res) -> {
//            String tmp = req.body().replaceAll("\"", "");
//
//            String[] formParts = null;
//            try {
//                formParts = tmp.split("----");
//            } catch (Exception e){
//                System.out.println(e);
//            }

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
                System.out.println(e);
            }

            //return StudentGUI.modifyGommette(formParts[0], formParts[1], formParts[2], req.params(":id"));
            return StudentGUI.modifyGommette(gomTmp.getColour(), gomTmp.getDescription(), String.valueOf(gomTmp.getId()), req.params(":id"));
        });

        /*
         * .queryParam -> localhost:8081/users/delete?firstname=julien&lastname=herbaux
         * .params -> /users/:id/delete
         **/
        delete("/users/:id/delete", (req, res) -> {
            String id = req.params(":id");

            StudentGUI.delete(id);

            return res;
        });

        delete("/gommette/:id/delete", (req, res) -> {
            String id = req.params(":id");

            return StudentGUI.deleteGommette(id);
        });
    }
}