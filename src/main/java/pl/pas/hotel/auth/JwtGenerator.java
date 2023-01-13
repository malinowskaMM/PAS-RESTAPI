package pl.pas.hotel.auth;

import com.nimbusds.jose.JWSAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;

import java.util.Date;

public class JwtGenerator {

    private static final String SECRET = "09SEVheeEsOTYLDZAJylVmlHb4XadBtgABGKZB5wmKVexgWU";
    private static final long timeout = 9000000L;


    public String generateJWT(CredentialValidationResult result) {
        return Jwts.builder()
                .setSubject(result.getCallerPrincipal().getName())
                .claim("role", result.getCallerGroups().toString())
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public Jws<Claims> parseJWT(String jwt) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwt);
    }

}
