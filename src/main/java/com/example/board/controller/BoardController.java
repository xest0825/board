package com.example.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Controller
public class BoardController {

    @GetMapping("/board")
    public ModelAndView goItems(HttpServletRequest req) {
        ModelAndView mv = new ModelAndView("items");
        return mv;
    }

}
