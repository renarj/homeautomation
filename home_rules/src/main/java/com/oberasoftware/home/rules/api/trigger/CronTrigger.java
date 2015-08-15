package com.oberasoftware.home.rules.api.trigger;

/**
 * @author Renze de Vries
 */
public class CronTrigger implements TimeTrigger {
    private String cron;

    public CronTrigger(String cron) {
        this.cron = cron;
    }

    public CronTrigger() {
    }

    @Override
    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CronTrigger that = (CronTrigger) o;

        return cron.equals(that.cron);

    }

    @Override
    public int hashCode() {
        return cron.hashCode();
    }

    @Override
    public String toString() {
        return "CronTrigger{" +
                "cron='" + cron + '\'' +
                '}';
    }
}
