package com.oberasoftware.home.rules.api.trigger;

/**
 * @author Renze de Vries
 */
public class DayTimeTrigger implements Trigger {
    private int hour;
    private int minute;

    public DayTimeTrigger(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public DayTimeTrigger() {
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DayTimeTrigger that = (DayTimeTrigger) o;

        if (hour != that.hour) return false;
        return minute == that.minute;

    }

    @Override
    public int hashCode() {
        int result = hour;
        result = 31 * result + minute;
        return result;
    }

    @Override
    public String toString() {
        return "DayTimeTrigger{" +
                "hour=" + hour +
                ", minute=" + minute +
                '}';
    }
}
