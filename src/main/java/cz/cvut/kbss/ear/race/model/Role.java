package cz.cvut.kbss.ear.race.model;

public enum Role {
    ADMIN("ROLE_ADMIN"),ORGANIZER("ROLE_ORGANIZER"), DRIVER("ROLE_DRIVER"), GUEST("ROLE_GUEST");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
