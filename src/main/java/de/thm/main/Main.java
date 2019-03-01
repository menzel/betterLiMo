package de.thm.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Server
 *
 * Created by Michael Menzel on 3/2/16.
 */
@SpringBootApplication
@ComponentScan(basePackages = "de.thm.spring")
public class Main {

    public static void main(String[] args) {


        try {
            SpringApplication.run(Main.class, args);

        } catch (Exception e) {
            System.err.println(e);
        }

    }
}
