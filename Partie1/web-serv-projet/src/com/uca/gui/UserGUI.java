package com.uca.gui;

import com.uca.core.UserCore;
import com.uca.dao.UserDAO;
import com.uca.entity.UserEntity;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

/*
 * ***
 * Controller
 * ***
 **/
public class UserGUI {

    public static String getAllUsers() throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        Map<String, Object> input = new HashMap<>();

        input.put("users", UserCore.getAllUsers());

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/users.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }

    public static UserEntity create(String firstname, String lastname, String prof, String blanche, String rouge, String verte) throws SQLException, IOException, TemplateException {

        UserEntity newUser = new UserEntity();

        newUser.setFirstName(firstname);
        newUser.setLastName(lastname);
        newUser.setProf(prof);
        newUser.setBlanche(parseInt(blanche));
        newUser.setRouge(parseInt(rouge));
        newUser.setVerte(parseInt(verte));

        newUser = UserCore.create(newUser);

        //System.out.println("Etape 5");

        return newUser;
    }

    public static UserEntity delete(String firstname, String lastname) throws SQLException{

        UserEntity newUser = new UserEntity();

        newUser.setFirstName(firstname);
        newUser.setLastName(lastname);

        //newUser = UserCore.detete(firstname, lastname);
        UserCore.delete(firstname, lastname);

        //System.out.println("Etape 5");

        return newUser;
    }
}
