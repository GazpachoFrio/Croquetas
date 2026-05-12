package com.Croquetas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EvaluationController {

    @GetMapping("/evaluate")
    public String evaluate() {
        return "evaluate";
    }
}