package com.uca.gui;

import com.uca.core.ProfCore;
import com.uca.core.StudentCore;
import com.uca.entity.ProfEntity;
import com.uca.entity.StudentEntity;
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

import static java.lang.Integer.parseInt;


/*
 * ***
 * Controller
 * ***
 **/
public class StudentGUI {

    public static String getAllUsers() throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        /*
         * 1 call in .ftl = 1 obj in Java, with the HashMap
         **/
        Map<String, Object> input = new HashMap<>();

        input.put("users", StudentCore.getAllUsers());
        input.put("x", 0);

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/users.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }

    public static String getAllUsers(spark.Request req) throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        /*
         * 1 call in .ftl = 1 obj in Java, with the HashMap
         **/
        Map<String, Object> input = new HashMap<>();

        input.put("users", StudentCore.getAllUsers());

        int x = 0;
        String tmpUsername = new String();
        String tmpPassword = new String();
        String[] parts;
        try {
            parts = req.cookie("user").split("----");
            tmpUsername = parts[0];
            tmpPassword = parts[1];
        } catch (Exception e){

        }


        ArrayList<ProfEntity> profs = ProfCore.getAllUsers();
        ProfEntity currentProf  = new ProfEntity();

        int tmp = 0;

        for (ProfEntity prof : profs){
            if (tmpPassword.equals(prof.getHashedPassword())){
                currentProf = prof;
                tmp = 1;
                break;
            }
        }

        if (tmp == 1){
            x = 1;
        }

        input.put("x", x);

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/users.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }

    public static StudentEntity create(String firstname, String lastname, String group) throws SQLException, IOException, TemplateException {

        StudentEntity newUser = new StudentEntity();

        newUser.setFirstName(firstname);
        newUser.setLastName(lastname);
        newUser.setGroup(group);

        newUser = StudentCore.create(newUser);

        return newUser;
    }

    public static String delete(String firstname, String lastname) throws SQLException, TemplateException, IOException {

        StudentEntity obj = new StudentEntity();

        StudentCore.delete(obj);
        return StudentGUI.getAllUsers();
    }
}
