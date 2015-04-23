/*
 * ******************************************************
 *                  * Copyright (C) 2015 retor  <retor@mail.ru>
 *                  *
 *                  * This file Event.java is part of  alfa 2.
 *                  *
 *                  * alfa 2 / parserlib can not be copied and/or distributed without the express
 *                  * permission
 * *****************************************************
 */

package contentclasses.test;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by retor on 23.04.2015.
 */
public class Event {

    private int id;
    private int catId;
    private long startTime;
    private String name;
    private String state;
    private String com1;
    private String com2;
    private ArrayList<SimpleItem> c1Miscs = new ArrayList<>();
    private ArrayList<SimpleItem> c2Miscs = new ArrayList<>();
    private ArrayList<SimpleItem> eventMiscs = new ArrayList<>();

    public Event() {
    }

    public ArrayList<SimpleItem> getC1Miscs() {
        return c1Miscs;
    }

    public void setC1Miscs(ArrayList<SimpleItem> c1Miscs) {
        this.c1Miscs = c1Miscs;
    }

    public ArrayList<SimpleItem> getC2Miscs() {
        return c2Miscs;
    }

    public void setC2Miscs(ArrayList<SimpleItem> c2Miscs) {
        this.c2Miscs = c2Miscs;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCom1() {
        return com1;
    }

    public void setCom1(String com1) {
        this.com1 = com1;
    }

    public String getCom2() {
        return com2;
    }

    public void setCom2(String com2) {
        this.com2 = com2;
    }

    public ArrayList<SimpleItem> getEventMiscs() {
        return eventMiscs;
    }

    public void setEventMiscs(ArrayList<SimpleItem> eventMiscs) {
        this.eventMiscs = eventMiscs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return new Date(startTime).toLocalDate().toString();
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Event{" +
                "startTime=" + getStartTime() +
                ", name='" + name + '\n' +
                ", state='" + state + '\n' +
                ", eventMiscs=" + eventMiscs + '\n' +
                ", com1='" + com1 + '\n' +
                ", c1Miscs=" + c1Miscs + '\n' +
                ", com2='" + com2 + '\n' +
                ", c2Miscs=" + c2Miscs +
                '}';
    }
}
