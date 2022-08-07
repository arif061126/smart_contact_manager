package com.smartContactManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class NormalUserController {

    @GetMapping("/index")
    public String userDashboard(){
        return "user/index";
    }
}
