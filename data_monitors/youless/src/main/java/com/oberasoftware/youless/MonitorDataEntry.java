package com.oberasoftware.youless;

import com.oberasoftware.data.DataEntry;

/**
 * @author renarj
 */
public class MonitorDataEntry {
    private int day;
    private int month;
    private int year;

    private String unit;
    private String value;

    public MonitorDataEntry(int day, int month, int year, String unit, String value) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.unit = unit;
        this.value = value;
    }

    public DataEntry toDataEntry() {
        return new DataEntry(day, month, year, unit, value);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getUnit() {
        return unit;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MonitorDataEntry{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", unit='" + unit + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
