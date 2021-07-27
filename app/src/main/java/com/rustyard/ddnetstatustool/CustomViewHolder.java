package com.rustyard.ddnetstatustool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textView);
    }

    public void bind(List<String> dataSet, int position) {
        textView.setText(dataSet.get(position));
    }

    public static CustomViewHolder create(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new CustomViewHolder(view);
    }
}
