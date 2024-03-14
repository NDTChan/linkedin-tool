package com.hanstack.linkedintool.controller;

import com.hanstack.linkedintool.dto.FilterDTO;
import com.hanstack.linkedintool.dto.LinkedinDTO;
import com.hanstack.linkedintool.dto.LoginDTO;
import com.hanstack.linkedintool.enums.ToolbarEnum;
import com.hanstack.linkedintool.service.SeleniumService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/selenium")
@AllArgsConstructor
public class SeleniumController {

    private final SeleniumService seleniumService;
    @PostMapping
    public String process(@ModelAttribute LinkedinDTO linkedinDTO, HttpSession httpSession, Model model) throws Exception {
        FilterDTO filterDTO = linkedinDTO.getFilterDTO();
        LoginDTO loginDTO = linkedinDTO.getLoginDTO();
        try {
            seleniumService.startLinkedin();
            seleniumService.deleteAndImportCookies(linkedinDTO.getLoginDTO().getCookieFile());
            seleniumService.searchByFilter(filterDTO);

            model.addAttribute("lstFilterBarGrouping", ToolbarEnum.values());
            model.addAttribute(httpSession.getAttribute("linkedinDTO"));
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
        } finally {
            log.info("Close only the current browser window");
            seleniumService.quit();
        }
        return "index";
    }
}
