package com.example.Project.Management.bayport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping(value = "/{path:[^\\.]*}", produces = "text/html")
    public String index() {
        return "forward:/index.html";
    }
}
