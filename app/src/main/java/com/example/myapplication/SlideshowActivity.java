package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.image.Image;
import com.example.myapplication.image.ImageHelper;

import java.util.ArrayList;

public class SlideshowActivity extends AppCompatActivity {

    ImageHelper imageHelper;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);

        Button nextButton = findViewById(R.id.next_button);
        Button prevButton = findViewById(R.id.prev_button);
        ImageView displayedImage = findViewById(R.id.image_display);

        imageHelper = ImageHelper.instanceOfDatabase(getApplicationContext());

        ArrayList<Image> imageList = imageHelper.getImageDetailsList();

        Image image = imageHelper.getImage(imageList.get(position).getDate());
        displayedImage.setImageBitmap(image.getData());

        nextButton.setOnClickListener((View v) -> {
            if(++position >= imageList.size()) {
                position = 0;
            }

            Image newImage = imageHelper.getImage(imageList.get(position).getDate());
            displayedImage.setImageBitmap(newImage.getData());
        });

        prevButton.setOnClickListener((View v) -> {
            if(--position <= -1) {
                position = imageList.size()-1;
            }

            Image newImage = imageHelper.getImage(imageList.get(position).getDate());
            displayedImage.setImageBitmap(newImage.getData());
        });
    }
}