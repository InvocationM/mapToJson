package com.excelparser.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * 单元格数据模型
 */
public class Cell {
    @JsonProperty("pos")
    private int pos;

    @JsonProperty("x")
    private int x;

    @JsonProperty("y")
    private int y;

    @JsonProperty("tileId")
    private int tileId;

    @JsonProperty("events")
    private List<Event> events;

    public Cell() {
        this.events = new ArrayList<>();
    }

    public Cell(int pos, int x, int y, int tileId) {
        this.pos = pos;
        this.x = x;
        this.y = y;
        this.tileId = tileId;
        this.events = new ArrayList<>();
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTileId() {
        return tileId;
    }

    public void setTileId(int tileId) {
        this.tileId = tileId;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        if (event != null) {
            this.events.add(event);
        }
    }
}
