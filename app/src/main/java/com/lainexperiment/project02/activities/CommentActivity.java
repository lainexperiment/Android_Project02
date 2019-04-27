package com.lainexperiment.project02.activities;

import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lainexperiment.project02.Comment;
import com.lainexperiment.project02.CommentRecyclerViewAdapter;
import com.lainexperiment.project02.MessageController;
import com.lainexperiment.project02.R;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    public static final String POST_ID_EXTRA = "post_id_extra";
    private static final String TAG = "CommentActivity";

    private RecyclerView commentsRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentsRecyclerView = findViewById(R.id.comment_recyclerView);

        layoutManager = new LinearLayoutManager(this);
        commentsRecyclerView.setLayoutManager(layoutManager);

        recyclerViewAdapter = new CommentRecyclerViewAdapter(comments);
        commentsRecyclerView.setAdapter(recyclerViewAdapter);

        int postId = getIntent().getIntExtra(POST_ID_EXTRA, -1);
        setTitle("Post " + postId);

        if (postId == -1) {
            Log.w(TAG, "onCreate: post id is not provided!");
        } else {
            showData(postId);
        }
    }

    private void showData(final int postId) {
        MessageController.getInstance(this).getComments(postId, new Consumer<List<Comment>>() {
            @Override
            public void accept(List<Comment> comments) {
                ((CommentRecyclerViewAdapter) recyclerViewAdapter).updateData(comments);
                setTitle("Post " + postId + ", " + comments.size() + " comments");
            }
        });
    }
}
