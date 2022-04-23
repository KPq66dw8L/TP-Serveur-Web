package com.uca.gui;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uca.core.ProfCore;
import com.uca.entity.ProfEntity;
import freemarker.template.TemplateException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * ***
 * Controller
 * ***
 **/
public class ProfGUI {

//    public static String getAllUsers() throws IOException, TemplateException {
//        Configuration configuration = _FreeMarkerInitializer.getContext();
//
//        Map<String, Object> input = new HashMap<>();
//
//        input.put("users", ProfCore.getAllUsers());
//
//        Writer output = new StringWriter();
//        Template template = configuration.getTemplate("users/register.ftl");
//        template.setOutputEncoding("UTF-8");
//        template.process(input, output);
//        return output.toString();
//    }

    // Handle part of the registration of a prof => create a new JAVA ProfEntity
    public static void create(String body) throws SQLException, IOException, TemplateException {

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
            e.printStackTrace();
        }

        String tmpSalt = BCrypt.gensalt();
        currentProf.setSalt(tmpSalt);
        String tmpHsPwd = BCrypt.hashpw(currentProf.getHashedPassword(), tmpSalt); // got to call it hashedPassword in the json, even though it will not be hashed at first

        currentProf.setHashedPassword(tmpHsPwd);

        ProfCore.create(currentProf);

    }

    // Handle login => check if user exists in the list of all Profs
    public static String login(String body){

        ArrayList<ProfEntity> profs = ProfCore.getAllUsers();

        ProfEntity currentProf  = new ProfEntity();

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // ignore if attribute of the currentProf instance are not filled
        try {
            // JSON from String to Object.
            currentProf = mapper.readValue(body, ProfEntity.class);

        } catch ( Exception e ) {
            e.printStackTrace();
        }

        for (ProfEntity prof : profs){

            String tmp1 = BCrypt.hashpw(currentProf.getHashedPassword(), prof.getSalt());
            String tmp2 = prof.getHashedPassword();
            if (tmp1.equals(tmp2)){
                currentProf = prof;

                String json = "";
                try {
                    // create `ObjectMapper` instance
                    ObjectMapper mapper2 = new ObjectMapper();

                    // create a JSON object
                    ObjectNode user = mapper2.createObjectNode();
                    user.put("id", currentProf.getId());
                    user.put("firstName", prof.getFirstName());
                    user.put("lastName", prof.getLastName());
                    user.put("username", prof.getUsername());
                    user.put("hashedPassword", currentProf.getHashedPassword());

                    // convert `ObjectNode` to pretty-print JSON
                    json = mapper2.writerWithDefaultPrettyPrinter().writeValueAsString(user);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                return json;
            }
        }
        return null;
    }


}
