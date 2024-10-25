package com.taidev198.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "demo";
    }

    @PostMapping
    public String post() {
        return "demo";
    }
}
