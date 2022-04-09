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

    // Authenticate the user by hashing the inputted password using the stored salt,
    // then comparing the generated hashed password to the stored hashed password
//    public static boolean authenticate(String username, String password) {
//        if (username.isEmpty() || password.isEmpty()) {
//            return false;
//        }
//        User user = userDao.getUserByUsername(username);
//        if (user == null) {
//            return false;
//        }
//        String hashedPassword = BCrypt.hashpw(password, user.getSalt());
//        return hashedPassword.equals(user.getHashedPassword());
//    }
//
//    // This method doesn't do anything, it's just included as an example
//    public static void setPassword(String username, String oldPassword, String newPassword) {
//        if (authenticate(username, oldPassword)) {
//            String newSalt = BCrypt.gensalt();
//            String newHashedPassword = BCrypt.hashpw(newSalt, newPassword);
//            // Update the user salt and password
//        }
//    }

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

    public static String delete(String firstname, String lastname) throws SQLException, TemplateException, IOException {

        ProfEntity obj = new ProfEntity();

        ProfCore.delete(obj);
        return ProfGUI.getAllUsers();
    }
}
