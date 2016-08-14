package com.example.mlj.mylocaljourney2;


import android.content.ContentValues;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.firebase.client.ValueEventListener;


import android.content.ContentValues;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Handler;

//import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private MyPagerAdapter mPagerAdapter;
    private Fragment mFragmentTripPlan = null;
    private Fragment mFragmentTripLogin = null;
    private Fragment mFragmentTripGuide = null;
    private ServerInfo mServerInfo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mServerInfo = new ServerInfo();
        Utils.l("Libo debug baseURL " + mServerInfo.getBaseURL());


        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mPagerAdapter);
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        //mPagerSlidingTabStrip.setTextSize(30);
        //mPagerSlidingTabStrip.setIndicatorColor(0x00000000); // setIndicatorColor as tran
        mPagerSlidingTabStrip.setDividerColor(0x00000000);

    }

    public class MyPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    //public class MyPagerAdapter extends FragmentPagerAdapter  {

        private final String[] TITLES = {"行程", "+", "攻略"};
        //ori private final int TabIcons[] = { R.drawable.trip_plan_default, R.drawable.trip_login_default , R.drawable.trip_guide_default };
        private final int TabIcons[] = { R.drawable.trip_plan_default, R.drawable.trip_guide_default, R.drawable.trip_login_default   };

        //private final int TabIcons[] = {  R.drawable.icon_trip_plan_show ,  R.drawable.icon_trip_login , R.drawable.icon_trip_guide_show};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int page_index) {
            Utils.l("Libo debug page_index " + page_index);
            return TITLES[page_index];
            /*  //show icon and text together, it can't work. why?
            // Generate title based on item position
            Drawable image = getResources().getDrawable(TabIcons[page_index]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            // Replace blank spaces with image icon
            SpannableString sb = new SpannableString("   " + TITLES[page_index]);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.d(TAG,"getPageTitle() page_index"+page_index);
            return sb;
            */

        }

        @Override
        public int getCount() {
            return TITLES.length;
        }


        @Override
        public int getPageIconResId(int page_index) {
            Utils.l("Libo debug page_index " + page_index);
            return TabIcons[page_index];
        }


        @Override
        public Fragment getItem(int page_index) {
            Utils.l("Libo debug page_index " + page_index);
            Fragment fragment = null;
            switch (page_index) {
                case 0:
                    fragment = TripPlanFragment.newInstance(page_index);
                    /*
                    if (mFragmentTripPlan == null || mFragmentTripPlan.isDetached() || mFragmentTripPlan.isRemoving()) {
                        fragment = TripPlanFragment.newInstance(page_index);
                        mFragmentTripPlan = fragment;
                        Log.d(TAG, "TripPlanFragment.newInstance(page_index) ");
                    } else {
                        Utils.l("Libo debug ");
                        return mFragmentTripPlan;
                    }
                    */
                    break;
                case 1:
                    fragment = TripGuideFragment.newInstance(page_index);
                    /*
                    if (mFragmentTripGuide == null || mFragmentTripGuide.isDetached() || mFragmentTripGuide.isRemoving()) {
                        fragment = TripGuideFragment.newInstance(page_index);
                        mFragmentTripGuide = fragment;
                        Log.d(TAG, "TripGuideFragment.newInstance(page_index) ");
                    } else {
                        Utils.l("Libo debug ");
                        return mFragmentTripGuide;
                    }
                    */
                    break;
                case 2:
                    fragment = TripLoginFragment.newInstance(page_index);
                    /*
                    if (mFragmentTripLogin == null || mFragmentTripLogin.isDetached() || mFragmentTripLogin.isRemoving()) {
                        fragment = TripLoginFragment.newInstance(page_index);
                        mFragmentTripLogin = fragment;
                        Log.d(TAG, "TripLoginFragment.newInstance(page_index) ");
                    } else{
                        Utils.l("Libo debug ");
                        return mFragmentTripLogin;
                    }
                    */
                    break;
            }
            return fragment;
        }
    }
}
