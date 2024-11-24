package ru.urfu.sv.studentvoice.services;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
public class WebDriverService {

    @Value("${urfu.user.username}")
    private String urfuUserName;
    @Value("${urfu.user.password}")
    private String urfuUserPassword;
    @Value("${urfu.auth.url}")
    private String urfuAuthUrl;
    @Value("${urfu.page.url}")
    private String urfuPageUrl;
    @Value("${modeus.url}")
    private String modeusUrl;
    @Value("${web.driver.log}")
    private String pathToLog;

    private final FirefoxOptions firefoxOptions;

    public WebDriverService(@Value("${web.driver.path}") String pathToDriver,
                            @Value("${web.driver.browser.binary}") String browserBinary) {

        System.setProperty("webdriver.gecko.driver", pathToDriver);
        firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(browserBinary);
        firefoxOptions.addArguments("-headless");
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.INFO);
    }

    public Optional<String> getModeusAuthToken() {
        final FirefoxDriverService firefoxDriverService = new GeckoDriverService.Builder()
                .withLogFile(new File(pathToLog))
                .build();

        final FirefoxDriver webDriver = new FirefoxDriver(firefoxDriverService, firefoxOptions);
        webDriver.manage()
                .timeouts()
                .pageLoadTimeout(Duration.ofSeconds(20))
                .implicitlyWait(Duration.ofSeconds(10));

        final String token;
        try {
            webDriver.get(urfuAuthUrl);
            log.info("Страница логина УрФУ загрузилась");

            webDriver.findElement(By.id("userNameInput")).sendKeys(urfuUserName);
            webDriver.findElement(By.id("passwordInput")).sendKeys(urfuUserPassword);
            webDriver.findElement(By.id("submitButton")).click();
            final WebDriverWait waitForAuth = new WebDriverWait(webDriver, Duration.ofSeconds(20));
            waitForAuth.until(driver -> {
                log.info(driver.getCurrentUrl());
                return driver.getCurrentUrl().equals(urfuPageUrl);
            });

            if (webDriver.getCurrentUrl().equals(urfuPageUrl)) {
                log.info("Логирование в УрФУ прошло успешно");
            } else {
                log.error("Логирование в УрФУ не прошло, прерываем процесс");
                return Optional.empty();
            }

            webDriver.get(modeusUrl);
            log.info("Страница Модеус загрузилась");

            final WebDriverWait waitForToken = new WebDriverWait(webDriver, Duration.ofSeconds(20));
            waitForToken.until(driver -> ((FirefoxDriver) driver).getSessionStorage().getItem("id_token") != null);
            token = webDriver.getSessionStorage().getItem("id_token");
            log.info(token == null ? "Не получилось получить токен аутентификации Модеус" : "Получили токен аутентификации Модеус");
        } catch (Exception e) {
            log.error("Ошибка в ходе работы веб драйвера", e);
            return Optional.empty();
        } finally {
            webDriver.manage().deleteAllCookies();
            webDriver.quit();
            log.info("Веб драйвер успешно закрыт");
        }

        return token == null ? Optional.empty() : Optional.of(token);
    }
}
