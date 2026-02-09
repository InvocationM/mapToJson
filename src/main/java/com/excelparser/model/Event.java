package com.excelparser.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 事件数据模型
 */
public class Event {
    @JsonProperty("type")
    private int type;

    @JsonProperty("id")
    private int id;

    @JsonProperty("weight")
    private int weight;

    public Event() {
    }

    public Event(int type, int id, int weight) {
        this.type = type;
        this.id = id;
        this.weight = weight;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
