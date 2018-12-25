package com.emojicipher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ActionController {

    @RequestMapping("/")
    public String home() {
        return "en/index";
    }

    @RequestMapping("/about")
    public String about() {
        return "en/about";
    }

    @RequestMapping("/zh-CN")
    public String cnIndex() {
        return "zh-CN/index";
    }

    @RequestMapping("/zh-CN/about")
    public String cnAbout() {
        return "zh-CN/about";
    }

    @RequestMapping("/zh-TW")
    public String twIndex() {
        return "zh-TW/index";
    }

    @RequestMapping("/zh-TW/about")
    public String twAbout() {
        return "zh-TW/about";
    }

    @ResponseBody
    @RequestMapping("/robots.txt")
    public String robots() {
        return "User-agent: *\n" +
                "Disallow:";
    }

}
