package cz.cvut.kbss.ear.race.model;

public enum TeamMembershipState {

    EXPIRED("MEMBERSHIP_EXPIRED"),
    ACTIVE("MEMBERSHIP_ACTIVE");

    private final String name;

    TeamMembershipState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}