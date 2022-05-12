package com.example.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private List<Task> itemList;

    public TaskListAdapter() {
        itemList = new ArrayList<>();
    }

    public void setItems(List<Task> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder
            (ViewGroup parent, int viewType) {
        ItemBinding binding = ItemBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(binding);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder
            (final ItemViewHolder holder, int position) {
        Task data = itemList.get(position);
        holder.nameTextField.setText(data.toString());

    }
}