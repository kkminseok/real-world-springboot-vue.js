package com.io.realworld.api;

import com.io.realworld.DTO.UserSignupRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class MainController {

    @GetMapping("")
    public String mainHome(){
        return "index";
    }

    @GetMapping("/register")
    public String signupView(){
        return "/users/signup";
    }

}
