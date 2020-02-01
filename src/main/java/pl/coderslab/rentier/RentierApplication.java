package pl.coderslab.rentier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.charset.Charset;

@SpringBootApplication
public class RentierApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentierApplication.class, args);
        System.out.println(Charset.defaultCharset().displayName());

    }

}
