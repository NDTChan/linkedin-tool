package com.hanstack.linkedintool.config;

import com.hanstack.linkedintool.dto.FilterDTO;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@AllArgsConstructor
public class SeleniumFactory {

    private final WebDriver driver;
    private final Wait<WebDriver> wait;

    public void startLinkedin() {
        driver.get("https://www.linkedin.com");
    }


    public void deleteAndImportCookies(MultipartFile cookieFile) throws Exception {
        driver.manage().deleteAllCookies();
        String originalWindow = driver.getWindowHandle();
        Thread.sleep(1000);
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("chrome-extension://okpidcojinmlaakglciglbpcpajaibco/popup.html?url=aHR0cHM6Ly93d3cubGlua2VkaW4uY29tLw%3D%3D");
        WebElement fileInput = driver.findElement(By.cssSelector("input[type=file]"));
        File tempFile = File.createTempFile("temp", null);
        cookieFile.transferTo(tempFile);
        fileInput.sendKeys(tempFile.toString());
        for (String windowHandle : driver.getWindowHandles()) {
            if (originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                driver.navigate().refresh();
            }
        }

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

    public void searchByFilter(FilterDTO filterDTO, HttpSession httpSession) throws Exception {
        By byGlobalSearch = By.className("search-global-typeahead__input");
        wait.until(ExpectedConditions.presenceOfElementLocated(byGlobalSearch));

        WebElement searchBarInp = driver.findElement(By.className("search-global-typeahead__input"));
        searchBarInp.sendKeys(filterDTO.getGlobalNavSearch());
        Thread.sleep(1000);
        searchBarInp.sendKeys(Keys.ENTER);

        String dynamicXpath = String.format("//button[@aria-pressed='false'][normalize-space()='%s']", filterDTO.getFilterBarGrouping().getName());
        By byGroupFilter = By.xpath(dynamicXpath);
        wait.until(ExpectedConditions.presenceOfElementLocated(byGroupFilter));

        driver.findElement(byGroupFilter).click();
    }

    public void allFilter(FilterDTO filterDTO) {
        By byAllFilter = By.xpath("//button[@aria-pressed='false'][normalize-space()='People]\"(//button[normalize-space()='All filters'])[1]\"undefined");
        wait.until(ExpectedConditions.presenceOfElementLocated(byAllFilter));

        driver.findElement(byAllFilter).click();

    }


}
