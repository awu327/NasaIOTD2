package com.example.myapplication.image;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.ArrayList;

// ImageListAdapter class to display proper text
public class ImageListAdapter extends BaseAdapter {

    private Context ctx;
    ArrayList<Image> imageList = new ArrayList<>();

    public ImageListAdapter(@NonNull Context ctx, int resource, int textViewResourceId, @NonNull ArrayList imageList) {
        this.ctx = ctx;
        this.imageList = imageList;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Nullable
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get required items, and set their values for the list.
        View imageItem = View.inflate(ctx, R.layout.image_list_item, null);
        TextView titleText = imageItem.findViewById(R.id.title_text);
        TextView dateText = imageItem.findViewById(R.id.date_text);

        titleText.setText(imageList.get(position).getTitle());
        dateText.setText("date: " + imageList.get(position).getDate());

        return imageItem;
    }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public int getCount() { return imageList.size(); }
}
