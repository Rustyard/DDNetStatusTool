package com.rustyard.ddnetstatustool;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private List<String> localDataSet;

    public CustomAdapter() {
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return CustomViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.bind(localDataSet, position);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public void setLocalDataSet(List<String> localDataSet) {
        this.localDataSet = localDataSet;
        notifyDataSetChanged();
    }
}
