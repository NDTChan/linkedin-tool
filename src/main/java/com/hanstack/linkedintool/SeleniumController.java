package com.hanstack.linkedintool;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;

@Controller
@RequestMapping("/selenium")
public class SeleniumController {
    Logger logger = LoggerFactory.getLogger(SeleniumController.class);


    @GetMapping
    public String sync(Model model) {
        WebDriverManager wdm = WebDriverManager.chromedriver().browserInDocker();
        WebDriver driver = wdm.create();
        driver.get("https://www.selenium.dev/selenium/web/web-form.html");
        logger.info("title: {}", driver.getTitle());
        String driverTitle = driver.getTitle();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        WebElement textBox = driver.findElement(By.name("my-text"));
        WebElement submitButton = driver.findElement(By.cssSelector("button"));

        textBox.sendKeys("Selenium");
        submitButton.click();

        WebElement message = driver.findElement(By.id("message"));
        String messageDriver = message.getText();
        wdm.quit(driver);

        model.addAttribute("title", driverTitle);
        model.addAttribute("message", messageDriver);

        return "index";
    }
}
