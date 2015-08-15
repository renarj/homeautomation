package com.oberasoftware.home.rules.triggers;

import com.oberasoftware.home.rules.RuleEngine;
import com.oberasoftware.home.rules.api.general.Rule;
import com.oberasoftware.home.rules.api.trigger.TimeTrigger;
import com.oberasoftware.home.rules.api.trigger.Trigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class CronTriggerProcessor implements TriggerProcessor {
    private static final Logger LOG = getLogger(CronTriggerProcessor.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private CronFactory cronFactory;

    @Autowired
    private RuleEngine ruleEngine;

    private Map<String, JobKey> scheduledRules = new ConcurrentHashMap<>();

    @Override
    public void register(Trigger trigger, Rule rule) {
        if(trigger instanceof TimeTrigger) {
            TimeTrigger timeTrigger = (TimeTrigger) trigger;
            String cron = timeTrigger.getCron();

            LOG.debug("Scheduling rule: {} evaluation using cron expression: {}", rule, cron);

            String cronId = "Cron: " + cron + " Rule: " + rule.getId();

            try {
                MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
                jobDetailFactoryBean.setTargetObject((CronJob) () -> evalRule(rule.getId()));
                jobDetailFactoryBean.setTargetMethod("eval");
                jobDetailFactoryBean.setName("Job " + cronId);
                jobDetailFactoryBean.setConcurrent(false);
                jobDetailFactoryBean.afterPropertiesSet();


                JobDetail jobDetail = jobDetailFactoryBean.getObject();
                JobKey jobKey = jobDetail.getKey();

                CronTriggerFactoryBean tr = cronFactory.cronTriggerFactoryBean(cronId, cron, jobDetail);

                scheduler.scheduleJob(jobDetail, tr.getObject());
                scheduledRules.put(rule.getId(), jobKey);
            } catch(Exception e) {
                LOG.error("", e);
            }
        }
    }

    @Override
    public void remove(Trigger trigger, Rule rule) {
        JobKey key = scheduledRules.remove(rule.getId());
        try {
            scheduler.deleteJob(key);
        } catch (SchedulerException e) {
            LOG.error("Could not stop scheduled job", e);
        }
    }

        @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    private void evalRule(String ruleId) {
        LOG.debug("Evaluating time trigger rule: {}", ruleId);

        ruleEngine.evalRule(ruleId);
    }

    private interface CronJob {
        void eval();
    }
}
