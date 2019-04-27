package com.lainexperiment.project02;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.CommentViewHolder> {
    private List<Comment> comments;

    public CommentRecyclerViewAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public @NonNull
    CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item_layout, parent, false);

        CommentViewHolder viewHolder = new CommentViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.name.setText(comments.get(position).getName());
        holder.body.setText(comments.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void updateData(List<Comment> newComments) {
        comments = newComments;
        notifyDataSetChanged();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView body;
        public CommentViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tv_name);
            body = v.findViewById(R.id.tv_body);
        }
    }
}