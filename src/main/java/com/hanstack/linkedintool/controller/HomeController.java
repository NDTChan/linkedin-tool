package com.hanstack.linkedintool.controller;

import com.hanstack.linkedintool.constant.ToolbarEnum;
import com.hanstack.linkedintool.model.FilterEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping
    public String home(Model model) {
        FilterEntity filterEntity = new FilterEntity();
        model.addAttribute("lstFilterBarGrouping", ToolbarEnum.values());
        model.addAttribute(filterEntity);
        return "index";
    }
}
