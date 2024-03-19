package com.hanstack.linkedintool.controller;

import com.hanstack.linkedintool.dto.FilterDTO;
import com.hanstack.linkedintool.dto.LinkedinDTO;
import com.hanstack.linkedintool.dto.UploadFileDTO;
import com.hanstack.linkedintool.enums.ToolbarEnum;
import com.hanstack.linkedintool.service.SeleniumService;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Controller
@Slf4j
@RequestMapping("/selenium")
@AllArgsConstructor
public class SeleniumController {

    private final SeleniumService seleniumService;

    @PostMapping
    public String process(@ModelAttribute LinkedinDTO linkedinDTO, Model model) throws Exception {
        FilterDTO filterDTO = linkedinDTO.getFilterDTO();
        try {
            seleniumService.createNewDriver();
            seleniumService.startLinkedin();
            seleniumService.deleteAndImportCookies(linkedinDTO.getUploadFileDTO().getCookieFile());
            seleniumService.searchByFilter(filterDTO);

            model.addAttribute("lstFilterBarGrouping", ToolbarEnum.values());
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
        } finally {
            seleniumService.quit();
        }
        return "index";
    }

    @GetMapping("/upload-cookie")
    public String viewCookie(Model model, HttpServletRequest httpServletRequest, HttpSession httpSession) throws IOException {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(httpServletRequest);
        if (Objects.nonNull(inputFlashMap)) {
            model.addAttribute("uploaded", Boolean.TRUE);
        } else {
            model.addAttribute("uploaded", Boolean.FALSE);
        }

        UploadFileDTO uploadFileDTO = new UploadFileDTO();
        if (Objects.nonNull(httpSession.getAttribute("uploadFileDTO"))) {
            uploadFileDTO = (UploadFileDTO) httpSession.getAttribute("uploadFileDTO");
        }
        model.addAttribute("uploadFileDTO", uploadFileDTO);

        return "layout/cookie/index";
    }

    @PostMapping("/upload-cookie")
    public String uploadCookie(@Valid @ModelAttribute("uploadFileDTO") UploadFileDTO uploadFileDTO,
                               HttpServletRequest httpServletRequest,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               HttpSession httpSession,
                               Model model) {
        try {
            if (Objects.nonNull(uploadFileDTO.getCookieFile()) && uploadFileDTO.getCookieFile().isEmpty()) {
                result.rejectValue("cookieFile", "cookie.file.not.exist");
                return "layout/cookie/index";
            }
            uploadFileDTO.setFileByte(uploadFileDTO.getCookieFile().getBytes());
            uploadFileDTO.setFileName(uploadFileDTO.getCookieFile().getOriginalFilename());

            seleniumService.createNewDriver();
            seleniumService.startLinkedin();
            seleniumService.deleteAndImportCookies(uploadFileDTO.getCookieFile());
        } catch (TimeoutException ex) {
            log.error("ERROR: ", ex);
            result.rejectValue("cookieFile", "cookie.file.expired");
        } catch (IOException ex) {
            log.error("ERROR: ", ex);
        } finally {
            seleniumService.quit();
        }

        if (result.hasErrors()) {
            return "layout/cookie/index";
        }


        httpSession.setAttribute("uploadFileDTO", uploadFileDTO);
        redirectAttributes.addFlashAttribute("uploaded", Boolean.TRUE);
        redirectAttributes.addFlashAttribute("uploadFileDTO", uploadFileDTO);
        return "redirect:/selenium/upload-cookie";
    }
}
