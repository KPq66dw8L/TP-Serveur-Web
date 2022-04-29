package com.uca;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uca.core.ProfCore;
import com.uca.core.StudentCore;
import com.uca.dao._Initializer;
import com.uca.entity.GivenGommettes;
import com.uca.entity.Gommette;
import com.uca.entity.ProfEntity;
import com.uca.security.doLogin;
import com.uca.entity.StudentEntity;
import io.jsonwebtoken.ExpiredJwtException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static spark.Spark.*;

/*
 * ***
 * Router & controller (sérialiser/dé-sérialiser)
 * ***
 **/

public class StartServer {

    public static void main(String[] args) {
        //Configure Spark
        staticFiles.location("/static/");
        port(8081);

        _Initializer.Init();

        // to allow CORS
        options("/*",
                (request, response) -> {
                    String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                    }
                    return "OK";
                });

        before(
                (request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        before("/protected/*", (request, response) -> {
            //System.out.println(request.headers());
            //System.out.println(request.headers("Authorization"));
            //System.out.println(request.requestMethod());
            if (!request.requestMethod().equals("OPTIONS")) {
                System.out.println(doLogin.introspec(request.headers("Authorization")));
                try {
                    if (doLogin.introspec(request.headers("Authorization")) == null) {
                        halt(401, "You are not welcome here.");
                    } else {
                        System.out.println("Access authorized.");
                    }
                } catch (ExpiredJwtException e) {
                    halt(400, "Token expired.");
                }
            }
        });

        // Print all students
        get("/users", (req, res) -> {
            System.out.println("Called get /users");
            ArrayList<StudentEntity> students = StudentCore.getAllUsers();
            String json = "";
            try {
                // create `ObjectMapper` instance
                ObjectMapper mapper = new ObjectMapper();
                // create `ArrayNode` object
                ArrayNode arrayNode = mapper.createArrayNode();

                // create each JSON object
                for (StudentEntity student : students) {
                    ObjectNode user = mapper.createObjectNode();
                    user.put("id", student.getId());
                    user.put("firstName", student.getFirstName());
                    user.put("lastName", student.getLastName());
                    user.put("group", student.getGroup());
                    user.put("white", student.getNb_white());
                    user.put("green", student.getNb_green());
                    user.put("red", student.getNb_red());

                    // add each JSON student to array
                    arrayNode.addAll(List.of(user));
                }

                // convert `ArrayNode` to pretty-print JSON
                json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return json;
        });

        // Get one student with its gommettes
        get("/users/:id", (req, res) -> {
            String id = req.params(":id");

            StudentEntity student = null;
            String json = "";

            // on récupère l'étudiant
            for (StudentEntity stu : StudentCore.getAllUsers()) {
                if (stu.getId() == Integer.parseInt(id)) {
                    student = stu;
                }
            }
            if (student == null) {
                res.status(404); // send error 404 if the student does not exist
                return res;
            }
            // on met en forme le json a renvoyer
            try {
                // create `ObjectMapper` instance
                ObjectMapper mapper = new ObjectMapper();

                // create `ArrayNode` object for the student
                ObjectNode user = mapper.createObjectNode();
                user.put("id", student.getId());
                user.put("firstName", student.getFirstName());
                user.put("lastName", student.getLastName());
                user.put("group", student.getGroup());
                user.put("nbWhite", student.getNb_white());
                user.put("nbGreen", student.getNb_green());
                user.put("nbRed", student.getNb_red());

                // create `ArrayNode` objects for the student's gommettes
                ArrayNode arrayNodeWhite = mapper.createArrayNode();
                ArrayNode arrayNodeGreen = mapper.createArrayNode();
                ArrayNode arrayNodeRed = mapper.createArrayNode();

                // sort the gommettes
                for (GivenGommettes gom : student.gommettes) {
                    // temporary object to get each gommette
                    ObjectNode studentGommette = mapper.createObjectNode();
                    studentGommette.put("id", gom.getGommette().getId());
                    studentGommette.put("colour", gom.getGommette().getColour());
                    studentGommette.put("description", gom.getGommette().getDescription());
                    studentGommette.put("date", gom.getDate());
                    studentGommette.put("prof", gom.getId_prof());
                    if (gom.getGommette().getColour().equals("white")) {
                        arrayNodeWhite.add(studentGommette);
                    } else if (gom.getGommette().getColour().equals("green")) {
                        arrayNodeGreen.add(studentGommette);
                    } else {
                        arrayNodeRed.add(studentGommette);
                    }
                }
                ObjectNode everyGommettes = mapper.createObjectNode();

                // nest the arrays
                everyGommettes.set("white", arrayNodeWhite);
                everyGommettes.set("green", arrayNodeGreen);
                everyGommettes.set("red", arrayNodeRed);

                user.set("gommettes", everyGommettes);

                // convert `ObjectNode` to pretty-print JSON
                // without pretty-print, use `arrayNode.toString()` method
                json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return json;
        });

        // List all profs
        get("/register", (req, res) -> {
            ArrayList<ProfEntity> list = ProfCore.getAllUsers();
            String json = "";
            try {
                // create `ObjectMapper` instance
                ObjectMapper mapper = new ObjectMapper();
                // create `ArrayNode` object
                ArrayNode arrayNode = mapper.createArrayNode();

                // create each JSON object
                for (ProfEntity prof : list) {
                    ObjectNode user = mapper.createObjectNode();
                    user.put("id", prof.getId());
                    user.put("firstName", prof.getFirstName());
                    user.put("lastName", prof.getLastName());
                    user.put("username", prof.getUsername());

                    // add each JSON student to array
                    arrayNode.addAll(List.of(user));
                }

                // convert `ArrayNode` to pretty-print JSON
                json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return json;
        });

        // Create a student
        post("/protected/users", (req, res) -> {
            StudentEntity tmpStu = null;

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            try {
                /*
                 * JSON from String to Object.
                 * the following automatically map the values in the JSON.stringify to the variable with the same name in the class instance (gomTmp).
                 * BTW: we put an id in the Gommette instance, the id is the one of the student, we juste store it there for now
                 **/
                tmpStu = mapper.readValue(req.body(), StudentEntity.class);
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            if (!StudentCore.create(tmpStu)) {
                res.status(201);
            } else {
                res.status(500);
            }
            return res;
        });

        // register a prof
        post("/register", (req, res) -> {

            ProfEntity currentProf  = new ProfEntity();

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            try {
                /*
                 * JSON from String to Object.
                 * the following automatically map the values in the JSON.stringify to the variable with the same name in the class instance (gomTmp).
                 * BTW: we put an id in the Gommette instance, the id is the one of the student, we juste store it there for now
                 **/
                currentProf = mapper.readValue(req.body(), ProfEntity.class);
            } catch ( Exception e ) {
                e.printStackTrace();
            }

            ProfEntity prof = ProfCore.create(currentProf);
            if (prof == null) {
                halt(400, "Error creating account.");
            }
            String token = doLogin.createToken(prof.getId(), prof.getFirstName(), prof.getLastName(), prof.getUsername());

            String json = "";
            try {
                // create `ObjectMapper` instance
                ObjectMapper mapper2 = new ObjectMapper();

                // create a JSON object
                ObjectNode user = mapper2.createObjectNode();
                user.put("id", prof.getId());
                user.put("firstName", prof.getFirstName());
                user.put("lastName", prof.getLastName());
                user.put("username", prof.getUsername());
                user.put("token", token);

                // convert `ObjectNode` to pretty-print JSON
                json = mapper2.writerWithDefaultPrettyPrinter().writeValueAsString(user);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return json;
        });

        // login a prof
        post("/login", (req, res) -> {
            System.out.println("login started");

            ProfEntity currentProf  = new ProfEntity();

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // ignore if attribute of the currentProf instance are not filled
            try {
                // JSON from String to Object. We only have the username and hashedPassword.
                currentProf = mapper.readValue(req.body(), ProfEntity.class);
            } catch ( Exception e ) {
                e.printStackTrace();
            }

            ProfEntity prof = ProfCore.getUser(currentProf.getUsername(), currentProf.getHashedPassword());

            String token = doLogin.createToken(prof.getId(), prof.getFirstName(), prof.getLastName(), prof.getUsername());

            String json = "";
            try {
                // create `ObjectMapper` instance
                ObjectMapper mapper2 = new ObjectMapper();

                // create a JSON object
                ObjectNode user = mapper2.createObjectNode();
                user.put("id", prof.getId());
                user.put("firstName", prof.getFirstName());
                user.put("lastName", prof.getLastName());
                user.put("username", prof.getUsername());
                user.put("token", token);

                // convert `ObjectNode` to pretty-print JSON
                json = mapper2.writerWithDefaultPrettyPrinter().writeValueAsString(user);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return json;
        });

        // Add a gommette to a student
        put("/protected/users/:idProf", (req, res) -> {

            int id_prof = Integer.parseInt(req.params(":idProf"));

            // Set the gommette colour & description and the student's id in a temporary object, form the JSON received
            Gommette gomTmp;
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
                return false;
            }

            try {
                Gommette newGommette = new Gommette();
                newGommette.setColour(gomTmp.getColour());
                newGommette.setDescription(gomTmp.getDescription());
                GivenGommettes donneLaGommette = new GivenGommettes();

                int id_student = gomTmp.getId();
                donneLaGommette.setId_student(id_student);

                donneLaGommette.setGommette(newGommette);

                Date date = new Date();
                donneLaGommette.setDate(date);

                donneLaGommette.setId_prof(id_prof);

                StudentCore.addGommette(donneLaGommette);
                res.status(201);
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
            }

            return res;
        });

        // Modify a specific gommette
        put("/protected/users/:id/gommette", (req, res) -> {
//            String studentId = req.params(":id");

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

            StudentCore.modifyGommette(gomTmp.getId(), gomTmp.getColour(), gomTmp.getDescription());
            return res;
        });

        /*
         * Notes:
         * .queryParam -> localhost:8081/users/delete?firstname=julien&lastname=Airbot
         * .params -> /users/:id/delete
         **/
        // delete a student
        delete("/protected/users/:id/delete", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));

            // everything before StudentCore.delete is used to get the gommettes of the student-to-delete, to be able to delete them from the db
            ArrayList<StudentEntity> students = StudentCore.getAllUsers();

            StudentEntity stu_to_del = new StudentEntity();

            for (StudentEntity student : students) {
                if (id == student.getId()){
                    stu_to_del = student;
                }
            }

            ArrayList<Integer> gommettes_id = new ArrayList<>();

            for (int i = 0 ; i < stu_to_del.gommettes.size() ; i++){
                gommettes_id.add(stu_to_del.gommettes.get(i).getId());
            }

            StudentCore.delete(id, gommettes_id);

            return res;
        });

        // delete a gommette
        delete("/protected/gommette/:id/delete", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            StudentCore.deleteGommette(id);
            return res;
        });

        // delete a prof
        delete("/protected/register/:id/delete/:idToDel", (req, res) -> {
            System.out.println("Deleting prof");

            int id_prof = Integer.parseInt(req.params(":id"));
            int id_to_del = Integer.parseInt(req.params(":idToDel"));

            boolean status = ProfCore.delete(id_prof, id_to_del);
            if (!status) {
                res.status(404);
            }
            return res;
        });
    }
}