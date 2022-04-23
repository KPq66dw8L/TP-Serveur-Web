package com.uca;

import com.uca.dao._Initializer;
import com.uca.gui.ProfGUI;
import com.uca.gui.StudentGUI;

import static spark.Spark.*;

public class StartServer {

    public static void main(String[] args) {
        //Configure Spark
        staticFiles.location("/static/");
        port(8081);

        _Initializer.Init();

        // to allow CORS
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
        * Router, defining our routes
        * ***
        **/

        // Print all students, and more features to logged in profs
        get("/users", (req, res) -> {
            System.out.println("Called get /users");
            return StudentGUI.getAllUsers();
        });

        // Get one student with its gommettes
        get("/users/:id", (req, res) -> {
            String id = req.params(":id");

            String infosStu = StudentGUI.getUser(id);
            res.body(infosStu);

            return res.body();
        });

        // List all profs
//        get("/register", (req, res) -> {
//            return ProfGUI.getAllUsers();
//        });

        // Create a student
        post("/users", (req, res) -> {

            if (StudentGUI.create(req.body())) {
                res.status(201);
            } else {
                res.status(500);
            }
            return res;
        });

        // register a prof
        post("/register", (req, res) -> {

            ProfGUI.create(req.body());

            return res;
        });

        // login a prof
        post("/login", (req, res) -> {

            String tmpProf = ProfGUI.login(req.body());
            res.body(tmpProf);
            if (tmpProf == null) {
                res.status(401);
                return res;
            }

            return res.body(); // problem -> no header in the response sent. If sending whole res, don't know how to read 'spark.Response@42c86cfd' in Frontend
        });

        // Add a gommette to a student
        put("/users/:hashedPwd", (req, res) -> {

            String hashedPwd = req.params(":hashedPwd");

            boolean tmp = StudentGUI.addGommette(req.body(), hashedPwd);
            if (!tmp) {
                res.status(500);
            } else {
                res.status(201);
            }
            return res;
        });

        // Modify a specific gommette
        put("/users/:id/gommette", (req, res) -> {
            StudentGUI.modifyGommette(req.body(), req.params(":id"));
            return res;
        });

        /*
         * Notes:
         * .queryParam -> localhost:8081/users/delete?firstname=julien&lastname=Airbot
         * .params -> /users/:id/delete
         **/
        // delete a student
        delete("/users/:id/delete", (req, res) -> {
            String id = req.params(":id");
            StudentGUI.delete(id);

            return res;
        });

        // delete a gommette
        delete("/gommette/:id/delete", (req, res) -> {
            String id = req.params(":id");
            return StudentGUI.deleteGommette(id);
        });
    }
}