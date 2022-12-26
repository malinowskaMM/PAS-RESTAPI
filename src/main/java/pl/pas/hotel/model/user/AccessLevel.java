package pl.pas.hotel.model.user;

public enum AccessLevel {

    ADMIN,
    CLIENT,
    MANAGER;

    public String getAccessLevel() {
        return this.name();
    }
}
