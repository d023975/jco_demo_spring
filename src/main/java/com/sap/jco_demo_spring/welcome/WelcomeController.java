package com.sap.jco_demo_spring.welcome;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @Autowired
    private Environment environment;
    private static final String strDefine = "Hello, %s!";

    @GetMapping("/welcome")
    public Welcome handleWelcome(@RequestParam(value = "name", defaultValue = "Enthusiast") String name) {
        return new Welcome(String.format(strDefine, name));
    }
}
