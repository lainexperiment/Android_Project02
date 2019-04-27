package com.lainexperiment.project02;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.HashMap;

@Entity(tableName = "comment_table", foreignKeys = @ForeignKey(entity = Comment.class,
        parentColumns = "id", childColumns = "postId", onDelete = ForeignKey.CASCADE))
public class Comment implements DataContainer {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int postId;
    private String name;
    private String email;
    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public HashMap<String, String> getData() {
        HashMap<String, String> data = new HashMap<>();
        data.put("postId", String.valueOf(postId));
        data.put("id", String.valueOf(id));
        data.put("name", name);
        data.put("email", email);
        data.put("body", body);
        return data;
    }
}
