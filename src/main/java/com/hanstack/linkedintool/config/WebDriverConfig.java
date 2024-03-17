package com.hanstack.linkedintool.config;

import com.hanstack.linkedintool.enums.ProfileEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Base64;

@Slf4j
@Component
public class WebDriverConfig {

    private static Resource drx;
    private static String rwDriverUrl;
    private static String rwDriverUsername;
    private static String rwDriverPassword;
    private static String env;

    public WebDriverConfig(@Value("classpath:static/drx/j2team-cookies-ext.crx") Resource drx,
                           @Value("${remote.web-driver.url}") String rwDriverUrl,
                           @Value("${remote.web-driver.username}") String rwDriverUsername,
                           @Value("${remote.web-driver.password}") String rwDriverPassword,
                           @Value("${spring.profiles.active:}") String env) {
        WebDriverConfig.drx = drx;
        WebDriverConfig.rwDriverUrl = rwDriverUrl;
        WebDriverConfig.rwDriverUsername = rwDriverUsername;
        WebDriverConfig.rwDriverPassword = rwDriverPassword;
        WebDriverConfig.env = env;
    }

    public static WebDriver getDriverInstance() {
        WebDriver driver = null;
        try {
            var chromeOptions = new ChromeOptions()
                    .addEncodedExtensions(Base64.getEncoder().encodeToString(drx.getContentAsByteArray()));

            if (StringUtils.equals(env, ProfileEnum.DEV.getName())) {
                driver = new ChromeDriver(chromeOptions);
            } else {
                var clientConfig = ClientConfig.defaultConfig()
                        .baseUrl(new URL(rwDriverUrl))
                        .authenticateAs(new UsernameAndPassword(rwDriverUsername, rwDriverPassword));
                var executor = new HttpCommandExecutor(clientConfig);
                driver = new RemoteWebDriver(executor, chromeOptions);
            }
        } catch (IOException ex) {
            log.error("There is an error when init the webdriver instance", ex.getCause());
        }

        return driver;
    }

    public static Wait<WebDriver> getWaitDriverInstance(WebDriver webDriver) {
        return new WebDriverWait(webDriver, Duration.ofSeconds(5));
    }
}
