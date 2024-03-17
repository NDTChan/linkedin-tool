package com.hanstack.linkedintool.service;

import com.hanstack.linkedintool.dto.FilterDTO;
import org.openqa.selenium.TimeoutException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SeleniumService {

    void createNewDriver();

    void startLinkedin();

    void deleteAndImportCookies(MultipartFile cookieFile) throws IOException, TimeoutException;

    void searchByFilter(FilterDTO filterDTO);

    void allFilter(FilterDTO filterDTO);

    void quit();

    void close();
}
