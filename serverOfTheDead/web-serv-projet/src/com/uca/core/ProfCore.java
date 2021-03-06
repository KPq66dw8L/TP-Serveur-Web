package com.uca.core;

import com.uca.dao.ProfDAO;
import com.uca.entity.ProfEntity;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

/*
 * ***
 * Model(s), traitements "métiers" (=intermédiaire entre ce que tu écris en base et ce que tu reçois/envoie aux clients)
 * ***
 **/
public class ProfCore {

    public static ArrayList<ProfEntity> getAllUsers() {

        return new ProfDAO().getAllUsers();
    }

    public static ProfEntity create(ProfEntity obj) {
        // TODO: empecher de creer un compte avec le meme username
        String tmpSalt = BCrypt.gensalt();
        obj.setSalt(tmpSalt);
        String tmpHsPwd = BCrypt.hashpw(obj.getHashedPassword(), tmpSalt); // got to call it hashedPassword in the json, even though it's not hashed at first
        obj.setHashedPassword(tmpHsPwd);

        ProfEntity tmp = new ProfDAO().create(obj);

        if (tmp != null) {
            return ProfCore.getUser(tmp.getUsername(), tmp.getHashedPassword());
        }
        return null;
    }

        public static ProfEntity getUser(String username, String rawPwd){
            ArrayList<ProfEntity> profs = new ProfDAO().getUser(username);
            /*
            L'utilisateur fourni un username et son mot de passe brut. Cependant, on stocke dans la db les hashed pwd.
            Donc on peut seulement récupérer toutes les entrées avec cet username dans la db. Et on doit donc
            ensuite verifier le mot de passe ici, ce que l'on peut faire puisque l'on recup aussi le salt ds la db.
            SOLUTION: empecher lors de l'inscription d'utiliser un username deja utilisé.
            * */
            for (ProfEntity prof : profs) {
                if (BCrypt.hashpw(rawPwd, prof.getSalt()).equals(prof.getHashedPassword())) {
                    return prof;
                }
            }
            return null;
        }

    public static boolean delete(int id, int id_to_del){
        if (id != id_to_del) {
            return new ProfDAO().deleteProf(id_to_del);
        }
        return false;
    }

}
