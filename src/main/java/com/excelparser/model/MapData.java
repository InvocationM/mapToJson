package com.excelparser.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * 地图数据模型
 */
public class MapData {
    @JsonProperty("mapId")
    private int mapId;

    @JsonProperty("width")
    private int width;

    @JsonProperty("height")
    private int height;

    @JsonProperty("cells")
    private List<Cell> cells;

    public MapData() {
        this.cells = new ArrayList<>();
    }

    public MapData(int mapId, int width, int height) {
        this.mapId = mapId;
        this.width = width;
        this.height = height;
        this.cells = new ArrayList<>();
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public void addCell(Cell cell) {
        if (cell != null) {
            this.cells.add(cell);
        }
    }
}
