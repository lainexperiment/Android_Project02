package com.lainexperiment.android_project02;

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
