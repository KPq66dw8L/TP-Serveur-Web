package com.uca.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class doLogin {

    // mettre ca dans fichier de configuration a part/ ENV :
    private final static String TOKEN = "HIcOONpumSREn9Dn5lzMDICr0xYQIwL6B121ee3pIObGn6bWGjAtvzMXwYFOnkvvA3RRW9jmrH47WCzOUZZhTpP6Zw5Wk06tocTe6OP3zS4NZTUEoEqkDS4YnQ9pSlqP";

    public static Map<String, String> introspec(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(TOKEN).parseClaimsJws(token).getBody(); // on pourrait aussi récupérer le header
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("uuid", claims.get("uuid", String.class));
        map.put("firstName", claims.get("firstName", String.class));
        map.put("lastName", claims.get("lastName", String.class));
        map.put("username", claims.get("username", String.class));

        return map;
    }

    public static String createToken(int uuid, String firstName, String lastName, String username) {
        Map<String, String> content = new HashMap<>();
        content.put("uuid", String.valueOf(uuid));
        content.put("firstName", firstName);
        content.put("lastName", lastName);
        content.put("username", username);

        return Jwts.builder().setClaims(content)
                .setId(UUID.randomUUID().toString())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, TOKEN)
                .compact();
    }


}
