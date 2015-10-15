package com.oberasoftware.home.rest;

import com.oberasoftware.home.api.managers.DashboardManager;
import com.oberasoftware.home.api.model.storage.Dashboard;
import com.oberasoftware.home.core.model.storage.DashboardImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Renze de Vries
 */
@RestController
@RequestMapping("/dashboards")
public class DashboardRestController {
    private static final Logger LOG = LoggerFactory.getLogger(DashboardRestController.class);

    @Autowired
    private DashboardManager dashboardManager;

    @RequestMapping("/")
    public List<Dashboard> findDashboards() {
        return dashboardManager.findDashboards();
    }

    @RequestMapping("/default")
    public Dashboard findDefaultDashboards() {
        return dashboardManager.findDefaultDashboard();
    }

    @RequestMapping(value = "/({dashboardId})")
    public Dashboard findDashboard(@PathVariable String dashboardId) {
        return dashboardManager.findDashboard(dashboardId);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Dashboard create(@RequestBody DashboardImpl dashboard) {
        return dashboardManager.store(dashboard);
    }

    @RequestMapping(value = "/({dashboardId})", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDashboard(@PathVariable String dashboardId) {
        LOG.debug("Deleting Dashboard: {}", dashboardId);
        dashboardManager.delete(dashboardId);
        LOG.debug("Deleted dashboard");
    }

}
