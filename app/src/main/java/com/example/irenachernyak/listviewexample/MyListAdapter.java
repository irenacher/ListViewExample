package com.example.irenachernyak.listviewexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.irenachernyak.listviewexample.R;

class MyListAdapter extends ArrayAdapter<String>
{

    public MyListAdapter(Context context, String[] objects) {
        super(context, R.layout.row_layout_2, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());
        View theView = theInflater.inflate(R.layout.row_layout_2, parent, false);

        String tvShow = getItem(position);
        TextView tv = (TextView)theView.findViewById(R.id.textView1);
        tv.setText(tvShow);

        ImageView iv = (ImageView)theView.findViewById(R.id.dot_imageview);

        iv.setImageResource(R.drawable.dot);

        return theView;
    }
}