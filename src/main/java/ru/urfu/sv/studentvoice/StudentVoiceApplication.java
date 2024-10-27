package ru.urfu.sv.studentvoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;


@SpringBootApplication
public class StudentVoiceApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("web.driver.path", dotenv.get("WEB_DRIVER_PATH"));
        System.setProperty("web.driver.log", dotenv.get("WEB_DRIVER_LOG"));
        System.setProperty("web.driver.browser.binary", dotenv.get("WEB_DRIVER_BINARY"));
        System.setProperty("spring.datasource.url", dotenv.get("SPRING_DATASOURCE_URL"));
        System.setProperty("spring.datasource.username", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("spring.datasource.password", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
        System.setProperty("urfu.user.username", dotenv.get("URFU_USER_USERNAME"));
        System.setProperty("urfu.user.password", dotenv.get("URFU_USER_PASSWORD"));

        SpringApplication.run(StudentVoiceApplication.class, args);
    }
}
