package com.maybe.sys.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.maybe.sys.model.SysUser;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWToken {

    private static String SECRET = "K8F2H6Y5E2V9C5K7O5T2S4";

    public static String createToken(SysUser user) throws Exception {
        Date now = new Date();
        Calendar time = Calendar.getInstance();
        time.add(Calendar.HOUR, 5);
        Date exp = time.getTime();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        return JWT.create()
                .withHeader(map)
                .withClaim("id", user.getId())
                .withExpiresAt(exp)
                .withIssuedAt(now)
                .sign(Algorithm.HMAC256(SECRET));
    }


    public static Map<String, Claim> verifyToken(String token) throws Exception {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return verifier.verify(token).getClaims();
    }
}

