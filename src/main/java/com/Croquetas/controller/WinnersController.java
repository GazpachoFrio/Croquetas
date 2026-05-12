package com.Croquetas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WinnersController {

    @GetMapping("/winners")
    public String winners() {
        return "winners";  // placeholder template
    }
}