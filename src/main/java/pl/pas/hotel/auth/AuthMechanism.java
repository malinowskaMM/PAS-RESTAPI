package pl.pas.hotel.auth;

import io.jsonwebtoken.Claims;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class AuthMechanism implements HttpAuthenticationMechanism {

    private JwtGenerator generator = new JwtGenerator();

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpMessageContext httpMessageContext) throws AuthenticationException {
        String header = httpServletRequest.getHeader(AUTHORIZATION);
        Set<String> role = new HashSet<>();
        if(header != null) {
            if(header.startsWith(BEARER)) {
                String token = header.replace(BEARER, "");
                Claims claims = generator.parseJWT(token).getBody();
                role.add(claims.get("role", String.class));
                return httpMessageContext.notifyContainerAboutLogin(claims.getSubject(), role);
            }
        }
        role.add("NONE");
        return httpMessageContext.notifyContainerAboutLogin("none", role);
    }
}
