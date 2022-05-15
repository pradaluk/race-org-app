package cz.cvut.kbss.ear.race.model;

public enum CarClass {
    GT3("GT3"),GT4("GT4"), LMP1("LMP1"), LMP2("LMP2");

    private final String name;

    CarClass(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
