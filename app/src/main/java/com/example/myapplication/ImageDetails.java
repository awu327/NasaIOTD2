package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.image.Image;
import com.example.myapplication.image.ImageHelper;
import com.google.android.material.snackbar.Snackbar;

public class ImageDetails extends AppCompatActivity {

    ImageHelper imageHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        // Get data for use
        String date = getIntent().getExtras().getString(getString(R.string.date_key));
        imageHelper = ImageHelper.instanceOfDatabase(getApplicationContext());
        Image image = imageHelper.getImage(date);


        // Instantiate all used items in this activity
        ConstraintLayout main = findViewById(R.id.main);
        TextView titleText = findViewById(R.id.title_text);
        TextView dateText = findViewById(R.id.date_text);
        ImageView imageView = findViewById(R.id.image_view);
        Button deleteButton = findViewById(R.id.delete_button);
        Button backButton = findViewById(R.id.back_button);
        Button favButton = findViewById(R.id.fav_button);


        // Set values based on pulled data
        titleText.setText(image.getTitle());
        dateText.setText(image.getDate());
        imageView.setImageBitmap(image.getData());


        // Add on click listeners
        backButton.setOnClickListener((View V) -> {
            finish();
        });

        deleteButton.setOnClickListener((View V) -> {
            Snackbar snackbar = Snackbar.make(main, R.string.confirm_delete, Snackbar.LENGTH_LONG)
                    .setAction(R.string.yes, v -> {
                        SharedPreferences sharedPref = this.getSharedPreferences("fav image", Context.MODE_PRIVATE);
                        if(image.getDate().equals(sharedPref.getString("date", "no date")))
                            sharedPref.edit().putString("date", "no date").apply();
                        imageHelper.deleteImage(image);
                        finish();
                    });
            snackbar.show();
        });

        favButton.setOnClickListener((View v) -> {
            SharedPreferences sharedPref = this.getSharedPreferences("fav image", Context.MODE_PRIVATE);
            sharedPref.edit().putString("date", image.getDate()).apply();
        });
    }
}