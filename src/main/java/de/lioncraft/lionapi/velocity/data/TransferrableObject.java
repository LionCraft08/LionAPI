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
}