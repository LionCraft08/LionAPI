package de.lioncraft.lionapi.guimanagement.lionclient;

import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class LionDisplayData {
    private static final Gson gson = new Gson();
    private String id;
    private String type;
    private Integer offsetX;
    private Integer offsetY;
    private DisplayAttachment displayAttachment;
    private HashMap<String, String> data = new HashMap<>();

    public LionDisplayData(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public LionDisplayData(String id, String type, Integer offsetX, Integer offsetY, @Nullable DisplayAttachment displayAttachment) {
        this.id = id;
        this.type = type;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.displayAttachment = displayAttachment;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(Integer offsetX) {
        this.offsetX = offsetX;
    }

    public Integer getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(Integer offsetY) {
        this.offsetY = offsetY;
    }

    public DisplayAttachment getDisplayAttachment() {
        return displayAttachment;
    }

    public void setDisplayAttachment(DisplayAttachment displayAttachment) {
        this.displayAttachment = displayAttachment;
    }

    public HashMap<String, String> getData() {
        return data;
    }
    public String getData(String key) {
        return data.get(key);
    }

    public void setData(String key, String data) {
        this.data.put(key, data);
    }

    @Override
    public String toString(){
        return gson.toJson(this);
    }
}
