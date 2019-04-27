package com.lainexperiment.project02;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.Consumer;
import android.util.Log;
import android.util.SparseLongArray;

import com.lainexperiment.project02.db.AppRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MessageController {
    private final static String TAG = "MessageController";

    private static MoshiAPI api;
    private static MessageController instance;

    private AppRepository repository;
    private long prevPostReceiveMillis = 0;
    private SparseLongArray sparsePrevCommentMillis;
    private SharedPreferences sharedPreferences;

    private MessageController(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        MessageController.api = retrofit.create(MoshiAPI.class);
        this.repository = new AppRepository(context);
        this.sparsePrevCommentMillis = new SparseLongArray();
        sharedPreferences = context.getSharedPreferences("AppSharedPrefs", Context.MODE_PRIVATE);
        prevPostReceiveMillis = sharedPreferences.getInt("prevPostReceiveMillis", 0);
    }

    public static MessageController getInstance(Context context) {
        if (instance == null) {
            instance = new MessageController(context);
        }
        return instance;
    }

    public void getPosts(final Consumer<List<Post>> consumer) {
        if (System.currentTimeMillis() - sharedPreferences.getLong("-1", 0) < 5 * 60 * 1000)
        {
            repository.getAllPosts(consumer);
            Log.i(TAG, "Less than 5 minutes passed. All posts Received from the database.");
            return;
        }
        Call<List<Post>> postsCall = api.getPosts();
        postsCall.enqueue(new PostReceivingCallback(consumer));
    }

    public void getComments(int postId, final Consumer<List<Comment>> consumer) {
//        long prevCommentReceiveMillis = sparsePrevCommentMillis.get(postId, 0);
        long prevCommentReceiveMillis = sharedPreferences.getLong(String.valueOf(postId), 0);
        if (System.currentTimeMillis() - prevCommentReceiveMillis < 5 * 60 * 1000)
        {
            repository.getAllComments(consumer, postId);
            Log.i(TAG, "Less than 5 minutes passed. Comments from post id " + postId +
                    " Received from the database.");
            return;
        }
        Call<List<Comment>> commentsCall = api.getComments(postId);
        commentsCall.enqueue(new CommentReceivingCallback(consumer, postId));
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

            if (!response.isSuccessful()) {
                repository.getAllPosts(consumer);
                // TODO log
                return;
            }

            posts = response.body();
            if (posts == null) {
                // TODO handle null posts
                return;
            }
            consumer.accept(posts);
            repository.insertPosts(posts);
//            prevPostReceiveMillis = System.currentTimeMillis();
            sharedPreferences.edit().putLong("-1", System.currentTimeMillis()).apply();
        }

        @Override
        public void onFailure(Call<List<Post>> call, Throwable t) {
            repository.getAllPosts(consumer);
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

            if (!response.isSuccessful()) {
                Log.i(TAG, "Response not successful. " + "Comments from post id " + postId +
                        " Received from the database.");
                repository.getAllComments(consumer, postId);
                return;
            }

            comments = response.body();
            if (comments == null) {
                // TODO handle null posts
                Log.i(TAG, "Response body is null.");
                return;
            }
            consumer.accept(comments);
            Log.i(TAG, "Inserting comments of post " + postId);
            repository.insertComments(comments);
            sparsePrevCommentMillis.put(postId, System.currentTimeMillis());
            sharedPreferences.edit().
                    putLong(String.valueOf(postId), System.currentTimeMillis()).apply();
        }

        @Override
        public void onFailure(Call<List<Comment>> call, Throwable t) {
            Log.i(TAG, "Request failed. " + "Comments from post id " + postId +
                    " Received from the database.");
            repository.getAllComments(consumer, postId);
        }
    }
}
