package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ImagePreview;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.image.Image;
import com.example.myapplication.image.ImageHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Image currentImage;
    private ImageHelper imageHelper;

    // This class is used for checking to see if an image exists.
    class ImageExtractor extends AsyncTask<String, Integer, String> {

        private String date;
        private String imageURL;

        public ImageExtractor(String date) {
            this.date = date;
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("test", "test");

            try{
                HttpsURLConnection con;
                URL url = new URL("https://api.nasa.gov/planetary/apod?" +
                        "api_key=TeRiAhjnpFc5yr3qq7echT5IJTeYqb4Cv6bjhf0t&date="+date);

                con = (HttpsURLConnection) url.openConnection();
                con.connect();

                InputStream inputStream = con.getInputStream();
                BufferedReader imgDetailReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();
                String line;

                try {
                    while((line = imgDetailReader.readLine()) != null) {
                        buffer.append(line);
                    }

                    String json = buffer.toString();
                    JSONObject imgDetails = new JSONObject(json);
                    String mediaType = imgDetails.getString("media_type");

                    // If the link is an image (sometimes it's a video)
                    // get the details and use them.
                    if(!mediaType.equals("image")) {
                        return "failure";
                    } else {
                        imageURL = imgDetails.getString("url");
                        String title = imgDetails.getString("title");
                        currentImage = new Image(title, date);
                        for(int i = 0; i < 100; i++) {
                            publishProgress(i);
                            Thread.sleep(10);
                        }
                        return "success";
                    }
                } catch (JSONException e) {
                    publishProgress(100);
                    Thread.sleep(30);
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "failure";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0] == 0) {
                progressBar.setVisibility(View.VISIBLE);
            }

            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // If there is a success, go to image preview
            // Also make a toast for it to be available.
            if(s.equals("success")) {
                Toast.makeText(getContext().getApplicationContext(), getString(R.string.image_found),
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), ImagePreview.class);
                intent.putExtra("title", currentImage.getTitle());
                intent.putExtra("date", currentImage.getDate());
                intent.putExtra("url", imageURL);

                startActivity(intent);
            }
            else {
                Toast.makeText(getContext().getApplicationContext(), getString(R.string.no_image_found),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        final Button dateButton = binding.dateButton;
        final EditText editText = binding.datePicker;
        progressBar = binding.progressBar;
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setMax(100);
        imageHelper = ImageHelper.instanceOfDatabase(getContext());

        dateButton.setOnClickListener((View v) -> {
            // call a thread to extract an image from the website.
            ImageExtractor imageExtractor =
                    new ImageExtractor(editText.getText().toString());
            imageExtractor.execute();
        });


        SharedPreferences sharedPref = getContext().getSharedPreferences("fav image", Context.MODE_PRIVATE);
        String date = sharedPref.getString(getString(R.string.date_key), "no date");

        if(!date.equals("no date")) {
            imageView = binding.nasaImage;
            Image favImage = imageHelper.getImage(date);
            imageView.setImageBitmap(favImage.getData());
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}