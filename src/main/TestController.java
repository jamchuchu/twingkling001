package com.sparta.twkingkling01;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {


    @GetMapping("/helloWorld")
    @ResponseBody
    public String Test(){
        return "HELLO WORLD";
    }
}
