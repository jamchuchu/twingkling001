package com.sparta.twingkling001;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/test")
@Controller
public class TestController {


    @GetMapping("/helloWorld")
    @ResponseBody
    public String Test(){
        return "HELLO WORLD";
    }
}
