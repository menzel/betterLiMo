package de.thm.spring.controller;

import de.thm.spring.model.Lichen;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;

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
    public String error(ServletWebRequest request, Model model) {
        model.addAttribute("errorCode", request.getAttribute("javax.servlet.error.status_code",0));
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception",0);
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
