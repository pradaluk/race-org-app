package cz.cvut.kbss.ear.race.environment;

import cz.cvut.kbss.ear.race.model.Race;
import cz.cvut.kbss.ear.race.model.User;

import java.util.Random;

public class Generator {

    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    public static User generateUser() {
        final User user = new User();
        user.setFirstName("FirstName" + randomInt());
        user.setLastName("LastName" + randomInt());
        user.setUsername("username" + randomInt() + "@kbss.felk.cvut.cz");
        user.setPassword(Integer.toString(randomInt()));
        return user;
    }

    public static Race generateRace() {
        final Race r = new Race();
        r.setName("Race " + randomInt());
        return r;
    }
}
