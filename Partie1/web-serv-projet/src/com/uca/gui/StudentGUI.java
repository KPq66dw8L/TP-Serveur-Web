package com.uca.gui;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.util.*;

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

        ArrayList<StudentEntity> students = StudentCore.getAllUsers();
        String json = new String();
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

                // add JSON users to array
                arrayNode.addAll(Arrays.asList(user));
            }

            // convert `ArrayNode` to pretty-print JSON
            // without pretty-print, use `arrayNode.toString()` method
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return json;
    }

    public static String getUser(String idStudent) throws IOException, TemplateException {

        // TODO : try catch : if the id does not correspond to any student?
        Configuration configuration = _FreeMarkerInitializer.getContext();

        Map<String, Object> input = new HashMap<>();

        StudentEntity student = null;

        for (StudentEntity stu : StudentCore.getAllUsers()) {
            if (stu.getId() == Integer.parseInt(idStudent)) {
                student = stu;
            }
        }

        input.put("user", student);

//        ArrayList<Gommette> gommettes_white = new ArrayList<>(), gommettes_green = new ArrayList<>(), gommettes_red = new ArrayList<>();
//        for (GivenGommettes givenGom : student.gommettes) {
//            if (givenGom.getGommette().getColour().equals("white")){
//                gommettes_white.add(givenGom.getGommette());
//            } else if (givenGom.getGommette().getColour().equals("green")){
//                gommettes_green.add(givenGom.getGommette());
//            } else {
//                gommettes_red.add(givenGom.getGommette());
//            }
//        }
//
//        input.put("gommetteWhite", gommettes_white);
//        input.put("gommetteGreen", gommettes_green);
//        input.put("gommetteRed", gommettes_red);

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
            Template template = configuration.getTemplate("users/student.ftl");
            template.setOutputEncoding("UTF-8");
            template.process(input, output);
        } catch (Exception e){
            System.out.println(e);
        }
        return output.toString();
    }

    /*
     * Handle creation of a new student entity in Java
     **/
    public static boolean create(String newStudent) throws SQLException, IOException, TemplateException {
        StudentEntity tmpStu = null;

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            /*
             * JSON from String to Object.
             * the following automatically map the values in the JSON.stringify to the variable with the same name in the class instance (gomTmp).
             * BTW: we put an id in the Gommette instance, the id is the one of the student, we juste store it there for now
             **/
            tmpStu = mapper.readValue(newStudent, StudentEntity.class);
        } catch ( Exception e ) {
            System.out.println(e);
        }

        if (StudentCore.create(tmpStu) != null) {
            return false;
        }

        return true;
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
    public static Object addGommette(String gommette, String description, String studentID, int id_prof) throws TemplateException, IOException {
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

    public static Object modifyGommette(String gomColour, String gomDescription, String gommetteId, String studentId) throws TemplateException, IOException {

        StudentCore.modifyGommette(Integer.parseInt(gommetteId), gomColour, gomDescription);

        return StudentGUI.getUser(studentId);

    }

    public static Object deleteGommette(String idGommette) throws TemplateException, IOException {

        int id = Integer.parseInt(idGommette);

        StudentCore.deleteGommette(id);


        return StudentGUI.getAllUsers(true);
    }
}
