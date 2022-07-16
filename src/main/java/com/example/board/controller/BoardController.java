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

    @GetMapping("/items")
    public ModelAndView goItems(HttpServletRequest req) {
        ModelAndView mv = new ModelAndView("items");
        String id = req.getParameter("id");
        log.info("id : " + id);
        mv.addObject("serverTime", "2022-07-16 15:00:00");
        mv.addObject("id", id);
        return mv;
    }

    @GetMapping("/items/list")
    public ModelAndView goItemsList() {
        ModelAndView mv = new ModelAndView("list/itemsList");
        mv.addObject("serverTime", "2022-07-16 15:00:00");
        return mv;
    }

    @GetMapping("/items2")
    public ModelAndView goItems2() {
        ModelAndView mv = new ModelAndView("items2");
        mv.addObject("serverTime", "2022-07-16 15:20:00");
        return mv;
    }

}
