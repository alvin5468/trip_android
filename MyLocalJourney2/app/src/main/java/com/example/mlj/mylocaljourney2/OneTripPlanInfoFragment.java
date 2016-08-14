package com.example.mlj.mylocaljourney2;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class OneTripPlanInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final static String TAG = "OneTripPlanInfoFragment";
    private static final String ARG_PARAM1 = "param1";
    private ImageButton mImgBtnCamera;
    private ImageButton mImgBtnArrow;
    private int mIsShowInfo;
    private  ListView mlvTripInfo;
    private static final int REQUEST_CODE_TAKE_PICTURE = 1;
    ArrayList<String> mSpotList = null;
    private CallbackInterface mCallback;

    // TODO: Rename and change types of parameters
    private String mParam1;

    public interface CallbackInterface {
        public void updateInfoWindow(int index);

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment OneTripPlanInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OneTripPlanInfoFragment newInstance(String param1) {
        Utils.l("Libo debug ");
        OneTripPlanInfoFragment fragment = new OneTripPlanInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        Utils.l("Libo debug ");
        return fragment;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mCallback = (CallbackInterface) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() +
            "must implement OneTripPlanInfoFragment.CallbackInterface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.l("Libo debug ");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_one_trip_plan_info, container, false);

        super.onCreateView(inflater, container, savedInstanceState);
        Utils.l("Libo debug ");
        View view = inflater.inflate(R.layout.fragment_one_trip_plan_info, container, false);
        TextView textView = (TextView) view.findViewById(R.id.tvTripPlan);
        String title = getArguments().getString("title");
        String uniqueID  = getArguments().getString("uniqueID");

        ArrayList<String> SpotList = new ArrayList<String>();
        mSpotList = getArguments().getStringArrayList("SpotList");

        mImgBtnCamera = (ImageButton) view.findViewById(R.id.imgBtnCamera);
        mImgBtnArrow = (ImageButton) view.findViewById(R.id.imgBtnArrow);

        mImgBtnCamera.setOnClickListener(imgBtnCamerOnClick);
        mImgBtnArrow.setOnClickListener(imgBtnArrowOnClick);

        textView.setText("行程規劃");

        //  change textSize
        int textSize = (int)textView.getTextSize();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize + 10);

        mIsShowInfo = 1;

        //  use HotSpotListAdapter
        ArrayAdapter<String> HotSpotListAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mSpotList);
        mlvTripInfo = (ListView) view.findViewById(R.id.listView);
        // Set the ArrayAdapter as the ListView's adapter.
        mlvTripInfo.setAdapter(HotSpotListAdapter);
        mlvTripInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(TAG, "id " + id);
                Log.d(TAG, "position " + position);
                String name;

                name = mSpotList.get(position);
                Utils.l("Libo debug: name " + name );
                mCallback.updateInfoWindow(position); // it will call function of TripDetailActivity
            }
        });

        Utils.l("Libo debug ");
        return view;

    }

    private View.OnClickListener imgBtnCamerOnClick = new View.OnClickListener() {
        public void onClick(View v){
            Utils.l("Libo debug ");
            Log.d(TAG,"imgBtnCameraOnClick");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        }
    };
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Utils.l("Libo debug ");
        Bitmap picture;
        Uri uri;
        ExifInterface exif;
        String filepath, StrLatitude, StrLongitude;
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_TAKE_PICTURE:

            }
        }
    }

    private View.OnClickListener imgBtnArrowOnClick = new View.OnClickListener(){
        public void onClick(View v){
            Log.d(TAG,"imgBtnArrowOnClick");
            if(mIsShowInfo ==1)// if ShowInfo == 1, then we collapse ListView
            {
                collapseAnimation(mlvTripInfo, 200, 0);//change ListView height as 0
                mImgBtnArrow.setImageResource(R.drawable.ic_arrow_top);
                mIsShowInfo = 0;
            }
            else  // if mIsShowInfo == 0, we expand ListView
            {
                expandAnimation(mlvTripInfo, 200, 290);//change ListView height as 290
                mImgBtnArrow.setImageResource(R.drawable.ic_arrow_bottom);
                mIsShowInfo = 1;
            }
        }
    };

    public static void expandAnimation(final View v, int duration, int targetHeight) {
        int prevHeight  = v.getHeight();
        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static void collapseAnimation(final View v, int duration, int targetHeight) {
        int prevHeight  = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }





}
