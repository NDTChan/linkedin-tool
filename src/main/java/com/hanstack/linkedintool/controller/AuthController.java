package com.hanstack.linkedintool.controller;

import com.hanstack.linkedintool.dto.FilterDTO;
import com.hanstack.linkedintool.dto.LinkedinDTO;
import com.hanstack.linkedintool.dto.UserDTO;
import com.hanstack.linkedintool.enums.ToolbarEnum;
import com.hanstack.linkedintool.model.User;
import com.hanstack.linkedintool.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class AuthController {

    private UserService userService;

    @GetMapping("/home")
    public String home(Model model, HttpSession httpSession) {
        FilterDTO filterDTO = FilterDTO.builder()
                .globalNavSearch("CEO")
                .filterBarGrouping(ToolbarEnum.PEOPLE)
                .build();

        LinkedinDTO linkedinDTO = LinkedinDTO.builder().filterDTO(filterDTO).build();

        model.addAttribute("lstFilterBarGrouping", ToolbarEnum.values());
        model.addAttribute(linkedinDTO);

        httpSession.setAttribute("linkedinDTO", linkedinDTO);
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "layout/auth/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "layout/auth/forgot-password";
    }

    // handler method to handle user registration request
    @GetMapping("register")
    public String showRegistrationForm(Model model){
        UserDTO user = new UserDTO();
        model.addAttribute("user", user);
        return "layout/auth/register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDTO user,
                               BindingResult result,
                               Model model){
        User existingEmail = userService.findByEmail(user.getEmail());
        if (existingEmail != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "layout/auth/register";
        }
        userService.saveUser(user);
        return "redirect:/login";
    }
}
