package com.lainexperiment.project02;

import android.content.Context;
import android.support.v4.util.Consumer;
import android.telecom.RemoteConnection;

import com.lainexperiment.project02.db.CommentDao;
import com.lainexperiment.project02.db.JTDatabase;
import com.lainexperiment.project02.db.PostDao;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MessageController {
    private static MoshiAPI api;
    private static MessageController instance;

    private JTDatabase database;
    private long prevPostReceiveMillis = 0;
    private long prevCommentReceiveMillis = 0;

    private MessageController(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        MessageController.api = retrofit.create(MoshiAPI.class);
        this.database = JTDatabase.getInstance(context);
    }

    public static MessageController getInstance(Context context) {
        if (instance == null) {
            instance = new MessageController(context);
        }
        return instance;
    }

    public void getPosts(final Consumer<List<Post>> consumer) {
        if (System.currentTimeMillis() - prevPostReceiveMillis < 5 * 60 * 1000)
        {
            getPostsFromDatabase(consumer);
            // TODO log
            return;
        }
        Call<List<Post>> postsCall = api.getPosts();
        postsCall.enqueue(new PostReceivingCallback(consumer));
    }

    public void getComments(int postId, final Consumer<List<Comment>> consumer) {
        if (System.currentTimeMillis() - prevCommentReceiveMillis < 5 * 60 * 1000)
        {
            getCommentsFromDatabase(consumer, postId);
            // TODO log
            return;
        }
        Call<List<Comment>> commentsCall = api.getComments(postId);
        commentsCall.enqueue(new CommentReceivingCallback(consumer, postId));
    }

    private void getPostsFromDatabase(Consumer<List<Post>> consumer)
    {
        PostDao postDao = database.postDao();
        List<Post> posts = postDao.getAllPosts();
        consumer.accept(posts);
    }

    private void getCommentsFromDatabase(Consumer<List<Comment>> consumer, int postId)
    {
        CommentDao commentDao = database.commentDao();
        List<Comment> comments = commentDao.getAllPostComments(postId);
        consumer.accept(comments);
    }

    private class PostReceivingCallback implements Callback<List<Post>>
    {
        private final Consumer<List<Post>> consumer;

        private PostReceivingCallback(final Consumer<List<Post>> consumer)
        {
            this.consumer = consumer;
        }

        @Override
        public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
            List<Post> posts;
            PostDao postDao = database.postDao();

            if (!response.isSuccessful()) {
                getPostsFromDatabase(consumer);
                // TODO log
                return;
            }

            posts = response.body();
            if (posts == null) {
                // TODO handle null posts
                return;
            }
            consumer.accept(posts);
            postDao.insertAll(posts);
            prevPostReceiveMillis = System.currentTimeMillis();
        }

        @Override
        public void onFailure(Call<List<Post>> call, Throwable t) {
            getPostsFromDatabase(consumer);
            // TODO handle error
        }
    }

    private class CommentReceivingCallback implements Callback<List<Comment>>
    {
        private final Consumer<List<Comment>> consumer;
        private int postId;

        private CommentReceivingCallback(final Consumer<List<Comment>> consumer, int postId)
        {
            this.consumer = consumer;
            this.postId = postId;
        }

        @Override
        public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
            List<Comment> comments;
            CommentDao commentDao = database.commentDao();

            if (!response.isSuccessful()) {
                getCommentsFromDatabase(consumer, postId);
                // TODO log
                return;
            }

            comments = response.body();
            if (comments == null) {
                // TODO handle null posts
                return;
            }
            consumer.accept(comments);
            commentDao.insertAll(comments);
            prevCommentReceiveMillis = System.currentTimeMillis();
        }

        @Override
        public void onFailure(Call<List<Comment>> call, Throwable t) {
            getCommentsFromDatabase(consumer, postId);
            // TODO handle error
        }
    }
}
