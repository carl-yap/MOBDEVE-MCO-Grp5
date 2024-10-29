package com.mobdeve.s21.yap.carl.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class OpenAlbumActivity extends AppCompatActivity {

    private LinearLayout llBackButton;
    private TextView tvAlbumName;
    private RecyclerView rvGallery;
    private GalleryAdapter galleryAdapter;

    private final ActivityResultLauncher<Intent> editPhotoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Intent data = result.getData();

                    // assert data != null;

                    // Notify gallery adapter
                    Toast.makeText(this, "edit photo: RES_OK", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_gallery);

        llBackButton = findViewById(R.id.llBackButton);
        llBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        int albumPosition = getIntent().getIntExtra(AlbumFragment.ALBUM_POSITION_KEY, -1);

        tvAlbumName = findViewById(R.id.tvAlbumHeading);
        tvAlbumName.setText(getAlbumName(albumPosition));

        rvGallery = findViewById(R.id.rvGallery);
        rvGallery.setLayoutManager(new GridLayoutManager(this, 2));

        List<Image> images = getImagesForAlbum(albumPosition);
        galleryAdapter = new GalleryAdapter(this, images, editPhotoResultLauncher);
        rvGallery.setAdapter(galleryAdapter);
    }

    private List<Image> getImagesForAlbum(int albumPosition) {
        // TODO: replace with db logic
        List<Image> dummy = new ArrayList<>();
        for (int i = 0; i < 4; i++) { dummy.add(new Image("test", "test")); }
        return dummy;
    }

    private String getAlbumName(int albumPosition) {
        // TODO: replace with db logic
        return "Fixed Album Name";
    }
}
