package com.lainexperiment.project02.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import com.lainexperiment.project02.Post;

@Dao
public interface PostDao
{
    @Insert
    void insert(Post... posts);

    @Update
    void update(Post post);

    @Update
    void update(Post... posts);

    @Delete
    void delete(Post post);
}
