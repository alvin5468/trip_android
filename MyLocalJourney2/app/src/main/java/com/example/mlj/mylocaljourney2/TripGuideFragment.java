package com.example.mlj.mylocaljourney2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class TripGuideFragment extends Fragment {
    private static final String ARG_PAGEINDEX = "PageIndex";
    private final static String TAG = "TripGuideFragment";
    private int mPageIndex;
    private ImageView mImageView;
    private int count=0;

    public static TripGuideFragment newInstance(int PageIndex) {
        TripGuideFragment f = new TripGuideFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PAGEINDEX, PageIndex);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPageIndex = getArguments().getInt(ARG_PAGEINDEX);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_guide, container, false);
        /*
        View rootView = inflater.inflate(R.layout.fragment_trip_guide, container, false);
        mImageView = (ImageView)rootView.findViewById(R.id.imageView2);
        Button mButton = (Button) rootView.findViewById(R.id.button3);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                count++;
                Log.d(TAG, "Picasso click"+ count );
                if(count%2==0)
                    Picasso.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into(mImageView);
                else
                    Picasso.with(getContext()).load("http://square.github.io/picasso/static/sample.png").into(mImageView);
            }
        });
        return rootView;
        */

    }

}