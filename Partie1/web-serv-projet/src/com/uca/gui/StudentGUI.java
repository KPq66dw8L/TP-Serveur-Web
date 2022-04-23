package com.uca.gui;

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
import freemarker.template.TemplateException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/*
 * ***
 * Controller
 * ***
 **/
public class StudentGUI {

    // Return the json containing every student infos
    public static String getAllUsers() {

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
    }

    public static String getUser(String idStudent) {

        // TODO : try catch : if the id does not correspond to any student?

        StudentEntity student = null;
        String json = "";

        // on récupère l'étudiant
        for (StudentEntity stu : StudentCore.getAllUsers()) {
            if (stu.getId() == Integer.parseInt(idStudent)) {
                student = stu;
            }
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
            e.printStackTrace();
        }

        return StudentCore.create(tmpStu) == null;
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
        return StudentGUI.getAllUsers();
    }

    /*
     * Handle the creation of a new Gommette + a new GivenGommette. Also call the necessary function to create everything in the db.
     * Return the list of students updated, with the logged-in parameter as one already need to be logged to be able to manipulate gommettes.
     **/
    public static boolean addGommette(String body, String hashedPwd) {

        Integer id_prof = null;
        // Get the prof who gave the gommette
        for (ProfEntity pro : ProfCore.getAllUsers()) {
            if (hashedPwd.equals(pro.getHashedPassword())) {
                id_prof = pro.getId();
            }
        }
        if (id_prof == null) {
            return false;
        }

        // Set the gommette colour & description and the student's id in a temporary object, form the JSON received
        Gommette gomTmp;
        ObjectMapper mapper = new ObjectMapper();
        try {
            /*
             * JSON from String to Object.
             * the following automatically map the values in the JSON.stringify to the variable with the same name in the class instance (gomTmp).
             * BTW: we put an id in the Gommette instance, the id is the one of the student, we juste store it there for now
             **/
            gomTmp = mapper.readValue(body, Gommette.class);
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
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void modifyGommette(String reqBody, String studentId){

        Gommette gomTmp = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            /*
             * JSON from String to Object.
             * the following automatically map the values in the JSON.stringify to the variable with the same name in the class instance (gomTmp).
             * BTW: we put an id in the Gommette instance, the id is the one of the student, we juste store it there for now
             **/
            gomTmp = mapper.readValue(reqBody, Gommette.class);
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        StudentCore.modifyGommette(gomTmp.getId(), gomTmp.getColour(), gomTmp.getDescription());

        StudentGUI.getUser(studentId);
    }

    public static Object deleteGommette(String idGommette) {

        int id = Integer.parseInt(idGommette);
        StudentCore.deleteGommette(id);
        return StudentGUI.getAllUsers();
    }
}
