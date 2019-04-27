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

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommentRecyclerViewAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public @NonNull
    CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item_layout, parent, false);

        CommentViewHolder viewHolder = new CommentViewHolder(v);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(comments.get(position).getName());
        holder.body.setText(comments.get(position).getBody());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void updateData(List<Comment> newComments) {
        comments = newComments;
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public TextView body;
        public CommentViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tv_name);
            body = v.findViewById(R.id.tv_body);
        }
    }
}