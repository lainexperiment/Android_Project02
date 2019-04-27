package com.lainexperiment.project02;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.HashMap;

@Entity(tableName = "comment_table", foreignKeys = @ForeignKey(entity = Comment.class,
        parentColumns = "id", childColumns = "postId", onDelete = ForeignKey.CASCADE))
public class Comment implements DataContainer
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int postId;
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
