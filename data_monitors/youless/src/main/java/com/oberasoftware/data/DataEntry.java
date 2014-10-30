package com.oberasoftware.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author renarj
 */
@Entity
public class DataEntry {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="DAY")
    private int day;

    @Column(name="MONTH")
    private int month;

    @Column(name="YEAR")
    private int year;

    @Column(name="TIMESTAMP")
    private long timestamp;

    @Column(name="ITEM_ID")
    private long itemId;

    @Column(name="VALUE_TYPE")
    private String valueType;

    @Column(name="VALUE")
    private String value;

    public DataEntry() {

    }

    public DataEntry(int day, int month, int year, String valueType, String value) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.valueType = valueType;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
