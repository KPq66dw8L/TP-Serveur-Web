package com.uca.gui;

import com.uca.core.ProfCore;
import com.uca.entity.ProfEntity;
import com.uca.security.doLogin;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/*
 * ***
 * Controller
 * ***
 **/
public class ProfGUI {

    //Return all profs. Send infos to FreeMarker that parses and use HashMap, and return the page as a string
    public static String registerPage(boolean loggedIn) throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        Map<String, Object> input = new HashMap<>();

        input.put("users", ProfCore.getAllUsers());
        input.put("logged", loggedIn);

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("content/register.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);
        return output.toString();
    }

    //Handle part of the registration of a prof => create a new prof entity, and return the list of profs afterwards
    public static String create(String firstname, String lastname, String username, String newPassword) throws SQLException, IOException, TemplateException {

        ProfEntity newUser = new ProfEntity();

        if (username != null) {
            if (ProfCore.isUsernameTaken(username)) {
                return "Username already taken.";
            }
        }

        newUser.setFirstName(firstname);
        newUser.setLastName(lastname);
        newUser.setUsername(username);
        newUser.setHashedPassword(newPassword);
        ProfCore.create(newUser);

        return registerPage(true);
    }

    //Content of the login page, to print a custom validation msg when successfully logged in
    public static String loginPage(boolean loggedIn) throws IOException, TemplateException {
        try {
            Configuration configuration = _FreeMarkerInitializer.getContext();

            Map<String, Object> input = new HashMap<>();

            input.put("msg", "");
            input.put("logged", loggedIn);

            Writer output = new StringWriter();
            Template template = configuration.getTemplate("content/login.ftl");
            template.setOutputEncoding("UTF-8");
            template.process(input, output);

            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Erreur";
    }

    //Handle login = check if user exists in db and if so, write it in cookie with key: "user"
    public static ArrayList<String> login(String username, String password) throws TemplateException, IOException {
        ProfEntity prof = ProfCore.getUser(username, password); // login du prof
        if (prof == null) {
            return null;
        }
        String token = doLogin.createToken(prof.getId(), prof.getFirstName(), prof.getLastName(), prof.getUsername()); //creation du token pour la session

        Configuration configuration = _FreeMarkerInitializer.getContext();
        Map<String, Object> input = new HashMap<>();

        input.put("msg", "Bienvenue " + prof.getUsername());
        input.put("logged", true);

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("content/login.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        //mise en form du return
        ArrayList<String> ret = new ArrayList<>();
        ret.add(token);
        ret.add(output.toString());
        return ret;
    }

    public static String delete(String strId, String strId_to_del) throws SQLException, TemplateException, IOException {

        int id = Integer.parseInt(strId);
        int id_to_del = Integer.parseInt(strId_to_del);

        ProfCore.delete(id, id_to_del);
        return null;
    }
}
