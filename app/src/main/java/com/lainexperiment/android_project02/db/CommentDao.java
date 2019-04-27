package com.lainexperiment.android_project02.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import com.lainexperiment.android_project02.Comment;

@Dao
public interface CommentDao
{
    @Insert
    void insert(Comment... comments);

    @Update
    void update(Comment comment);

    @Update
    void update(Comment... comments);

    @Delete
    void delete(Comment comment);
}
