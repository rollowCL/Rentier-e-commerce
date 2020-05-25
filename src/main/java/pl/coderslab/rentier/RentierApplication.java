package pl.coderslab.rentier;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class RentierApplication extends SpringBootServletInitializer {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(RentierApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RentierApplication.class, args);

    }

}
