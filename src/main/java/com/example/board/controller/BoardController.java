package com.example.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Slf4j
@Controller
public class BoardController {

    @GetMapping("/items")
    public ModelAndView goIntroducingPage() {
        ModelAndView mv = new ModelAndView("items");
        mv.addObject("serverTime", "2022-07-16");
        return mv;
    }

}
