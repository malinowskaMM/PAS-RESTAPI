package pl.pas.hotel.model.user;

public enum AccessLevel {

    ADMIN,
    CLIENT,
    MANAGER,

    NONE;

    public String getAccessLevel() {
        return this.name();
    }
}
