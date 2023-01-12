package pl.pas.hotel.dto.auth;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeDto {

    @NotNull
    @Size(min = 5)
    String login;

    @NotNull
    @Size(min = 9)
    String oldPassword;

    @NotNull
    @Size(min = 9)
    String newPassword;

    @NotNull
    @Size(min = 9)
    String confirmNewPassword;

    @JsonbCreator
    public PasswordChangeDto(@JsonbProperty("login")String login, @JsonbProperty("oldPassword")String oldPassword, @JsonbProperty("newPassword")String newPassword, @JsonbProperty("confirmNewPassword")String confirmNewPassword) {
        this.login = login;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public PasswordChangeDto() {
    }
}
