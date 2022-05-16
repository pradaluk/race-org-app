package cz.cvut.kbss.ear.race.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "cz.cvut.kbss.ear.race.service")
public class ServiceConfig {
}
