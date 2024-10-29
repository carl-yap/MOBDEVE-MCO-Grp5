package com.mobdeve.s21.yap.carl.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ImageButton btnMenu, btnCamera;
    private ViewPager2 viewPager;
    private ViewPagerAdapter tabAdapter;

    private final String[] tabNames = new String[]{"Gallery", "Album"};

    private HelpOverlay help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(tabAdapter);

        // Linking the TabLayout to ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabNames[position]);
                    }
                }).attach();

        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.photo_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return handleMenuItemClick(menuItem);
                    }
                });
                popupMenu.show();
            }
        });

        // TODO: Replace current FAB onClick to use Android camera API
        GalleryViewModel temp = new ViewModelProvider(this).get(GalleryViewModel.class);
        help = new HelpOverlay(this);

        btnCamera = findViewById(R.id.btnCamera);
        btnCamera.bringToFront();
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp.addImage(new Image("*snap*", "test"));
            }
        });
    }

    private boolean handleMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.sort_added) {
            // No implementation
            return true;
        } else if (item.getItemId() == R.id.sort_edited) {
            // No implementation
            return true;
        } else if (item.getItemId() == R.id.album_add) {
            AlbumViewModel temp = new ViewModelProvider(MainActivity.this).get(AlbumViewModel.class);
            Album blank = new Album("New Album");
            temp.addAlbum(blank);
            Log.d("ADD_ALBUM", String.format("number of albums=%d", temp.getAlbumList().getValue().size()));
            return true;
        } else if (item.getItemId() == R.id.album_delete) {
            // No implementation
            return true;
        } else if (item.getItemId() == R.id.help_a) {
            help.showOverlay((ViewGroup) findViewById(android.R.id.content), HelpOverlay.HELP_GESTURES_FOR_ADD_STICKER);
            return true;
        } else if (item.getItemId() == R.id.help_b) {
            help.showOverlay((ViewGroup) findViewById(android.R.id.content), HelpOverlay.HELP_GESTURES_FOR_EDIT_STICKER);
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return false;
        }
    }
}