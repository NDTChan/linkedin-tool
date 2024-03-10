package com.hanstack.linkedintool.service.impl;

import com.hanstack.linkedintool.dto.FilterDTO;
import com.hanstack.linkedintool.enums.ProfileEnum;
import com.hanstack.linkedintool.service.SeleniumService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class SeleniumServiceImpl implements SeleniumService {

    private final String env;
    private final WebDriver driver;
    private final Wait<WebDriver> wait;

    public SeleniumServiceImpl(
            @Value("${spring.profiles.active:}") String env,
            WebDriver driver,
            Wait<WebDriver> wait
    ) {
        this.env = env;
        this.driver = driver;
        this.wait = wait;
    }

    @Override
    public void startLinkedin() {
        driver.get("https://www.linkedin.com");
    }

    @Override
    public void deleteAndImportCookies(MultipartFile cookieFile) throws InterruptedException {
        try {
            driver.manage().deleteAllCookies();
            String originalWindow = driver.getWindowHandle();
            Thread.sleep(1000);
            driver.switchTo().newWindow(WindowType.TAB);
            driver.get("chrome-extension://okpidcojinmlaakglciglbpcpajaibco/popup.html?url=aHR0cHM6Ly93d3cubGlua2VkaW4uY29tLw%3D%3D");
            if (StringUtils.equals(env, ProfileEnum.PROD.getName())) {
                ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
            }
            WebElement fileInput = driver.findElement(By.cssSelector("input[type=file]"));
            File tempFile = File.createTempFile("temp", null);
            cookieFile.transferTo(tempFile);
            fileInput.sendKeys(tempFile.getAbsolutePath());
            for (String windowHandle : driver.getWindowHandles()) {
                if (originalWindow.contentEquals(windowHandle)) {
                    driver.switchTo().window(windowHandle);
                    driver.navigate().refresh();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void searchByFilter(FilterDTO filterDTO) {
        WebElement searchBarInp = driver.findElement(By.className("search-global-typeahead__input"));
        wait.until(d -> searchBarInp.isDisplayed());

        searchBarInp.sendKeys(filterDTO.getGlobalNavSearch());
        searchBarInp.sendKeys(Keys.ENTER);

        String dynamicXpath = String.format("//button[@aria-pressed='false'][normalize-space()='%s']", filterDTO.getFilterBarGrouping().getName());
        WebElement weGroupFilter = driver.findElement(By.xpath(dynamicXpath));
        wait.until(d -> weGroupFilter.isDisplayed());

        weGroupFilter.click();

    }

    @Override
    public void allFilter(FilterDTO filterDTO) {
        WebElement allFilter = driver.findElement(By.xpath("//button[@aria-pressed='false'][normalize-space()='People]\"(//button[normalize-space()='All filters'])[1]\"undefined"));
        wait.until(d -> allFilter.isDisplayed());

        allFilter.click();

    }

    @Override
    public void quit() {
        driver.quit();
    }

    @Override
    public void close() {
        driver.close();
    }
}
