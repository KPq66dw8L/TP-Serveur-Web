package com.uca.gui;

import com.uca.core.ProfCore;
import com.uca.core.StudentCore;
import com.uca.entity.ProfEntity;
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

    /*
     * Return all profs. Send infos to FreeMarker that parses and use HashMap, and return the page as a string
     **/
    public static String getAllUsers() throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        /*
         * 1 call in .ftl = 1 obj in Java, with the HashMap
         **/
        Map<String, Object> input = new HashMap<>();

        input.put("users", ProfCore.getAllUsers());

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/register.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);
        return output.toString();
    }

    /*
     * Handle part of the registration of a prof = create a new prof entity, and return the list of profs afterwards
     **/
    public static String create(String firstname, String lastname, String username, String newPassword) throws SQLException, IOException, TemplateException {

        ProfEntity newUser = new ProfEntity();

        newUser.setFirstName(firstname);
        newUser.setLastName(lastname);
        newUser.setUsername(username);
        String tmpSalt = BCrypt.gensalt();


        newUser.setSalt(tmpSalt);
        String tmpHsPwd = BCrypt.hashpw(newPassword, tmpSalt);


        newUser.setHashedPassword(tmpHsPwd);

        newUser = ProfCore.create(newUser);

        return ProfGUI.getAllUsers();
    }

    /*
     * Content of the login page, to print a custom validation msg when successfully logged in
     **/
    public static String loginPage() throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        Map<String, Object> input = new HashMap<>();

        input.put("msg", "");

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/login.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }

    /*
     * Handle login = check if user exists in db and if so, write it in cookie "user"
     **/
    public static String login(String username, String password, spark.Response res) throws TemplateException, IOException {

        ArrayList<ProfEntity> profs = ProfCore.getAllUsers();
        ProfEntity currentProf  = new ProfEntity();

        int tmp = 0;

        for (ProfEntity prof : profs){
            if (BCrypt.hashpw(password, prof.getSalt()).equals(prof.getHashedPassword())){
                currentProf = prof;
                tmp = 1;
                break;
            }
        }

        if (currentProf == null || tmp == 0){
            return "Non.";
        }

        try {
            String tmpCookie = currentProf.getUsername() + "----" + currentProf.getHashedPassword();
            res.cookie("user", tmpCookie); //la valeur du cookie doit etre passée par une variable..

        } catch (Exception e){
            System.out.println(e);
        }

        Configuration configuration = _FreeMarkerInitializer.getContext();

        /*
         * 1 call in .ftl = 1 obj in Java, with the HashMap
         **/
        Map<String, Object> input = new HashMap<>();

        input.put("msg", "Bienvenue " + currentProf.getUsername());

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/login.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }

    /*
     * OUTDATED
     **/
    public static String delete(String firstname, String lastname) throws SQLException, TemplateException, IOException {

        ProfEntity obj = new ProfEntity();

        ProfCore.delete(obj);
        return ProfGUI.getAllUsers();
    }
}
