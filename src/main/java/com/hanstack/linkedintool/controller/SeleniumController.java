package com.hanstack.linkedintool.controller;

import com.hanstack.linkedintool.config.SeleniumFactory;
import com.hanstack.linkedintool.dto.FilterDTO;
import com.hanstack.linkedintool.dto.LinkedinDTO;
import com.hanstack.linkedintool.dto.LoginDTO;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.servlet.http.HttpSession;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;

@Controller
@RequestMapping("/selenium")
public class SeleniumController {
//    private final WebDriver driver;
//    private final Wait<WebDriver> wait;
//
//    public SeleniumController(WebDriver driver, Wait<WebDriver> wait) {
//        this.driver = driver;
//        this.wait = wait;
//    }

    @PostMapping
    public String process(@ModelAttribute LinkedinDTO linkedinDTO, HttpSession httpSession, Model model) throws Exception {
        FilterDTO filterDTO = linkedinDTO.getFilterDTO();
        LoginDTO loginDTO = linkedinDTO.getLoginDTO();

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(new ChromeOptions().addArguments("--incognito"));
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        SeleniumFactory seleniumFactory = new SeleniumFactory(driver, wait);
        seleniumFactory.deleteAndImportCookies(linkedinDTO.getLoginDTO().getCookieFile());
        seleniumFactory.startLinkedin();
        seleniumFactory.signIn(loginDTO.getUsername(), loginDTO.getPassword());
        seleniumFactory.searchByFilter(filterDTO);
        driver.quit();
        model.addAttribute(httpSession.getAttribute("linkedinDTO"));
        return "index";
    }
}
