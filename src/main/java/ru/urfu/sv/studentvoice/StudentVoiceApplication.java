package ru.urfu.sv.studentvoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentVoiceApplication {

    public static void main(String[] args) {

        System.setProperty("web.driver.path", System.getenv("WEB_DRIVER_PATH"));
        System.setProperty("web.driver.log", System.getenv("WEB_DRIVER_LOG"));
        System.setProperty("web.driver.browser.binary", System.getenv("WEB_DRIVER_BINARY"));
        System.setProperty("spring.datasource.url", System.getenv("SPRING_DATASOURCE_URL"));
        System.setProperty("spring.datasource.username", System.getenv("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("spring.datasource.password", System.getenv("SPRING_DATASOURCE_PASSWORD"));
        System.setProperty("urfu.user.username", System.getenv("URFU_USER_USERNAME"));
        System.setProperty("urfu.user.password", System.getenv("URFU_USER_PASSWORD"));

        SpringApplication.run(StudentVoiceApplication.class, args);
    }
}
