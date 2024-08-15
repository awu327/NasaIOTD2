package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.image.Image;
import com.example.myapplication.image.ImageHelper;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ImagePreview extends AppCompatActivity {

    ImageHelper imageHelper;
    Image image;
    ImageView imagePreview;
    String title;
    String date;

    // Get the image. Verification that the image is available is done in the home fragment.
    class ImageExtractor extends AsyncTask<String, Integer, String> {

        private String urlAddress;
        private Bitmap bitmap;

        public ImageExtractor(String urlAddress) {
            this.urlAddress = urlAddress;
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("test", "test");

            try {
                URL url = new URL(urlAddress);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                InputStream imageStream = con.getInputStream();

                bitmap = BitmapFactory.decodeStream(imageStream);
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "failure";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("success")) {
                imagePreview.setImageBitmap(bitmap);
                image = new Image(title, date, bitmap);
            }
            else {
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        title = getIntent().getExtras().getString("title");
        String url = getIntent().getExtras().getString("url");
        date = getIntent().getExtras().getString("date");

        imageHelper = ImageHelper.instanceOfDatabase(getApplicationContext());

        imagePreview = findViewById(R.id.image_preview);
        Button saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener((View v) -> {

            if(image == null)
                Toast.makeText(getApplicationContext(), "Please Wait!", Toast.LENGTH_SHORT).show();
            else if(imageHelper.saveImage(image)) {
                Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Image Already Exists", Toast.LENGTH_SHORT).show();
            }
        });

        ImageExtractor extractor = new ImageExtractor(url);
        extractor.execute();
    }
}