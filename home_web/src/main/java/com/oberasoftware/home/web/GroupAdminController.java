package com.oberasoftware.home.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Renze de Vries
 */
@Controller
@RequestMapping("/web/admin/groups")
public class GroupAdminController {

    @RequestMapping
    public String getGroups() {
        return "admin/groups";
    }
}
