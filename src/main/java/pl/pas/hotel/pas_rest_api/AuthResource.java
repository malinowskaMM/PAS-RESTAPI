package pl.pas.hotel.pas_rest_api;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pl.pas.hotel.auth.AuthIdentityStore;
import pl.pas.hotel.auth.JwtGenerator;
import pl.pas.hotel.dto.auth.AuthDto;

@Path("/login")
@RequestScoped
public class AuthResource {

    @Context
    private SecurityContext securityContext;

    @Inject
    AuthIdentityStore authIdentityStore;

    JwtGenerator jwtGenerator = new JwtGenerator();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"NONE"})
    public Response login(@NotNull AuthDto authDto) {
        UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredential(authDto.getLogin(), authDto.getPassword());
            CredentialValidationResult credentialValidationResult = authIdentityStore.validate(usernamePasswordCredential);

            if(credentialValidationResult.getStatus().equals(CredentialValidationResult.Status.VALID)) {
                String jwt = jwtGenerator.generateJWT(authDto.getLogin(), credentialValidationResult.getCallerGroups().iterator().next());
                return Response.ok(jwt).build();
            }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

}
