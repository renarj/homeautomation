package com.oberasoftware.home.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author renarj
 */
@Controller
@RequestMapping("/web")
public class WebController {

    @RequestMapping
    public String getIndex() {
        return "index";
    }
}
