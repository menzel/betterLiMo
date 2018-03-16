package de.thm.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class ApplicationController {

    /**
     * Index/ Welcome page with four links
     *
     * @return index page
     */
    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String index(HttpSession session) {

        return "index";
    }

}
