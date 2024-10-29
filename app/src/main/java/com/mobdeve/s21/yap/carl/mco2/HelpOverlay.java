package com.mobdeve.s21.yap.carl.mco2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Objects;

public class HelpOverlay {

    public static String HELP_GESTURES_FOR_ADD_STICKER = "HELP_GESTURES_FOR_ADD_STICKER";
    public static String HELP_GESTURES_FOR_EDIT_STICKER = "HELP_GESTURES_FOR_EDIT_STICKER";

    private View overlay;
    private TextView helpText;
    private Context context;

    public HelpOverlay(Context context) {
        this.context = context;
        initialize();
    }

    private void initialize() {
        // Inflate the overlay layout
        overlay = LayoutInflater.from(context).inflate(R.layout.overlay_help, null);
        helpText = overlay.findViewById(R.id.help_text);

        // Set up the close button
        Button closeButton = overlay.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideOverlay();
            }
        });
    }

    public void showOverlay(ViewGroup rootView, String helpKey) {
        // Add the overlay to the root view
        rootView.addView(overlay);
        editText(helpKey);
        overlay.setVisibility(View.VISIBLE);

        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(300);
        overlay.startAnimation(fadeIn);
    }

    public void hideOverlay() {
        // Remove the overlay from the root view
        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(300);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (overlay.getParent() != null) {
                    ((ViewGroup) overlay.getParent()).removeView(overlay);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}
        });
        overlay.startAnimation(fadeOut);
    }

    private void editText(String helpKey) {
        if (Objects.equals(helpKey, HELP_GESTURES_FOR_ADD_STICKER)) {
            helpText.setText(add_guide);
        } else {
            helpText.setText(edit_guide);
        }
    }

    private static String add_guide = "User Guide: Adding Stickers with Custom Gestures\n" +
            "\n" +
            "Welcome to AniCam! Follow these simple gestures to add stickers and enhance your photos creatively:\n" +
            "\n" +
            "*insert table with two columns for gesture line and corresponding animal*\n" +
            "\n" +
            "Experiment with these gestures to customize your stickers quickly and easily!";

    private static String edit_guide = "User Guide: Editing a Placed Sticker\n" +
            "\n" +
            "Once you’ve added a sticker to your photo, here’s how to easily edit it:\n" +
            "\n" +
            "*insert table with two columns for gesture line and corresponding emotion*\n" +
            "\n" +
            "Use these quick gestures to customize stickers effortlessly!";
}

