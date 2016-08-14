package com.example.mlj.mylocaljourney2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import java.io.Serializable;

public class TripDetailActivity extends AppCompatActivity implements
        OneTripPlanInfoFragment.CallbackInterface {

    private final static String TAG = "TripDetailActivity";
    private GoogleMap mGoogleMap;;
    private List<HotSpotInfo> mHotSpotInfoList;
    private List<LatLng> mLatLngList = new ArrayList<LatLng>();
    private List<Marker> mMarkerList = new ArrayList<Marker>();
    private ServerInfo mServerInfo = null;
    private String mJourneyBeginId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        Bundle bundleIntent = getIntent().getExtras();
        String uniqueID = bundleIntent.getString("uniqueID");
        String title = bundleIntent.getString("title");
        Log.d(TAG, "uniqueID " + uniqueID);
        Log.d(TAG, "title " + title);

        mJourneyBeginId = uniqueID;

        mServerInfo = new ServerInfo();
        // init Map
        //initMap();
    }

    protected void onStart(){
        super.onStart();
        HotSpotInfo hotSpotInfo;
        ArrayList<String> SpotList = new ArrayList<String>();
        // get SpotInfo from server
        mHotSpotInfoList = GetTripDetailListNew();

        Log.d(TAG, "mHotSpotInfoList.size() " + mHotSpotInfoList.size());
        for(int index=0;index<mHotSpotInfoList.size();index++) {
            hotSpotInfo = mHotSpotInfoList.get(index);
            SpotList.add(hotSpotInfo.getName());
            Log.d(TAG, hotSpotInfo.getName() + "< " + hotSpotInfo.getLocation().getLatitude() + " , " + hotSpotInfo.getLocation().getLongitude() + " > ");
        }
        // init Point
        initPoints();

        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentById(R.id.frameLayout);
        if (fragment == null) {
            OneTripPlanInfoFragment oneTripPlanInfoFragment = OneTripPlanInfoFragment.newInstance("Test");

            //OneTripPlanInfoFragment oneTripPlanInfoFragment = new OneTripPlanInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("SpotList",SpotList);

            oneTripPlanInfoFragment.setArguments(bundle);
            transaction.add(R.id.frameLayout, oneTripPlanInfoFragment, TAG);
            transaction.commit();
        }

    }

    protected void onResume(){
        super.onResume();
        initMap();
    }

    public void updateInfoWindow(int index) // OneTripPlanInfoFragment will call this callback
    {
        Utils.l("Libo debug index " + index);
        Marker marker_tmp = mMarkerList.get(index);

        if( marker_tmp != null ) {
            marker_tmp.showInfoWindow(); // show an info window (showInfoWindow())

            // move camera
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(mLatLngList.get(index))
                    .zoom(8)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory
                    .newCameraPosition(cameraPosition);
            mGoogleMap.animateCamera(cameraUpdate);

        }
    }

    private class DownloadTripDetailTask extends AsyncTask<String, Integer, JSONObject> {

        protected JSONObject doInBackground(String... urlStrings) {
            String url = urlStrings[0];
            Log.d(TAG, "url " + url);
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(JSONObject json) {
            try {

            } catch (
            Exception e) {
                e.printStackTrace();
            }
        }
    }
    private ArrayList<HotSpotInfo> GetTripDetailListNew(){
        //String URL = "http://52.197.58.253/journey/4136";
        String URL = mServerInfo.getBaseURL();
        URL = URL + "/journey/" + mJourneyBeginId;
        Utils.l("Libo debug URL " + URL);

        ArrayList<HotSpotInfo> HotSpotInfoList = new ArrayList<HotSpotInfo>();

        // get JSON from web server, add data
        DownloadTripDetailTask TripDetailTask = new DownloadTripDetailTask();
        TripDetailTask.execute(URL);
        try {
            JSONObject json = TripDetailTask.get();
            Log.d(TAG, "json : " + json);
            try {
                String Status = json.getString("status");
                long journeyId = 0;
                String pictureURL;
                String title;
                JSONObject journeyJsnObj = null;
                // contacts JSONArray
                JSONArray journeySpotsJsnArray = null;

                Log.d(TAG, "status is " + Status);
                journeyJsnObj = json.getJSONObject("journey");//Get JSONArray
                journeyId = journeyJsnObj.getLong("id");
                pictureURL = journeyJsnObj.getString("picture");
                title = journeyJsnObj.getString("title");

                Log.d(TAG, "id " + String.valueOf(journeyId) );
                Log.d(TAG, "pictureURL " + pictureURL);
                Log.d(TAG, "title " + title);

                journeySpotsJsnArray = journeyJsnObj.getJSONArray("journeySpots");
                for (int i = 0; i < journeySpotsJsnArray.length(); i++) {
                    JSONObject c = journeySpotsJsnArray.getJSONObject(i);
                    int budget = 0;
                    long journeySpotsId = 0;
                    String remark;
                    String startTime;
                    budget = c.getInt("budget");
                    journeySpotsId = c.getInt("id");
                    remark = c.getString("remark");
                    startTime = c.getString("startTime");

                    Log.d(TAG, "bugdet " + String.valueOf(budget) );
                    Log.d(TAG, "journeySpotsId " + String.valueOf(journeySpotsId) );
                    Log.d(TAG, "remark " + remark);
                    Log.d(TAG, "startTime " + startTime);

                    JSONObject spotJsnObj = c.getJSONObject("spot");
                    String description;
                    String info;
                    String name;
                    String spotPictureURL;
                    String placeId;
                    long spotID = 0;
                    long planCount = 0;
                    long viewCount = 0;
                    description = spotJsnObj.getString("description");
                    info = spotJsnObj.getString("info");
                    name = spotJsnObj.getString("name");
                    spotPictureURL = spotJsnObj.getString("picture");
                    placeId = spotJsnObj.getString("placeId");
                    spotID = spotJsnObj.getLong("id");
                    planCount = spotJsnObj.getLong("planCount");
                    viewCount = spotJsnObj.getLong("viewCount");

                    // location node is JSON Object
                    JSONObject locationJsnObj = spotJsnObj.getJSONObject("location");
                    double latitude;
                    double longitude;
                    latitude = locationJsnObj.getDouble("latitude");
                    longitude = locationJsnObj.getDouble("longitude");

                    String stLatitude = String.valueOf(latitude);
                    String stLongitude = String.valueOf(longitude);
                    Location location = new Location(stLatitude,stLongitude);
                    //HotSpotInfo(String name,String description,String picture, Location location, long viewCount, long planCount )
                    HotSpotInfoList.add( new HotSpotInfo( name, description, spotPictureURL,location,viewCount, planCount));


                    Log.d(TAG, "description " + description);
                    Log.d(TAG, "info " + info);
                    Log.d(TAG, "name " + name);
                    Log.d(TAG, "spotPictureURL " + spotPictureURL);
                    Log.d(TAG, "placeId " + placeId);

                    Log.d(TAG, "spotID " + String.valueOf(spotID) );
                    Log.d(TAG, "planCount " + String.valueOf(planCount) );
                    Log.d(TAG, "viewCount " + String.valueOf(viewCount) );
                    Log.d(TAG, "latitude " + String.valueOf(latitude) );
                    Log.d(TAG, "longitude " + String.valueOf(longitude) );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return HotSpotInfoList;
    }

    private void initPoints() {

        HotSpotInfo hotSpotInfo;
        for(int index=0;index<mHotSpotInfoList.size();index++)
        {
            hotSpotInfo = mHotSpotInfoList.get(index);
            Log.d(TAG, "initPoints" + "< " + hotSpotInfo.getLocation().getLatitude() + " , " + hotSpotInfo.getLocation().getLongitude() + " > ");
            LatLng LatLng_tmp = new LatLng(Double.valueOf(hotSpotInfo.getLocation().getLatitude()), Double.valueOf(hotSpotInfo.getLocation().getLongitude()));
            mLatLngList.add(LatLng_tmp);
        }
    }

    private void initMap() {
        if (mGoogleMap == null) {
            mGoogleMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fmMap)).getMap();
            if (mGoogleMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mGoogleMap.setTrafficEnabled(true);
        mGoogleMap.setMyLocationEnabled(true);
        //uiSettings = map.getUiSettings();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mLatLngList.get(0))
                .zoom(8)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newCameraPosition(cameraPosition);
        mGoogleMap.animateCamera(cameraUpdate);

        addMarkersToMap();
        mGoogleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        MyMarkerListener myMarkerListener = new MyMarkerListener();
        mGoogleMap.setOnMarkerClickListener(myMarkerListener);
        mGoogleMap.setOnInfoWindowClickListener(myMarkerListener);
    }

    private void addMarkersToMap() {
        HotSpotInfo hotSpotInfo;
        Marker marker_tmp = null;
        for(int index=0;index<mHotSpotInfoList.size();index++)
        {
            hotSpotInfo = mHotSpotInfoList.get(index);

            marker_tmp = mGoogleMap.addMarker(new MarkerOptions()
                    .position(mLatLngList.get(index))
                    .title(hotSpotInfo.getName())
                    .snippet(String.valueOf(hotSpotInfo.getPlanCount()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMarkerList.add(marker_tmp);
        }

        // show an info window programmatically by calling showInfoWindow()
        /*
        if( marker_tmp != null ) {
            marker_tmp.showInfoWindow();
        }
        */

        /*
        marker_1 = mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng_1)
                .title("柏林大教堂")
                .snippet("柏林大教堂")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        */
    }
    private class MyMarkerListener implements GoogleMap.OnMarkerClickListener,
            GoogleMap.OnInfoWindowClickListener {
        @Override
        public boolean onMarkerClick(Marker marker) {
            //showToast(marker.getTitle());
            return false;
        }

        @Override
        public void onInfoWindowClick(Marker marker) {
            showToast(marker.getTitle());

            int index = getIndexFromMarkList(marker);

            //create a activity to show more information about this spot in a day
            gotoOneDaySpotDetailActivity(index);
        }
    }
    private void gotoOneDaySpotDetailActivity(int Index)
    {
        //new一個intent物件，並指定Activity切換的class
        Intent myIntent = new Intent(this, OneDaySpotDetailActivity.class);

        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();

        // pass object via Serializable, Parcelable is better than Serializable
        /* // I don't know why it fail.
        HotSpotInfo hotSpotInfo = mHotSpotInfoList.get(Index);
        bundle.putSerializable("hotSpotInfo", hotSpotInfo );
        */

        /*  // work
        int programming = 10;
        int dataStructure = 20;
        int algorithm = 30;
        Score score = new Score(programming, dataStructure, algorithm);
        bundle.putSerializable("score", score);
        */
        Utils.l("Libo debug Index " + Index);
        bundle.putString("hot_spot_title", mHotSpotInfoList.get(Index).getName());
        bundle.putString("plan_count", String.valueOf(mHotSpotInfoList.get(Index).getPlanCount()));
        bundle.putString("view_count",  String.valueOf(mHotSpotInfoList.get(Index).getViewCount()));
        bundle.putString("picture_url", mHotSpotInfoList.get(Index).getPicture());
        bundle.putString("description", mHotSpotInfoList.get(Index).getDescription());
        bundle.putString("latitude", mHotSpotInfoList.get(Index).getLocation().getLatitude() );
        bundle.putString("longitude", mHotSpotInfoList.get(Index).getLocation().getLongitude() );


        //將Bundle物件傳給intent
        myIntent.putExtras(bundle);
        startActivity(myIntent);

    }

    //get index from mMarkerList
    public int getIndexFromMarkList(Marker marker){
        int index = 0;
        for( index=0;index<mMarkerList.size();index++){
            if(marker.equals(mMarkerList.get(index)) )
                break;
        }
        return index;
    }

    private class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View infoWindow;

        MyInfoWindowAdapter() {
            infoWindow = View.inflate(
                    TripDetailActivity.this,
                    R.layout.custom_info_window,
                    null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            int logoId = R.drawable.berlin_dom;
            int index=0;

            ImageView ivLogo = ((ImageView) infoWindow
                    .findViewById(R.id.ivLogo));
            //ivLogo.setImageResource(logoId); //show default photo

            //get index from mMarkerList, after getting index, I can use picasso to load picture
            index = getIndexFromMarkList(marker);

            HotSpotInfo hotSpotInfo = mHotSpotInfoList.get(index);
            Utils.l("Libo debug i " + index);
            Utils.l("Libo debug picture url " + mHotSpotInfoList.get(index).getPicture());

            if(hotSpotInfo.getPicture() != null && !hotSpotInfo.getPicture().isEmpty()) {
                //Picasso.with(TripDetailActivity.this).load(hotSpotInfo.getPicture()).fit().into(ivLogo);
                Picasso.with(getApplicationContext()).load(hotSpotInfo.getPicture()).fit().into(ivLogo);
            }
            else
            {
                ivLogo.setImageResource(logoId); //show default photo
                Utils.l("Libo debug: string is null ");
            }

            String title = marker.getTitle();
            TextView tvTitle = ((TextView) infoWindow
                    .findViewById(R.id.tvTitle));
            tvTitle.setText(title);

            String snippet = marker.getSnippet();
            TextView tvSnippet = ((TextView) infoWindow
                    .findViewById(R.id.tvSnippet));
            tvSnippet.setText(snippet+"次規劃");

            return infoWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
