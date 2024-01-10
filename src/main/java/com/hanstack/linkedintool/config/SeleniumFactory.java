package com.hanstack.linkedintool.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hanstack.linkedintool.dto.FilterDTO;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.tomcat.util.json.JSONParser;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

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

    public SeleniumFactory(WebDriver driver, Wait<WebDriver> wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(this.driver, this);
    }

    public void startLinkedin() {
        driver.get("https://www.linkedin.com/");
    }


    public void deleteAndImportCookies(MultipartFile cookieFile) throws Exception {
        driver.manage().deleteAllCookies();
//        File file = new File("/static/file.tmp");
//        cookieFile.transferTo(file);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readValue(cookieFile.getInputStream(), JsonNode.class);
//        jsonNode.get().

        JSONParser parser = new JSONParser(cookieFile.getInputStream());
        var lstLinkHashMap = (ArrayList) ((LinkedHashMap) parser.parse()).get("cookies");
        for (Object o : lstLinkHashMap) {
            var linkedHashMap = (LinkedHashMap) o;
            String name = linkedHashMap.get("name").toString();
            String value = linkedHashMap.get("value").toString();
            String domain = linkedHashMap.get("domain").toString();
            String path = linkedHashMap.get("path").toString();
            Date expirationDate = new Date();
            expirationDate.setTime(((BigDecimal) linkedHashMap.get("expirationDate")).longValue());
            boolean isSecure = Boolean.parseBoolean(linkedHashMap.get("secure").toString());
            boolean isHttpOnly = Boolean.parseBoolean(linkedHashMap.get("httpOnly").toString());
            String sameSite = linkedHashMap.get("sameSite").toString();
            driver.manage().addCookie(new Cookie(name, value, "www.linkedin.com", path, expirationDate, isSecure, isHttpOnly, sameSite));
        }
//        JsonArray arrCookies = (JsonArray) jsonObject.get("cookies");
//        arrCookies.forEach( cookie -> {
//            JsonObject obj = cookie.getAsJsonObject();
//            String name = obj.get("name").getAsString();
//            String value = obj.get("value").getAsString();
//            String domain = obj.get("domain").getAsString();
//            String path = obj.get("path").getAsString();
//            Date expirationDate = new Date();
//            expirationDate.setTime(obj.get("expirationDate").getAsLong());
//            boolean isSecure = obj.get("secure").getAsBoolean();
//            boolean isHttpOnly = obj.get("httpOnly").getAsBoolean();
//            String sameSite = obj.get("sameSite").getAsString();
//            driver.manage().addCookie(new Cookie(name, value, domain, path, expirationDate, isSecure, isHttpOnly, sameSite));
//        });
    }

    public void signIn(String username, String password) throws Exception {
        usernameInp.sendKeys(username);
        passwordInp.sendKeys(password);
        Thread.sleep(1000);
        submitBtn.click();
    }

    public void searchByFilter(FilterDTO filterDTO) throws Exception {
        wait.until(d -> searchBarInp.isDisplayed());
        searchBarInp.sendKeys(filterDTO.getGlobalNavSearch());
        Thread.sleep(1000);
        searchBarInp.sendKeys(Keys.ENTER);

        String dynamicXpath = String.format("//button[@aria-pressed='false'][normalize-space()='%s']", filterDTO.getFilterBarGrouping().getName());
        By byGroupFilter = By.xpath(dynamicXpath);
        wait.until(ExpectedConditions.presenceOfElementLocated(byGroupFilter));

        driver.findElement(byGroupFilter).click();
    }

}
