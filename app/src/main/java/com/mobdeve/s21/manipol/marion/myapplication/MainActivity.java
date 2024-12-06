package com.mobdeve.s21.manipol.marion.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> imagePaths;
    private RecyclerView imagesRV;
    private RecyclerViewAdapter imageRVAdapter;
    private Spinner sortSpinner;
    private FloatingActionButton fabCamera;

    private static final String TAG = "MainActivity";
    private static final String[] REQUIRED_PERMISSIONS = createRequiredPermissions();

    private static String[] createRequiredPermissions() {
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        return permissions.toArray(new String[0]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagePaths = new ArrayList<>();
        imagesRV = findViewById(R.id.idRVImages);
        sortSpinner = findViewById(R.id.sortSpinner);
        ImageButton fabCamera = findViewById(R.id.fabCamera);



        // Setup sorting spinner
        setupSortSpinner();

        // Setup camera fab
        fabCamera.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        });

        if (allPermissionsGranted()) {
            startApp();
        } else {
            requestPermissions();
        }
    }


    private void setupSortSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.sort_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getImagePath(position == 0); // 0 is Newest, 1 is Oldest
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void startApp() {
        prepareRecyclerView();
        getImagePath(true); // Default to newest first
    }

    private void prepareRecyclerView() {
        imageRVAdapter = new RecyclerViewAdapter(MainActivity.this, imagePaths);
        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 4);
        imagesRV.setLayoutManager(manager);
        imagesRV.setAdapter(imageRVAdapter);
    }

    private void requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS);
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private final ActivityResultLauncher<String[]> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    permissions -> {
                        boolean permissionGranted = true;
                        for (String permission : REQUIRED_PERMISSIONS) {
                            if (!permissions.getOrDefault(permission, false)) {
                                permissionGranted = false;
                                break;
                            }
                        }
                        if (!permissionGranted) {
                            Toast.makeText(this,
                                    "Permission request denied",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startApp();
                        }
                    });

    private void getImagePath(boolean newest) {
        // Clear the existing list first
        imagePaths.clear();

        boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        if (isSDPresent) {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            // Determine sort order based on the parameter
            final String orderBy = newest ?
                    MediaStore.Images.Media.DATE_ADDED + " DESC" :
                    MediaStore.Images.Media.DATE_ADDED + " ASC";

            Cursor cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns,
                    null,
                    null,
                    orderBy
            );

            if (cursor != null) {
                int count = cursor.getCount();

                for (int i = 0; i < count; i++) {
                    cursor.moveToPosition(i);
                    int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    imagePaths.add(cursor.getString(dataColumnIndex));
                }
                // Notify that the entire dataset has changed
                imageRVAdapter.notifyDataSetChanged();
                cursor.close();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh images when returning to the activity
        if (imagePaths != null && imageRVAdapter != null) {
            getImagePath(sortSpinner.getSelectedItemPosition() == 0);
        }
    }
}