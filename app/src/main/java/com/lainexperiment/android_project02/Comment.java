package com.lainexperiment.android_project02;

import java.util.HashMap;

public class Comment implements DataContainer
{
    private int postId;
    private int id;
    private String name;
    private String email;
    private String body;

    @Override
    public HashMap<String, String> getData()
    {
        HashMap<String, String> data = new HashMap<>();
        data.put("postId", String.valueOf(postId));
        data.put("id", String.valueOf(id));
        data.put("name", name);
        data.put("email", email);
        data.put("body", body);
        return data;
    }
}
