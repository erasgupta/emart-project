package com.portal.emart.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/emart/web")
class HomeWebController {
	
	@Value("${spring.application.name}")
	String appName;

    //@Autowired
    //private UserService userService;

    @GetMapping("/home")
    public String homePage(Model model) {
    	model.addAttribute("appName", appName);
        return "home";
    }
    
    @GetMapping("/footer")
    public String footerPage(Model model) {
    	model.addAttribute("appName", appName);
        return "footer";
    }
    
    
}