package com.mobdeve.s21.yap.carl.mco2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {
    Context context;
    List<Album> albums;
    OnAlbumClickListener onAlbumClickListener;

    public interface OnAlbumClickListener {
        void onAlbumClick(int position);
    }

    public AlbumAdapter(Context context, List<Album> albums, OnAlbumClickListener onAlbumClickListener) {
        this.albums = albums;
        this.context = context;
        this.onAlbumClickListener = onAlbumClickListener;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    @NonNull
    @Override
    public AlbumAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.album_item_layout, parent, false);

        return new AlbumAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.MyViewHolder holder, int position) {
        holder.ivAlbumCover.setImageResource(R.drawable.bunger_bunger);
        // TODO: replace with first photo of album or avatar for empty albums
        holder.tvAlbumName.setText(albums.get(position).getName());
        holder.tvAlbumSize.setText(String.format("%s Photos",albums.get(position).getSize()));

        // Set click listener for the view holder
        holder.itemView.setOnClickListener(v -> onAlbumClickListener.onAlbumClick(position));
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAlbumCover;
        TextView tvAlbumName, tvAlbumSize;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAlbumCover = itemView.findViewById(R.id.ivAlbumCover);
            tvAlbumName = itemView.findViewById(R.id.tvAlbumName);
            tvAlbumSize = itemView.findViewById(R.id.tvAlbumSize);
        }
    }
}
