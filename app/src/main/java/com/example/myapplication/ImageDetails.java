package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.image.Image;
import com.example.myapplication.image.ImageHelper;

public class ImageDetails extends AppCompatActivity {

    ImageHelper imageHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        String date = getIntent().getExtras().getString("date");

        imageHelper = ImageHelper.instanceOfDatabase(getApplicationContext());

        Image image = imageHelper.getImage(date);

        TextView titleText = findViewById(R.id.title_text);
        TextView dateText = findViewById(R.id.date_text);
        ImageView imageView = findViewById(R.id.image_view);

        titleText.setText(image.getTitle());
        dateText.setText(image.getDate());
        imageView.setImageBitmap(image.getData());
    }
}