package com.lainexperiment.project02.db;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Consumer;
import android.util.Log;

import com.lainexperiment.project02.Comment;
import com.lainexperiment.project02.Post;

import java.util.List;

public class AppRepository
{
    private static final String TAG = "AppRepository";
    private static final String PROJECT_NAME = "LainExperiment";

    private PostDao postDao;
    private CommentDao commentDao;

    public AppRepository(Context context)
    {
        JTDatabase database = JTDatabase.getInstance(context);
        this.postDao = database.postDao();
        this.commentDao = database.commentDao();
    }

    public void insertPosts(List<Post> posts)
    {
        new InsertPostAsyncTask(postDao).execute(posts.toArray(new Post[0]));
    }

    public void getAllPosts(Consumer<List<Post>> consumer)
    {
        new GetPostsAsyncTask(postDao, consumer).execute();
    }

    public void insertComments(List<Comment> comments)
    {
        new InsertCommentAsyncTask(commentDao).execute(comments.toArray(new Comment[0]));
    }

    public void getAllComments(Consumer<List<Comment>> consumer, int postId)
    {
        new GetCommentsAsyncTask(commentDao, consumer, postId).execute();
    }

    private static class GetCommentsAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private CommentDao commentDao;
        private Consumer<List<Comment>> consumer;
        private int postId;

        private GetCommentsAsyncTask(CommentDao commentDao, Consumer<List<Comment>> consumer,
                                  int postId)
        {
            this.commentDao = commentDao;
            this.consumer = consumer;
            this.postId = postId;
        }


        @Override
        protected Void doInBackground(Void... voids)
        {
            List<Comment> comments = commentDao.getAllPostComments(postId);
            Log.i(TAG, PROJECT_NAME + ": " + comments.size() +
                    " comments received from the database");
            consumer.accept(comments);
            return null;
        }
    }

    private static class InsertCommentAsyncTask extends AsyncTask<Comment, Void, Void>
    {
        private CommentDao commentDao;

        private InsertCommentAsyncTask(CommentDao commentDao)
        {
            this.commentDao = commentDao;
        }

        @Override
        protected Void doInBackground(Comment... comments)
        {
            commentDao.insertAll(comments);
            Log.i(TAG, PROJECT_NAME + ": Comment insertion finished.");
            return null;
        }
    }

    private static class GetPostsAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private PostDao postDao;
        private Consumer<List<Post>> consumer;

        private GetPostsAsyncTask(PostDao postDao, Consumer<List<Post>> consumer)
        {
            this.postDao = postDao;
            this.consumer = consumer;
        }


        @Override
        protected Void doInBackground(Void... voids)
        {
            List<Post> posts = postDao.getAllPosts();
            Log.i(TAG, PROJECT_NAME + ": " + posts.size() +
                    " Posts received from the database.");
            consumer.accept(posts);
            return null;
        }
    }

    private static class InsertPostAsyncTask extends AsyncTask<Post, Void, Void>
    {
        private PostDao postDao;

        private InsertPostAsyncTask(PostDao postDao)
        {
            this.postDao = postDao;
        }

        @Override
        protected Void doInBackground(Post... posts)
        {
            postDao.insertAll(posts);
            Log.i(TAG, PROJECT_NAME + ": Post insertion finished.");
            return null;
        }
    }
}
