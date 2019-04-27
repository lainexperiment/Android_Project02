package com.lainexperiment.android_project02;

import java.util.HashMap;

public class Post implements DataContainer
{
    private int usetId;
    private int id;
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
