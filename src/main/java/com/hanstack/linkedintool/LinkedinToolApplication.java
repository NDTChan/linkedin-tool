package com.hanstack.linkedintool;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;

@SpringBootApplication
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
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WebDriver getDriverInstance() {
        WebDriver driver = null;
        try {
            var chromeOptions = new ChromeOptions()
                    .addEncodedExtensions(Base64.getEncoder().encodeToString(drx.getContentAsByteArray()));

//            if (StringUtils.equals(env, ProfileEnum.DEV.getName())) {
//                driver = new ChromeDriver(chromeOptions);
//            } else {
            var clientConfig = ClientConfig.defaultConfig()
                        .baseUrl(new URL(rwDriverUrl))
                        .authenticateAs(new UsernameAndPassword(rwDriverUsername, rwDriverPassword));
            var executor = new HttpCommandExecutor(clientConfig);
//                chromeOptions.setCapability(CapabilityType.BROWSER_NAME, Browser.CHROME.browserName());
//                chromeOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.LINUX.name());
                driver = new RemoteWebDriver(executor, chromeOptions);
//            }
        } catch (IOException ex) {
            log.error("There is an error when init the webdriver instance", ex.getCause());
        }

        return driver;
    }

//    @Bean
//    public Wait<WebDriver> getWaitDriverInstance() {
//        return new WebDriverWait(getDriverInstance(), Duration.ofSeconds(2));
//    }

}
