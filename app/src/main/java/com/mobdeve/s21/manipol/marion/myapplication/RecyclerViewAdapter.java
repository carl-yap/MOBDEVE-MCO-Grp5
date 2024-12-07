package com.mobdeve.s21.manipol.marion.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private final Context context;
    private final ArrayList<String> imagePathArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<String> imagePathArrayList) {
        this.context = context;
        this.imagePathArrayList = imagePathArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        File imgFile = new File(imagePathArrayList.get(position));

        if (imgFile.exists()) {
            Picasso.get().load(imgFile).placeholder(R.drawable.ic_launcher_background).into(holder.imageIV);

            // Set click listener for image preview
            holder.itemView.setOnClickListener(v -> {
                Intent i = new Intent(context, PhotoPreviewActivity.class);
                i.putExtra("imgPath", imagePathArrayList.get(position));
                context.startActivity(i);
            });

            // Set click listener for delete button
            holder.deleteButton.setOnClickListener(v -> {
                // Delete the file
                boolean deleted = imgFile.delete();

                if (deleted) {
                    // Remove from the list
                    imagePathArrayList.remove(position);

                    // Notify adapter of item removal
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, imagePathArrayList.size());

                    // Optional: Show a toast to confirm deletion
                    Toast.makeText(context, "Image deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete image", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return imagePathArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageIV;
        private final ImageButton deleteButton;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIV = itemView.findViewById(R.id.idIVImage);
            deleteButton = itemView.findViewById(R.id.delete_image);
        }
    }
}