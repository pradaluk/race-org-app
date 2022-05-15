package cz.cvut.kbss.ear.race;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.util.Date;

/**
 * Main entry point of a Spring Boot application.
 * <p>
 * Notice that it is structured as a regular command-line Java application - it has a {@code main} method.
 * <p>
 * The {@link SpringBootApplication} annotation enables auto-configuration of the Spring context. {@link
 * SpringApplication} then starts the Spring context and the whole application.
 */
@SpringBootApplication
public class RacingOrgApp {
    @Bean
    @Scope("prototype")
    public Date personPrototype() {
        return new Date();
    }
    public static void main(String[] args) {
        SpringApplication.run(RacingOrgApp.class, args);
    }
}
