package pl.coderslab.rentier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.coderslab.rentier.utils.EmailUtil;

import java.nio.charset.Charset;

@SpringBootApplication
public class RentierApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentierApplication.class, args);


    }

}
