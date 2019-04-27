package com.lainexperiment.project02;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.PostViewHolder> {
    private List<Post> posts;
    private OnPostClickListener listener;

    public PostRecyclerViewAdapter(ArrayList<Post> posts, OnPostClickListener listener) {
        this.posts = posts;
        this.listener = listener;
    }

    @Override
    public @NonNull PostRecyclerViewAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item_layout, parent, false);

        PostViewHolder viewHolder = new PostViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.title.setText(posts.get(position).getTitle());
        holder.body.setText(posts.get(position).getBody());

        holder.bind(posts.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updateData(List<Post> newPosts) {
        posts = newPosts;
        notifyDataSetChanged();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView body;
        public PostViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.tv_title);
            body = v.findViewById(R.id.tv_body);
        }

        public void bind(final Post post, final OnPostClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onPostClick(post);
                }
            });
        }
    }

    public interface OnPostClickListener {
        void onPostClick(Post post);
    }
}