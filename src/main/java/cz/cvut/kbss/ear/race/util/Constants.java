package cz.cvut.kbss.ear.race.util;

import cz.cvut.kbss.ear.race.model.Role;

public final class Constants {

    /**
     * Default user role.
     */
    public static final Role DEFAULT_ROLE = Role.GUEST;

    private Constants() {
        throw new AssertionError();
    }
}
