package com.example.myapplication.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.ImageDetails;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentGalleryBinding;
import com.example.myapplication.image.Image;
import com.example.myapplication.image.ImageHelper;
import com.example.myapplication.image.ImageListAdapter;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private ImageListAdapter imageListAdapter;
    private ArrayList<Image> imageList;
    private ImageHelper imageHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView listView = binding.imageList;

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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}