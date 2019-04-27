package com.lainexperiment.project02.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.lainexperiment.project02.Comment;

import java.util.List;

@Dao
public interface CommentDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Comment... comments);

    @Update
    void update(Comment comment);

    @Update
    void updateAll(List<Comment> comments);

    @Delete
    void delete(Comment comment);

    @Query("SELECT comment_table.id, comment_table.postId, comment_table.name, comment_table.email," +
            " comment_table.body FROM comment_table INNER JOIN post_table" +
            " ON comment_table.postId = post_table.id" +
            " WHERE post_table.id = :postId")
    List<Comment> getAllPostComments(int postId);
}
