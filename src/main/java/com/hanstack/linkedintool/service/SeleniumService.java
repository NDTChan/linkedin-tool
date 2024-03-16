package com.hanstack.linkedintool.service;

import com.hanstack.linkedintool.dto.FilterDTO;
import org.openqa.selenium.WebDriver;
import org.springframework.web.multipart.MultipartFile;

public interface SeleniumService {

    void setDriver(WebDriver webDriver);

    void startLinkedin();

    void deleteAndImportCookies(MultipartFile cookieFile) throws InterruptedException;

    void searchByFilter(FilterDTO filterDTO);

    void allFilter(FilterDTO filterDTO);

    void quit();

    void close();
}
