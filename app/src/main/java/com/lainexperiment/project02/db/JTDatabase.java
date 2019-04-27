package com.lainexperiment.project02.db;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.lainexperiment.project02.Comment;
import com.lainexperiment.project02.Post;

@Database(entities = {Post.class, Comment.class}, version = 1)
public abstract class JTDatabase extends RoomDatabase {
    private static JTDatabase instance;

    public abstract CommentDao commentDao();

    public abstract PostDao postDao();

    public static synchronized JTDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), JTDatabase.class,
                    "JT_database").fallbackToDestructiveMigration().build();
        }

        return instance;
    }


}
