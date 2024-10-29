package com.mobdeve.s21.yap.carl.mco2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditPhotoActivity extends AppCompatActivity {
    private ImageView stickerImage;
    private float dX, dY;
    private boolean isSticker = false;

    private ImageButton addStickerButton, saveButton, backButton, helpButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        // Initialize views
        stickerImage = findViewById(R.id.stickerImage);
        addStickerButton = findViewById(R.id.addStickerButton);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.galleryButton);
        helpButton = findViewById(R.id.helpButton);

        // Make sticker draggable
        stickerImage.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    float newX = event.getRawX() + dX;
                    float newY = event.getRawY() + dY;

                    // Ensure sticker stays within bounds
                    FrameLayout container = findViewById(R.id.imageContainer);
                    newX = Math.max(0, Math.min(newX, container.getWidth() - view.getWidth()));
                    newY = Math.max(0, Math.min(newY, container.getHeight() - view.getHeight()));

                    view.setX(newX);
                    view.setY(newY);
                    return true;

                default:
                    return false;
            }
        });

        // Add sticker button click listener
        addStickerButton.setOnClickListener(v -> {
            if (!isSticker) {
                stickerImage.setVisibility(View.VISIBLE);
                // Center the sticker initially
                stickerImage.post(() -> {
                    FrameLayout container = findViewById(R.id.imageContainer);
                    float centerX = (container.getWidth() - stickerImage.getWidth()) / 2f;
                    float centerY = (container.getHeight() - stickerImage.getHeight()) / 2f;
                    stickerImage.setX(centerX);
                    stickerImage.setY(centerY);
                });
                isSticker = true;
            }
        });

        // Save button click listener
        saveButton.setOnClickListener(v -> {
            // Create a bitmap of the current view
            FrameLayout container = findViewById(R.id.imageContainer);
            Bitmap combinedImage = Bitmap.createBitmap(
                    container.getWidth(),
                    container.getHeight(),
                    Bitmap.Config.ARGB_8888
            );
            Canvas canvas = new Canvas(combinedImage);
            container.draw(canvas);

            // Save the bitmap
            saveBitmapToGallery(combinedImage);
        });

        // Back button click listener
        backButton.setOnClickListener(v -> {
            setResult(EditPhotoActivity.RESULT_OK);
            finish();
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelpOverlay help = new HelpOverlay(view.getContext());
                help.showOverlay((ViewGroup) findViewById(android.R.id.content), HelpOverlay.HELP_GESTURES_FOR_ADD_STICKER);
            }
        });
    }

    private void saveBitmapToGallery(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        }

        Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            if (imageUri != null) {
                try (OutputStream os = getContentResolver().openOutputStream(imageUri)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                }
                Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }
}