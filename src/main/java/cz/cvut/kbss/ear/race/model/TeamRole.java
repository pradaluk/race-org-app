package cz.cvut.kbss.ear.race.model;

public enum TeamRole {

    CREATOR("creator"), MEMBER("member");

    private final String name;

    TeamRole(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}