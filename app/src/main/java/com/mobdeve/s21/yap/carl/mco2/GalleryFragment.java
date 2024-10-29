package com.mobdeve.s21.yap.carl.mco2;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private RecyclerView rvGallery;
    private GalleryAdapter galleryAdapter;

    private final ActivityResultLauncher<Intent> editPhotoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Intent data = result.getData();

                    // assert data != null;

                    // Notify gallery adapter
                    Toast.makeText(getContext(), "edit photo: RES_OK", Toast.LENGTH_SHORT).show();
                }
            });

    public GalleryFragment() {
        // TODO: implement the gallery view
    }

    // Factory method
    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    public static GalleryFragment newInstance(int albumPosition) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt(AlbumFragment.ALBUM_POSITION_KEY, albumPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: Initialize your gallery view here
        // For example, set up RecyclerView, load images, etc.

        rvGallery = view.findViewById(R.id.rvGallery);
        rvGallery.setLayoutManager(new GridLayoutManager(getContext(), 2));

        galleryAdapter = new GalleryAdapter(getContext(), new ArrayList<>(), editPhotoResultLauncher);
        rvGallery.setAdapter(galleryAdapter);

        galleryViewModel = new ViewModelProvider(requireActivity()).get(GalleryViewModel.class);
        galleryViewModel.getImageList().observe(getViewLifecycleOwner(), new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                // Update UI with the new image list
                // For example, update RecyclerView adapter
                galleryAdapter.setImages(images);
                galleryAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Images Updated: " + images.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
