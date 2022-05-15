package cz.cvut.kbss.ear.race.model;

public enum JoinRequestStatus {

    PENDING("REQUEST_PENDING"), ACCEPTED("REQUEST_ACCEPTED"), REJECTED("REQUEST_REJECTED");

    private final String name;

    JoinRequestStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
