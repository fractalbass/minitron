package com.phg.minitron.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

/**
 * Created by milesporter on 5/11/17.
 */
@Controller
public class WebController {

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public  getMessage(@RequestParam String username, @RequestParam String passwd) {
        System.out.println("Login request with: ${username}, ${passwd}")
        return "ok"
    }
}