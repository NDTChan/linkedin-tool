package com.hanstack.linkedintool.config;

import com.hanstack.linkedintool.dto.FilterDTO;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
public class SeleniumFactory {

    private final WebDriver driver;
    private final Wait<WebDriver> wait;

    @FindBy(id = "session_key")
    private WebElement usernameInp;
    @FindBy(id = "session_password")
    private WebElement passwordInp;
    @FindBy(css = "button[type=submit]")
    private WebElement submitBtn;
    @FindBy(className = "search-global-typeahead__input")
    private WebElement searchBarInp;

    Logger logger = LoggerFactory.getLogger(SeleniumFactory.class);

    public SeleniumFactory(WebDriver driver, Wait<WebDriver> wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(this.driver, this);
    }

    public void startLinkedin() {
        driver.get("https://www.linkedin.com");
    }


    public void deleteAndImportCookies(MultipartFile cookieFile) throws Exception {
//        driver.manage().deleteAllCookies();

//        JSONParser parser = new JSONParser(cookieFile.getInputStream());
//        var lstLinkHashMap = (ArrayList) ((LinkedHashMap) parser.parse()).get("cookies");
//        for (Object o : lstLinkHashMap) {
//            var linkedHashMap = (LinkedHashMap) o;
//            String name = linkedHashMap.get("name").toString();
//            String value = linkedHashMap.get("value").toString();
////            String domain = linkedHashMap.get("domain").toString();
//            String domain = ".linkedin.com";
//            String path = linkedHashMap.get("path").toString();
//            Date expirationDate = new Date();
//            if (Objects.nonNull(linkedHashMap.get("expirationDate"))) {
//                logger.info("TIME OF expirationDate: {}", linkedHashMap.get("expirationDate"));
//                try {
//                    expirationDate.setTime(((BigDecimal) linkedHashMap.get("expirationDate")).longValue());
//                } catch ( Exception e) {
//                    expirationDate.setTime(Long.parseLong(linkedHashMap.get("expirationDate").toString()));
//                }
//            }
//            boolean isSecure = Boolean.parseBoolean(linkedHashMap.get("secure").toString());
//            boolean isHttpOnly = Boolean.parseBoolean(linkedHashMap.get("httpOnly").toString());
//            String sameSite = linkedHashMap.get("sameSite").toString();
//            Cookie cookie = new Cookie(name, value, domain, path, expirationDate, isSecure, isHttpOnly, sameSite);
//            try {
//                driver.manage().addCookie(cookie);
//            } catch (Exception e) {
//                logger.info("Error in {}", cookie.toString());
//                logger.error(e.getMessage());
//            }
//
//        }
    }

    public void signIn(String username, String password) throws Exception {
        usernameInp.sendKeys(username);
        passwordInp.sendKeys(password);
        Thread.sleep(1000);
        submitBtn.click();
    }

    public void searchByFilter(FilterDTO filterDTO, HttpSession httpSession) throws Exception {
        By byGlobalSearch = By.className("search-global-typeahead__input");
        wait.until(ExpectedConditions.presenceOfElementLocated(byGlobalSearch));

//        Set<Cookie> cookies = driver.manage().getCookies();
//        httpSession.setAttribute("cookie", cookies);

        searchBarInp.sendKeys(filterDTO.getGlobalNavSearch());
        Thread.sleep(1000);
        searchBarInp.sendKeys(Keys.ENTER);

        String dynamicXpath = String.format("//button[@aria-pressed='false'][normalize-space()='%s']", filterDTO.getFilterBarGrouping().getName());
        By byGroupFilter = By.xpath(dynamicXpath);
        wait.until(ExpectedConditions.presenceOfElementLocated(byGroupFilter));

        driver.findElement(byGroupFilter).click();
    }

}
