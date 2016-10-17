package com.bignerdranch.android.testapplication.posts.adapter;

/*
 * Created by Lidchanin on 17.10.2016.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bignerdranch.android.testapplication.R;

import java.util.List;

public class ThreeHorizontalTextViewsAdapter extends ArrayAdapter<ThreeStrings> {

    private int layoutResource;

    public ThreeHorizontalTextViewsAdapter(Context context, int layoutResource, List<ThreeStrings> threeStringsList) {
        super(context, layoutResource, threeStringsList);
        this.layoutResource = layoutResource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }
        ThreeStrings threeStrings = getItem(position);
        if (threeStrings != null) {
            TextView leftTextView = (TextView) view.findViewById(R.id.leftTextView);
            TextView rightTextView = (TextView) view.findViewById(R.id.rightTextView);
            TextView centreTextView = (TextView) view.findViewById(R.id.centreTextView);
            if (leftTextView != null) {
                leftTextView.setText(threeStrings.getLeft());
            }
            if (rightTextView != null) {
                rightTextView.setText(threeStrings.getRight());
            }
            if (centreTextView != null) {
                centreTextView.setText(threeStrings.getCentre());
            }
        }
        return view;
    }
}