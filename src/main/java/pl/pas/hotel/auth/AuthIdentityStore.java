package pl.pas.hotel.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import pl.pas.hotel.managers.UserManager;
import pl.pas.hotel.model.user.User;

import java.util.Arrays;
import java.util.HashSet;

@ApplicationScoped
public class AuthIdentityStore implements IdentityStore {

    @Inject
    UserManager userManager;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePasswordCredential =
                    (UsernamePasswordCredential) credential;
            User user = userManager.findUserByLogin(usernamePasswordCredential.getCaller(),
                    usernamePasswordCredential.getPasswordAsString());
            if (user != null) {
                return new CredentialValidationResult(user.getLogin(), new HashSet<>(
                        Arrays.asList(user.getAccessLevel().name())));
            }
        }
        return CredentialValidationResult.INVALID_RESULT;

    }
}
