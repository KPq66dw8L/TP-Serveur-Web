package com.uca.gui;

import com.uca.core.StudentCore;
import com.uca.entity.GivenGommettes;
import com.uca.entity.Gommette;
import com.uca.entity.StudentEntity;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
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

    //List all students and tell the template of user is logged in from the cookie "user"
    public static String getAllUsers(boolean logged) {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        //1 variable in .ftl = 1 obj in Java, with the HashMap
        Map<String, Object> input = new HashMap<>();

        input.put("users", StudentCore.getAllUsers());
        input.put("logged", logged);


        Writer output = new StringWriter();
        //try-catch, otherwise an error in template gives a 505 error and no traceback
        try {
            Template template = configuration.getTemplate("content/list.ftl");
            template.setOutputEncoding("UTF-8");
            template.process(input, output);
        } catch (Exception e){
            e.printStackTrace();
        }
        return output.toString();
    }

    public static String getUser(String idStudent, boolean logged) {

        Configuration configuration = _FreeMarkerInitializer.getContext();

        Map<String, Object> input = new HashMap<>();

        StudentEntity student = null;

        for (StudentEntity stu : StudentCore.getAllUsers()) {
            if (stu.getId() == Integer.parseInt(idStudent)) {
                student = stu;
            }
        }

        input.put("user", student);
        input.put("logged", logged);

        ArrayList<GivenGommettes> gommettes_white = new ArrayList<>(), gommettes_green = new ArrayList<>(), gommettes_red = new ArrayList<>();
        for (GivenGommettes givenGom : student.gommettes) {
            if (givenGom.getGommette().getColour().equals("white")){
                gommettes_white.add(givenGom);
            } else if (givenGom.getGommette().getColour().equals("green")){
                gommettes_green.add(givenGom);
            } else {
                gommettes_red.add(givenGom);
            }
        }

        input.put("gommetteWhite", gommettes_white);
        input.put("gommetteGreen", gommettes_green);
        input.put("gommetteRed", gommettes_red);

        Writer output = new StringWriter();

        try {
            Template template = configuration.getTemplate("content/single.ftl");
            template.setOutputEncoding("UTF-8");
            template.process(input, output);
        } catch (Exception e){
            e.printStackTrace();
        }
        return output.toString();
    }

    //Handle creation of a new student entity
    public static StudentEntity create(String firstname, String lastname, String group) throws SQLException, IOException, TemplateException {

        StudentEntity newUser = new StudentEntity();

        newUser.setFirstName(firstname);
        newUser.setLastName(lastname);
        newUser.setGroup(group);

        newUser = StudentCore.create(newUser);

        return newUser;
    }

    public static String delete(String idStudent) throws SQLException, TemplateException, IOException {

        int id = Integer.parseInt(idStudent);

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
        return StudentGUI.getAllUsers(true);
    }

    /*
     * Handle the creation of a new Gommette + a new GivenGommette. Also call the necessary function to create everything in the db.
     * Return the list of students updated, with the logged-in parameter as one already need to be logged to be able to manipulate gommettes.
     **/
    public static Object addGommette(String gommette, String description, String studentID, int id_prof) {
        Gommette newGommette = new Gommette();
        newGommette.setColour(gommette);
        newGommette.setDescription(description);
        GivenGommettes donneLaGommette = new GivenGommettes();

        int id_student = parseInt(studentID);
        donneLaGommette.setId_student(id_student);

        donneLaGommette.setGommette(newGommette);

        Date date = new Date();
        donneLaGommette.setDate(date);

        donneLaGommette.setId_prof(id_prof);

        StudentCore.addGommette(donneLaGommette);

        return StudentGUI.getAllUsers(true);
    }

    public static Object modifyGommette(String gomColour, String gomDescription, String gommetteId) {
        StudentCore.modifyGommette(Integer.parseInt(gommetteId), gomColour, gomDescription);
        return null;
    }

    public static Object deleteGommette(String idGommette) {
        int id = Integer.parseInt(idGommette);
        StudentCore.deleteGommette(id);
        return null;
    }
}
