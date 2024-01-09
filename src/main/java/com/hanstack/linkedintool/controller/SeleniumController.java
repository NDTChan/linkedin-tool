package com.hanstack.linkedintool.controller;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;

@Controller
@RequestMapping("/selenium")
public class SeleniumController {
    WebDriver driver;

    @GetMapping
    public String sync(Model model) {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.linkedin.com/");
        WebElement username = driver.findElement(By.id("session_key"));
        username.sendKeys("justducthanh105mta@gmail.com");
        WebElement password = driver.findElement(By.id("session_password"));
        password.sendKeys("Admin@10525597");

        WebElement submitBtn = driver.findElement(By.cssSelector("button[type=submit]"));
        submitBtn.submit();
        WebElement searchBar = driver.findElement(By.className("search-global-typeahead__input"));
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofMinutes(5));

        wait.until( d -> {
            searchBar.isDisplayed();
            return true;
        });

        searchBar.sendKeys("CTO");
        searchBar.sendKeys(Keys.ENTER);
        return "indexBasic";
    }
}
