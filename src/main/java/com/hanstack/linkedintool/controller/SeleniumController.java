package com.hanstack.linkedintool.controller;

import com.hanstack.linkedintool.dto.FilterDTO;
import com.hanstack.linkedintool.dto.LinkedinDTO;
import com.hanstack.linkedintool.dto.UploadFileDTO;
import com.hanstack.linkedintool.enums.ToolbarEnum;
import com.hanstack.linkedintool.service.SeleniumService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.TimeoutException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Objects;

@Controller
@Slf4j
@RequestMapping("/selenium")
@AllArgsConstructor
public class SeleniumController {

    private final SeleniumService seleniumService;

    @PostMapping
    public String process(@ModelAttribute LinkedinDTO linkedinDTO, HttpSession httpSession, Model model) throws Exception {
        FilterDTO filterDTO = linkedinDTO.getFilterDTO();
        try {
            seleniumService.createNewDriver();
            seleniumService.startLinkedin();
            seleniumService.deleteAndImportCookies(linkedinDTO.getUploadFileDTO().getCookieFile());
            seleniumService.searchByFilter(filterDTO);

            model.addAttribute("lstFilterBarGrouping", ToolbarEnum.values());
            model.addAttribute(httpSession.getAttribute("linkedinDTO"));
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
        } finally {
            seleniumService.quit();
        }
        return "index";
    }

    @GetMapping("/upload-cookie")
    public String viewCookie(Model model, HttpSession httpSession) {
        UploadFileDTO uploadFileDTO = new UploadFileDTO();
        if (Objects.nonNull(httpSession.getAttribute("file"))) {
            MultipartFile cookieFile = (MultipartFile) httpSession.getAttribute("file");
            if (!cookieFile.isEmpty()) {
                uploadFileDTO.setCookieFile(cookieFile);
            }
        } else {
            uploadFileDTO.setCookieFile(null);
        }
        model.addAttribute("uploaded", Boolean.FALSE);
        model.addAttribute("uploadFileDTO", uploadFileDTO);

        return "layout/cookie/index";
    }

    @PostMapping("/upload-cookie")
    public String uploadCookie(@Valid @ModelAttribute("uploadFileDTO") UploadFileDTO uploadFileDTO,
                               final HttpSession httpSession,
                               final BindingResult result,
                               final RedirectAttributes redirectAttributes,
                               final Model model) {
        if (Objects.nonNull(uploadFileDTO.getCookieFile()) && uploadFileDTO.getCookieFile().isEmpty()) {
            result.rejectValue("cookieFile", "cookie.file.not.exist");
            return "layout/cookie/index";
        }
        try {
            seleniumService.createNewDriver();
            seleniumService.startLinkedin();
            seleniumService.deleteAndImportCookies(uploadFileDTO.getCookieFile());
            httpSession.setAttribute("file", uploadFileDTO.getCookieFile());
            model.addAttribute("uploaded", Boolean.TRUE);
        } catch (TimeoutException ex) {
            log.error("ERROR: ", ex);
            result.rejectValue("cookieFile", "cookie.file.expired");
        } catch (IOException ex) {
            log.error("ERROR: ", ex);
        } finally {
            seleniumService.quit();
        }
        return "layout/cookie/index";
    }
}
