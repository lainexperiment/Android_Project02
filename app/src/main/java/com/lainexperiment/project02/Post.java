package com.lainexperiment.project02;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.HashMap;

@Entity(tableName = "post_table")
public class Post implements DataContainer
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int usetId;
    private String title;
    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsetId() {
        return usetId;
    }

    public void setUsetId(int usetId) {
        this.usetId = usetId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public HashMap<String, String> getData()
    {
        HashMap<String, String> data = new HashMap<>();
        data.put("userId", String.valueOf(usetId));
        data.put("id", String.valueOf(id));
        data.put("title", title);
        data.put("body", body);
        return data;
    }
}
