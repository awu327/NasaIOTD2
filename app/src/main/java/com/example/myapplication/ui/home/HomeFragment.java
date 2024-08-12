package com.example.myapplication.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ImageView imageView;
    private Image currentImage;
    private ImageHelper imageHelper;

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
                        "api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date="+date);

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
                        return "success";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "failure";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("success")) {
                Toast.makeText(getContext().getApplicationContext(), "Image Found!",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), ImagePreview.class);
                intent.putExtra("title", currentImage.getTitle());
                intent.putExtra("date", currentImage.getDate());
                intent.putExtra("url", imageURL);

                startActivity(intent);
            }
            else {
                Toast.makeText(getContext().getApplicationContext(), "No Image Found",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        final Button dateButton = binding.dateButton;
        final EditText editText = binding.datePicker;
        imageHelper = ImageHelper.instanceOfDatabase(getContext());

        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        dateButton.setOnClickListener((View v) -> {
            // call a thread to extract an image from the website.
            ImageExtractor imageExtractor =
                    new ImageExtractor(editText.getText().toString());
            imageExtractor.execute();
        });



        imageView = binding.nasaImage;

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}