package com.uca;

import com.uca.dao.UserDAO;
import com.uca.dao._Initializer;
import com.uca.entity.UserEntity;
import com.uca.gui.*;

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

        get("/users/add/:firstname/:lastname/:prof/:blanche/:rouge/:verte", (req, res) -> {
            //newUser.setId(1);
//            UserEntity newUser = new UserEntity();
//            newUser.setFirstName(req.params(":firstname"));
//            newUser.setLastName(req.params(":lastname"));
//            newUser.setProf(req.params(":prof"));
//            newUser.setBlanche(parseInt(req.params(":blanche")));
//            newUser.setRouge(parseInt(req.params(":rouge")));
//            newUser.setVerte(parseInt(req.params(":verte")));
//
//            newUser = new UserDAO().create(newUser);
//
//            return "added " + newUser.getFirstName();
            
            res.redirect("/users");

            return UserGUI.create(req.params(":firstname"), req.params(":lastname"), req.params(":prof"), req.params(":blanche"), req.params(":rouge"), req.params(":verte"));
        });

        get("/users/delete/:firstname/:lastname", (req, res) -> {

            res.redirect("/users");
            return UserGUI.delete(req.params(":firstname"), req.params(":lastname"));
        });
    }
}