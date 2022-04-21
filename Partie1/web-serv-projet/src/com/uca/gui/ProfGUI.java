package com.uca.gui;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uca.core.ProfCore;
import com.uca.core.StudentCore;
import com.uca.entity.ProfEntity;
import com.uca.entity.StudentEntity;
import com.uca.entity.UserEntity;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static String create(String body) throws SQLException, IOException, TemplateException {

        ProfEntity currentProf  = new ProfEntity();

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            /*
             * JSON from String to Object.
             * the following automatically map the values in the JSON.stringify to the variable with the same name in the class instance (gomTmp).
             * BTW: we put an id in the Gommette instance, the id is the one of the student, we juste store it there for now
             **/
            currentProf = mapper.readValue(body, ProfEntity.class);
        } catch ( Exception e ) {
            System.out.println(e);
        }

        String tmpSalt = BCrypt.gensalt();
        currentProf.setSalt(tmpSalt);
        String tmpHsPwd = BCrypt.hashpw(currentProf.getHashedPassword(), tmpSalt); // gotta call it hashedPassword in the json, even though it will not be hashed at first

        currentProf.setHashedPassword(tmpHsPwd);

        currentProf = ProfCore.create(currentProf);

        return "cool";
    }

    /*
     * Handle login = check if user exists in db and if so, write it in cookie "user"
     **/
    public static String login(String body) throws TemplateException, IOException {

        ArrayList<ProfEntity> profs = ProfCore.getAllUsers();

        ProfEntity currentProf  = new ProfEntity();

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            /*
             * JSON from String to Object.
             * the following automatically map the values in the JSON.stringify to the variable with the same name in the class instance (gomTmp).
             * BTW: we put an id in the Gommette instance, the id is the one of the student, we juste store it there for now
             **/
            currentProf = mapper.readValue(body, ProfEntity.class);

        } catch ( Exception e ) {
            System.out.println(e);
        }

        for (ProfEntity prof : profs){

            String tmp1 = BCrypt.hashpw(currentProf.getHashedPassword(), prof.getSalt());
            String tmp2 = prof.getHashedPassword();
            if (tmp1.equals(tmp2)){
                currentProf = prof;

                String json = new String();
                try {
                    // create `ObjectMapper` instance
                    ObjectMapper mapper2 = new ObjectMapper();

                    // create a JSON object
                    ObjectNode user = mapper2.createObjectNode();
                    user.put("id", currentProf.getId());
                    user.put("firstName", prof.getFirstName());
                    user.put("lastName", prof.getLastName());
                    user.put("hashedPassword", currentProf.getHashedPassword());

                    // convert `ObjectNode` to pretty-print JSON
                    // without pretty-print, use `user.toString()` method
                    json = mapper2.writerWithDefaultPrettyPrinter().writeValueAsString(user);

                    // print json
                    System.out.println(json);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                return json;
            }
        }
        return null;
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
