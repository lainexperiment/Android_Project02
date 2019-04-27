package com.lainexperiment.project02;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.Consumer;
import android.util.Log;

import com.lainexperiment.project02.db.AppRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MessageController {
    private final static String TAG = "MessageController";
    private final static String PROJECT_NAME = "LainExperiment";
    private final static String POSTS_LAST_SEEN_TIME = "posts last seen time";
    private final static String COMMENTS_LAST_SEEN_PREFIX = "comments last seen prefix";

    private static MoshiAPI api;
    private static MessageController instance;

    private AppRepository repository;
    private SharedPreferences sharedPreferences;

    private MessageController(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        MessageController.api = retrofit.create(MoshiAPI.class);
        this.repository = new AppRepository(context);
        this.sharedPreferences = context.getSharedPreferences("AppSharedPrefs", Context.MODE_PRIVATE);
    }

    public static MessageController getInstance(Context context) {
        if (instance == null) {
            instance = new MessageController(context);
        }
        return instance;
    }

    public void getPosts(final Consumer<List<Post>> consumer) {
        if (System.currentTimeMillis() - sharedPreferences.getLong(POSTS_LAST_SEEN_TIME, 0) < 5 * 60 * 1000) {
            Log.i(TAG, PROJECT_NAME + ": Less than 5 minutes passed." +
                    " Requesting from the database.");
            repository.getAllPosts(consumer);
            return;
        }

        Log.i(TAG, PROJECT_NAME + ": Sending a web request.");
        Call<List<Post>> postsCall = api.getPosts();
        postsCall.enqueue(new PostReceivingCallback(consumer));
    }

    public void getComments(int postId, final Consumer<List<Comment>> consumer) {
        long prevCommentReceiveMillis = sharedPreferences.getLong(COMMENTS_LAST_SEEN_PREFIX + String.valueOf(postId), 0);
        if (System.currentTimeMillis() - prevCommentReceiveMillis < 5 * 60 * 1000) {
            Log.i(TAG, PROJECT_NAME + ": Less than 5 minutes passed. " +
                    "Requesting the commend of post " + postId + " from the database.");
            repository.getAllComments(consumer, postId);
            return;
        }

        Log.i(TAG, PROJECT_NAME + ": Sending a web request.");
        Call<List<Comment>> commentsCall = api.getComments(postId);
        commentsCall.enqueue(new CommentReceivingCallback(consumer, postId));
    }

    private class PostReceivingCallback implements Callback<List<Post>> {
        private final Consumer<List<Post>> consumer;

        private PostReceivingCallback(final Consumer<List<Post>> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
            List<Post> posts;

            if (!response.isSuccessful()) {
                Log.i(TAG, PROJECT_NAME + ": Response not successful. " +
                        "Requesting from the database.");
                repository.getAllPosts(consumer);
                return;
            }

            posts = response.body();
            if (posts == null) {
                Log.i(TAG, PROJECT_NAME + ": Response body is null.");
                return;
            }
            consumer.accept(posts);
            Log.i(TAG, PROJECT_NAME + ": Inserting " + posts.size() + " posts");
            repository.insertPosts(posts);
            sharedPreferences.edit().putLong(POSTS_LAST_SEEN_TIME, System.currentTimeMillis()).apply();
        }

        @Override
        public void onFailure(Call<List<Post>> call, Throwable t) {
            Log.e(TAG, PROJECT_NAME + ": " + t.getMessage());
            Log.i(TAG, PROJECT_NAME + ": Request failed. " + "Requesting from the database.");
            repository.getAllPosts(consumer);
        }
    }

    private class CommentReceivingCallback implements Callback<List<Comment>> {
        private final Consumer<List<Comment>> consumer;
        private int postId;

        private CommentReceivingCallback(final Consumer<List<Comment>> consumer, int postId) {
            this.consumer = consumer;
            this.postId = postId;
        }

        @Override
        public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
            List<Comment> comments;

            if (!response.isSuccessful()) {
                Log.e(TAG, PROJECT_NAME +
                        ": Unsuccessful request with code " + response.code());
                Log.i(TAG, PROJECT_NAME + ": Response not successful. " +
                        "Comments from post id " + postId + " Received from the database.");
                repository.getAllComments(consumer, postId);
                return;
            }

            comments = response.body();
            if (comments == null) {
                Log.i(TAG, "Response body is null.");
                return;
            }
            Log.i(TAG, PROJECT_NAME + ": Response successfully received.");
            consumer.accept(comments);
            Log.i(TAG, PROJECT_NAME + ": Inserting " + comments.size()
                    + " comments of post " + postId);
            repository.insertComments(comments);
            sharedPreferences.edit().
                    putLong(COMMENTS_LAST_SEEN_PREFIX + String.valueOf(postId), System.currentTimeMillis()).apply();
        }

        @Override
        public void onFailure(Call<List<Comment>> call, Throwable t) {
            Log.e(TAG, PROJECT_NAME + ": " + t.getMessage());
            Log.i(TAG, PROJECT_NAME + ": Request failed. " + "Comments from post id " + postId +
                    " Received from the database.");
            repository.getAllComments(consumer, postId);
        }
    }
}
