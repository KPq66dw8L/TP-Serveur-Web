package com.uca.gui;

import com.uca.core.ProfCore;
import com.uca.core.StudentCore;
import com.uca.entity.GivenGommettes;
import com.uca.entity.Gommette;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;


/*
 * ***
 * Controller
 * ***
 **/
public class StudentGUI {

    /*
     * List all students and tell the template of user is logged in from the cookie "user"
     **/
    public static String getAllUsers(boolean logged) throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        /*
         * 1 call in .ftl = 1 obj in Java, with the HashMap
         **/
        Map<String, Object> input = new HashMap<>();

        input.put("users", StudentCore.getAllUsers());

//        for (StudentEntity stu : StudentCore.getAllUsers()){
//            System.out.println(stu.getFirstName());
//        }

        input.put("logged", logged);

        Writer output = new StringWriter();
        try {
            Template template = configuration.getTemplate("users/users.ftl");
            template.setOutputEncoding("UTF-8");
            template.process(input, output);
        } catch (Exception e){
//            System.out.println(e);
        }
        System.out.println("getAllUser ACTION !!");
        return output.toString();
    }

    /*
     * Handle creation of a new student entity
     **/
    public static StudentEntity create(String firstname, String lastname, String group) throws SQLException, IOException, TemplateException {

        StudentEntity newUser = new StudentEntity();

        newUser.setFirstName(firstname);
        newUser.setLastName(lastname);
        newUser.setGroup(group);

        newUser = StudentCore.create(newUser);

        return newUser;
    }

    /*
     * OUTDATED
     **/
    public static String delete(String firstname, String lastname) throws SQLException, TemplateException, IOException {

        StudentEntity obj = new StudentEntity();

        StudentCore.delete(obj);
        return StudentGUI.getAllUsers(false);
    }

    public static Object addGommette(String gommette, String description, String studentID, int id_prof) throws TemplateException, IOException {
        Gommette newGommette = new Gommette();
        newGommette.setColour(gommette);
        newGommette.setDescription(description);
        GivenGommettes donneLaGommette = new GivenGommettes();

        int id_student = parseInt(studentID);
        donneLaGommette.setId_student(id_student);

        donneLaGommette.setGommette(newGommette);
        System.out.println("addGommette dans StudentGUI ACTION !!");

        Date date = new Date();
        donneLaGommette.setDate(date);

        donneLaGommette.setId_prof(id_prof);

        StudentCore.addGommette(donneLaGommette);

        return StudentGUI.getAllUsers(true);
    }
}
