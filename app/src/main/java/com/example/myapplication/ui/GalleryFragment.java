package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ImageDetails;
import com.example.myapplication.R;
import com.example.myapplication.SlideshowActivity;
import com.example.myapplication.databinding.FragmentGalleryBinding;
import com.example.myapplication.image.Image;
import com.example.myapplication.image.ImageHelper;
import com.example.myapplication.image.ImageListAdapter;

import java.util.ArrayList;


// Fragment for the gallery section of the application.
public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private ImageListAdapter imageListAdapter;
    private ArrayList<Image> imageList;
    private ImageHelper imageHelper;

    // Done
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ListView listView = binding.imageList;
        Button slideshowButton = binding.startSlideshowButton;

        imageHelper = ImageHelper.instanceOfDatabase(getContext());
        imageList = new ArrayList<>();

        imageList = imageHelper.getImageDetailsList();

        Log.d("imageList object, gallery", imageList.toString());

        imageListAdapter = new ImageListAdapter(getContext(), R.layout.image_list_item,
                R.id.image_list_item, imageList);

        listView.setAdapter(imageListAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String date = imageList.get(position).getDate();
            Intent intent = new Intent(getContext(), ImageDetails.class);
            intent.putExtra("date", date);
            startActivity(intent);
        });

        slideshowButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(getContext(), SlideshowActivity.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onResume() {
        // update the imageList should an image be deleted
        super.onResume();
        imageList.clear();
        imageList.addAll(imageHelper.getImageDetailsList());
        imageListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}