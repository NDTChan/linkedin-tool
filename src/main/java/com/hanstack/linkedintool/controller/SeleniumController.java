package com.hanstack.linkedintool.controller;

import com.hanstack.linkedintool.config.SeleniumFactory;
import com.hanstack.linkedintool.constant.ToolbarEnum;
import com.hanstack.linkedintool.dto.FilterDTO;
import com.hanstack.linkedintool.dto.LinkedinDTO;
import com.hanstack.linkedintool.dto.LoginDTO;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.util.Arrays;

@Controller
@Slf4j
@RequestMapping("/selenium")
public class SeleniumController {
    //    private final WebDriver driver;
//    private final Wait<WebDriver> wait;
//
//    public SeleniumController(WebDriver driver, Wait<WebDriver> wait) {
//        this.driver = driver;
//        this.wait = wait;
//    }
    Logger logger = LoggerFactory.getLogger(SeleniumController.class);

    @PostMapping
    public String process(@ModelAttribute LinkedinDTO linkedinDTO, HttpSession httpSession, Model model) throws Exception {
        FilterDTO filterDTO = linkedinDTO.getFilterDTO();
        LoginDTO loginDTO = linkedinDTO.getLoginDTO();

        WebDriverManager.chromedriver().setup();
//        WebDriver driver = new ChromeDriver(new ChromeOptions().addArguments("--incognito"));
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(1));
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            SeleniumFactory seleniumFactory = new SeleniumFactory(driver, wait);
            seleniumFactory.deleteAndImportCookies(linkedinDTO.getLoginDTO().getCookieFile());
            seleniumFactory.startLinkedin();
            seleniumFactory.signIn(loginDTO.getUsername(), loginDTO.getPassword());
            seleniumFactory.searchByFilter(filterDTO, httpSession);

            model.addAttribute("lstFilterBarGrouping", ToolbarEnum.values());
            model.addAttribute(httpSession.getAttribute("linkedinDTO"));
        } finally {
//            driver.quit();
        }
        return "index";
    }
}
