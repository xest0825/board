package com.example.nav;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class PageController {

    @GetMapping("/board")
    public ModelAndView goItems(HttpServletRequest req) {
        log.info("/board");
        ModelAndView mv = new ModelAndView("items");
        return mv;
    }

    @GetMapping("/code")
    public ModelAndView goCodes(HttpServletRequest req) {
        log.info("/code");
        ModelAndView mv = new ModelAndView("code");
        return mv;
    }

    @GetMapping("/app-test")
    public ModelAndView goAppTest(HttpServletRequest req) {
        log.info("/app-test");
        ModelAndView mv = new ModelAndView("app_test");
        return mv;
    }

    @GetMapping("/join")
    public ModelAndView goJoin(HttpServletRequest req) {
        log.info("/join");
        ModelAndView mv = new ModelAndView("join");
        return mv;
    }

}
