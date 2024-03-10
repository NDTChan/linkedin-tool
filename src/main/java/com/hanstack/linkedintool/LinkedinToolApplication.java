package com.hanstack.linkedintool;

import com.hanstack.linkedintool.enums.ProfileEnum;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Base64;

@SpringBootApplication
@Configuration
@Slf4j
public class LinkedinToolApplication {

    @Value("classpath:static/drx/j2team-cookies-ext.crx")
    Resource drx;

    @Value("${remote.web-driver.url}")
    private String rwDriverUrl;

    @Value("${remote.web-driver.username}")
    private String rwDriverUsername;

    @Value("${remote.web-driver.password}")
    private String rwDriverPassword;

    @Value("${spring.profiles.active:}")
    private String env;

    public static void main(String[] args) {
        SpringApplication.run(LinkedinToolApplication.class, args);
        WebDriverManager.chromedriver().setup();
    }

    @Bean
    public WebDriver getDriverInstance() {
        WebDriver driver = null;
        try {
            var chromeOptions = new ChromeOptions()
                    .addEncodedExtensions(Base64.getEncoder().encodeToString(drx.getContentAsByteArray()));

            if (StringUtils.equals(env, ProfileEnum.DEV.getName())) {
                driver = new ChromeDriver(chromeOptions);
            } else {
                ClientConfig clientConfig = ClientConfig.defaultConfig()
                        .baseUrl(new URL(rwDriverUrl))
                        .authenticateAs(new UsernameAndPassword(rwDriverUsername, rwDriverPassword));
                HttpCommandExecutor executor = new HttpCommandExecutor(clientConfig);

                chromeOptions.setCapability(CapabilityType.BROWSER_NAME, Browser.CHROME.browserName());
                chromeOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.LINUX.name());
                driver = new RemoteWebDriver(executor, chromeOptions);
            }
        } catch (IOException ex) {
            log.error("There is an error when init the webdriver instance", ex.getCause());
        }

        return driver;
    }

    @Bean
    public Wait<WebDriver> getWaitDriverInstance() {
        WebDriver driver = getDriverInstance();
        return new WebDriverWait(driver, Duration.ofSeconds(2));
    }

}
