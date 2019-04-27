package com.lainexperiment.project02.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.lainexperiment.project02.Post;

import java.util.List;

@Dao
public interface PostDao
{
    @Insert
    void insertAll(List<Post> posts);

    @Update
    void update(Post post);

    @Update
    void updateAll(List<Post> posts);

    @Delete
    void delete(Post post);

    @Query("SELECT * FROM post_table ORDER BY id ASC")
    List<Post> getAllPosts();
}
