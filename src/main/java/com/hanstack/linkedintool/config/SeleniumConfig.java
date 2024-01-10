package com.hanstack.linkedintool.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

//@Configuration
public class SeleniumConfig {
//    @Bean
//    WebDriver configWebDriver() {
//        WebDriverManager.chromedriver().setup();
//        return new ChromeDriver(new ChromeOptions().addArguments("--incognito"));
//    }
//
//    @Bean
//    Wait<WebDriver> configWaitWebDriver() {
//        return new WebDriverWait(configWebDriver(), Duration.ofSeconds(10));
//    }


}
