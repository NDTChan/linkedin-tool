package com.hanstack.linkedintool.controller;

import com.hanstack.linkedintool.config.SeleniumFactory;
import com.hanstack.linkedintool.dto.FilterDTO;
import com.hanstack.linkedintool.dto.LinkedinDTO;
import com.hanstack.linkedintool.dto.LoginDTO;
import com.hanstack.linkedintool.enums.ToolbarEnum;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.util.Base64;

@Controller
@Slf4j
@RequestMapping("/selenium")
public class SeleniumController {

    @Value("classpath:static/drx/j2team-cookies-ext.crx")
    Resource drx;

    @PostMapping
    public String process(@ModelAttribute LinkedinDTO linkedinDTO, HttpSession httpSession, Model model) throws Exception {
        FilterDTO filterDTO = linkedinDTO.getFilterDTO();
        LoginDTO loginDTO = linkedinDTO.getLoginDTO();
        WebDriverManager.chromedriver().setup();
        String base64 = Base64.getEncoder().encodeToString(drx.getContentAsByteArray());
//        WebDriver driver = new ChromeDriver(new ChromeOptions().addArguments("--incognito"));
        WebDriver driver = new ChromeDriver(new ChromeOptions().addEncodedExtensions(base64));
        driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(1));
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            SeleniumFactory seleniumFactory = new SeleniumFactory(driver, wait);
            seleniumFactory.startLinkedin();
            seleniumFactory.deleteAndImportCookies(linkedinDTO.getLoginDTO().getCookieFile());
            seleniumFactory.searchByFilter(filterDTO, httpSession);

            model.addAttribute("lstFilterBarGrouping", ToolbarEnum.values());
            model.addAttribute(httpSession.getAttribute("linkedinDTO"));
        } finally {
//            driver.quit();
        }
        return "index";
    }
}
