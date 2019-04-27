package com.lainexperiment.project02;

import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView postsRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Post> posts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postsRecyclerView = findViewById(R.id.post_recyclerView);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        postsRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        recyclerViewAdapter = new PostRecyclerViewAdapter(posts);
        postsRecyclerView.setAdapter(recyclerViewAdapter);

        showData();
    }

    private void showData() {
        MessageController.getInstance().getPosts(new Consumer<List<Post>>() {
            @Override
            public void accept(List<Post> posts) {
                ((PostRecyclerViewAdapter) recyclerViewAdapter).updateData(posts);
            }
        });
    }
}
