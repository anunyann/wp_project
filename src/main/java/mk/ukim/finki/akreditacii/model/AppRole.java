package mk.ukim.finki.akreditacii.model;

public enum AppRole {
    PROFESSOR, ADMIN, GUEST;


    public String roleName() {
        return "ROLE_" + this.name();
    }
}
