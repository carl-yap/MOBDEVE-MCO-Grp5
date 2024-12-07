package com.mobdeve.s21.manipol.marion.myapplication;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.mobdeve.s21.manipol.marion.myapplication.databinding.ActivityPhotoPreviewBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PhotoPreviewActivity extends AppCompatActivity {
    private ActivityPhotoPreviewBinding binding;
    private String selectedAnimalGesture = null;
    private String selectedEmotionGesture = null;
    private GestureLibrary gestureLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPhotoPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageButton helpButton = findViewById(R.id.help_button);

        helpButton.setOnClickListener(v -> showInstructionsDialog());

        loadGestures();

        // Check for different types of image sources
        Uri photoUri = getIntent().getParcelableExtra("photo_uri");
        String imagePath = getIntent().getStringExtra("imgPath");

        // Load image from URI (from CameraActivity)
        if (photoUri != null) {
            binding.capturedImageView.setImageURI(photoUri);
        }
        // Load image from file path (from MainActivity/RecyclerViewAdapter)
        else if (imagePath != null) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Picasso.get().load(imgFile).into(binding.capturedImageView);
            } else {
                Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "No image source found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding.gestureFab.setOnClickListener(v -> {
            binding.gestureOverlay.setVisibility(View.VISIBLE);
            binding.gestureInstructionText.setVisibility(View.VISIBLE);

            Toast.makeText(this, "Draw your sticker!", Toast.LENGTH_SHORT).show();
        });

        binding.gestureOverlay.addOnGesturePerformedListener((overlay, gesture) -> {
            ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
            if (predictions.size() > 0) {
                Prediction bestPrediction = predictions.get(0);
                if (bestPrediction.score > 2.0) { // Adjust threshold as needed
                    processGesture(bestPrediction.name);
                } else {
                    Toast.makeText(this, "Unrecognized gesture", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button saveButton = binding.saveButton;
        saveButton.setOnClickListener(v -> {
            saveImageWithStickers();
        });
    }
    private void showInstructionsDialog() {
        // Inflate the instructions layout
        View instructionsView = getLayoutInflater().inflate(R.layout.instructions, null);

        // Create and configure the dialog
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setView(instructionsView)
                .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void saveImageWithStickers() {
        // Create a bitmap of the entire sticker container
        Bitmap bitmap = Bitmap.createBitmap(
                binding.stickerContainer.getWidth(),
                binding.stickerContainer.getHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);

        // Draw the background image first
        Drawable background = binding.capturedImageView.getDrawable();
        background.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        background.draw(canvas);

        // Draw all stickers
        binding.stickerContainer.draw(canvas);

        // Save the bitmap
        try {
            // Create a file name with timestamp
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";

            // Save to Pictures directory
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Write to file
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            // Notify gallery about the new image
            MediaScannerConnection.scanFile(this,
                    new String[]{imageFile.getAbsolutePath()},
                    null,
                    null
            );

            // Create an intent to return to MainActivity with a flag to refresh the gallery
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("IMAGE_SAVED", true);

            // Show toast and then start the MainActivity
            Toast.makeText(this, "Image saved: " + imageFile.getName(), Toast.LENGTH_SHORT).show();

            // Add a small delay to allow the user to see the toast
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    finish(); // Close the current activity
                }
            }, 500); // 500 milliseconds delay

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadGestures() {
        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gestureLibrary.load()) {
            Toast.makeText(this, "Failed to load gestures", Toast.LENGTH_SHORT).show();
        }
    }

    private void processGesture(String gesture) {
        if (selectedAnimalGesture == null) {
            if (gesture.equals("cat") || gesture.equals("dog") || gesture.equals("bird")) {
                selectedAnimalGesture = gesture;
                Toast.makeText(this, "Selected animal: " + gesture, Toast.LENGTH_SHORT).show();
                String instruction = "Now draw a smile or a frown!";
                binding.gestureInstructionText.setText(instruction);
            } else {
                Toast.makeText(this, "Please draw an animal gesture first", Toast.LENGTH_SHORT).show();
            }
        } else if (selectedEmotionGesture == null) {
            boolean isValidGesture = false;

            switch (selectedAnimalGesture) {
                case "dog":
                    isValidGesture = gesture.equals("smile") || gesture.equals("frown") || gesture.equals("glasses");
                    break;
                case "cat":
                    isValidGesture = gesture.equals("smile") || gesture.equals("frown") || gesture.equals("pop");
                    break;
                case "bird":
                    isValidGesture = gesture.equals("smile") || gesture.equals("frown") || gesture.equals("tweety");
                    break;
            }

            if (isValidGesture) {
                selectedEmotionGesture = gesture;
                showSticker();
                binding.gestureOverlay.setVisibility(View.GONE);
                binding.gestureInstructionText.setVisibility(View.GONE);
                //Resetting to allow for adding more stickers
                selectedAnimalGesture = null;
                selectedEmotionGesture = null;
            } else {
                String instruction = "Please draw ";
                switch (selectedAnimalGesture) {
                    case "dog":
                        instruction += "a smile, frown, or glasses";
                        break;
                    case "cat":
                        instruction += "a smile, frown, or pop";
                        break;
                    case "bird":
                        instruction += "a smile, frown, or tweety";
                        break;
                }
                Toast.makeText(this, instruction, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showSticker() {

        String stickerName;
        if (selectedEmotionGesture.equals("smile")) {
            stickerName = selectedAnimalGesture + "_happy";
        } else if (selectedEmotionGesture.equals("frown")) {
            stickerName = selectedAnimalGesture + "_sad";
        } else { // wild gesture
            stickerName = selectedAnimalGesture + "_wild";
        }

        int resId = getResources().getIdentifier(stickerName, "drawable", getPackageName());

        if (resId != 0) {

            ImageView newSticker = new ImageView(this);
            newSticker.setImageResource(resId);


            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            params.leftMargin = 200; // Initial X position
            params.topMargin = 200;  // Initial Y position
            newSticker.setLayoutParams(params);


            newSticker.setOnTouchListener(new View.OnTouchListener() {
                private float dX, dY;

                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dX = view.getX() - event.getRawX();
                            dY = view.getY() - event.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            view.animate()
                                    .x(event.getRawX() + dX)
                                    .y(event.getRawY() + dY)
                                    .setDuration(0)
                                    .start();
                            break;
                        default:
                            return false;
                    }
                    return true;
                }
            });
            // Add the new sticker to the sticker container
            binding.stickerContainer.addView(newSticker);
            Toast.makeText(this, "Sticker displayed: " + stickerName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sticker not found: " + stickerName, Toast.LENGTH_SHORT).show();
        }
    }

}