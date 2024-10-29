package com.mobdeve.s21.yap.carl.mco2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    Context context;
    List<Image> images;
    ActivityResultLauncher<Intent> editPhotoResultLauncher;

    public GalleryAdapter(Context context, List<Image> images, ActivityResultLauncher<Intent> editPhotoResultLauncher) {
        this.context = context;
        this.images = images;
        this.editPhotoResultLauncher = editPhotoResultLauncher;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public GalleryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.gallery_item_layout, parent, false);

        return new GalleryAdapter.MyViewHolder(view, editPhotoResultLauncher);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.MyViewHolder holder, int position) {
        holder.imageView.setImageResource(R.drawable.bunger_bunger);
        // TODO: replace with image resource
        holder.tvImageTitle.setText(images.get(position).getTitle());

        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete this image")
                        .setMessage("This action cannot be undone.")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            // proceed with deletion
                            images.remove(holder.getBindingAdapterPosition());
                            notifyItemRemoved(holder.getBindingAdapterPosition());
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvImageTitle;
        ImageButton ibDelete;

        public MyViewHolder(@NonNull View itemView, ActivityResultLauncher<Intent> editPhotoResultLauncher) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivImage);
            tvImageTitle = itemView.findViewById(R.id.tvImageTitle);
            ibDelete = itemView.findViewById(R.id.ibDelete);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), EditPhotoActivity.class);

                    // Add any information to pass

                    editPhotoResultLauncher.launch(intent);
                }
            });
        }
    }
}
