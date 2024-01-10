package com.hanstack.linkedintool.controller;

import com.hanstack.linkedintool.constant.ToolbarEnum;
import com.hanstack.linkedintool.dto.FilterDTO;
import com.hanstack.linkedintool.dto.LinkedinDTO;
import com.hanstack.linkedintool.dto.LoginDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping
    public String home(Model model, HttpSession httpSession) {
        FilterDTO filterDTO = FilterDTO.builder()
                .globalNavSearch("CEO")
                .filterBarGrouping(ToolbarEnum.PEOPLE)
                .build();
        LoginDTO loginDTO = LoginDTO.builder()
                .username("justducthanh105mta@gmail.com")
                .password("Admin@10525597")
                .build();
        LinkedinDTO linkedinDTO = LinkedinDTO.builder().filterDTO(filterDTO).loginDTO(loginDTO).build();

        model.addAttribute("lstFilterBarGrouping", ToolbarEnum.values());
        model.addAttribute(linkedinDTO);

        httpSession.setAttribute("linkedinDTO", linkedinDTO);
        return "index";
    }
}
