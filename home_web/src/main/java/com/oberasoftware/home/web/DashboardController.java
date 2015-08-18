package com.oberasoftware.home.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author renarj
 */
@Controller
@RequestMapping("/")
public class DashboardController {
    @RequestMapping("/web")
    public String getIndex() {
        return "index";
    }

    @RequestMapping
    public String getRoot() {
        return "redirect:/web";
    }


}
