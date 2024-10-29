package com.mobdeve.s21.yap.carl.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends Fragment {

    public static final String ALBUM_POSITION_KEY = "ALBUM_POSITION_KEY";

    private AlbumViewModel albumViewModel;
    private RecyclerView rvAlbum;
    private AlbumAdapter albumAdapter;

    public AlbumFragment() {
        // TODO: implement the album view
    }

    // Factory method
    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize your album view here
        // For example, set up RecyclerView, load albums, etc.

        rvAlbum = view.findViewById(R.id.rvAlbums);
        rvAlbum.setLayoutManager(new GridLayoutManager(getContext(), 2));

        albumAdapter = new AlbumAdapter(getContext(), new ArrayList<>(), this::onAlbumClick);
        rvAlbum.setAdapter(albumAdapter);

        albumViewModel = new ViewModelProvider(requireActivity()).get(AlbumViewModel.class);
        albumViewModel.getAlbumList().observe(getViewLifecycleOwner(), new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                // Update UI with the new album list
                // For example, update RecyclerView adapter
                Log.d("ADD_ALBUM", "onChanged triggered");
                albumAdapter.setAlbums(albums);
                albumAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Albums Updated: " + albums.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onAlbumClick(int albumPosition) {
        // Upon clicking on an album, open an activity displaying its images
        Intent intent = new Intent(getActivity(), OpenAlbumActivity.class);
        intent.putExtra(ALBUM_POSITION_KEY, albumPosition);
        startActivity(intent);
    }
}
