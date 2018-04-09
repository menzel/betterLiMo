package de.thm.spring.controller;

import de.thm.spring.model.Lichen;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    // Error page
    @RequestMapping("/error.html")
    public String error(HttpServletRequest request, Model model) {
        model.addAttribute("errorCode", request.getAttribute("javax.servlet.error.status_code"));
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        String errorMessage = "";

        if (throwable != null) {
            errorMessage = throwable.getMessage();
        }

        if (errorMessage.equals("null") || errorMessage.length() == 0) {
            errorMessage = throwable.getClass().getCanonicalName();
        }

        model.addAttribute("errormessage", errorMessage);

        return "error";
    }



}
