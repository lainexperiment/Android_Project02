package com.lainexperiment.project02;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static com.lainexperiment.project02.CommentActivity.POST_ID_EXTRA;

public class MainActivity extends AppCompatActivity {

    private RecyclerView postsRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private ArrayList<Post> posts = new ArrayList<>();
    private Boolean isGridView = false;
    private static final String TAG = "MainActivity";
    private static final String GRID_VIEW_EXTRA = "grid view extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null)
            isGridView = savedInstanceState.getBoolean(GRID_VIEW_EXTRA, false);

        postsRecyclerView = findViewById(R.id.post_recyclerView);

        postsRecyclerView.setLayoutManager(isGridView ? new GridLayoutManager(this, 2) : new LinearLayoutManager(this));

        recyclerViewAdapter = new PostRecyclerViewAdapter(posts, new PostRecyclerViewAdapter.OnPostClickListener() {
            @Override
            public void onPostClick(Post post) {
                Intent intent = new Intent(MainActivity.this, CommentActivity.class);
                intent.putExtra(POST_ID_EXTRA, post.getId());
                startActivity(intent);
            }
        });
        postsRecyclerView.setAdapter(recyclerViewAdapter);

        Log.d(TAG, "onCreate: lainExperiment: calling to get posts data");
        showData();
    }

    private void showData() {
        MessageController.getInstance(this).getPosts(new Consumer<List<Post>>() {
            @Override
            public void accept(List<Post> posts) {
                ((PostRecyclerViewAdapter) recyclerViewAdapter).updateData(posts);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_grid:
                isGridView = !isGridView;
                postsRecyclerView.setLayoutManager(isGridView ? new GridLayoutManager(this, 2) : new LinearLayoutManager(this));
                postsRecyclerView.setAdapter(recyclerViewAdapter);
                return true;
            case R.id.action_team:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("LainExperiment")
                .setMessage("MohammadAli Zarei Matin\nRuhollah Sekaleshfar\nHamid Hashemi")

                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("OK!", null)

                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(GRID_VIEW_EXTRA, isGridView);
        super.onSaveInstanceState(outState);
    }
}
