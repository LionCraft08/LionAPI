package de.lioncraft.lionapi.velocity.data;

import com.google.gson.Gson;

import java.util.HashMap;

public class TransferrableObject {
    private static final Gson gson = new Gson();
    private HashMap<String, String> data = new HashMap<>();
    private String objectType;

    public static TransferrableObject getFromJson(String s){
        return gson.fromJson(s, TransferrableObject.class);
    }

    public TransferrableObject(String objectType) {
        this.objectType = objectType;
    }

    public String toString(){
        return gson.toJson(this);
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public String getObjectType() {
        return objectType;
    }

    public TransferrableObject addValue(String key, String value){
        data.put(key, value);
        return this;
    }

    public String getString(String key){
        return data.get(key);
    }
    public int getInt(String key){
        return Integer.parseInt(getString(key));
    }
    public boolean getBool(String key){
        return Boolean.parseBoolean(getString(key));
    }
}