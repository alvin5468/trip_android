package com.example.mlj.mylocaljourney2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class OneDaySpotDetailActivity extends AppCompatActivity {
    private List<HotSpotInfo> mHotSpotInfoList;
    private ListView mListViewOneDaySpot;
    private String mSpotName;
    private String mPictureURL;
    private String mViewCount;
    private String mPlanCount;
    private String mDescription;
    private String mLatitude;
    private String mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_day_spot_detail);

        Bundle bundle = getIntent().getExtras();
        mSpotName = bundle.getString("hot_spot_title");
        mPictureURL = bundle.getString("picture_url");
        mPlanCount = bundle.getString("plan_count");
        mViewCount = bundle.getString("view_count");
        mDescription = bundle.getString("description");
        mLatitude = bundle.getString("latitude");
        mLongitude = bundle.getString("longitude");

        Utils.l("Libo debug : spotName ", mSpotName);
        Utils.l("Libo debug : pictureURL ", mPictureURL);
        Utils.l("Libo debug : mDescription ", mDescription);

        Utils.l("Libo debug : mLatitude ", mLatitude);
        Utils.l("Libo debug : mLongitude ", mLongitude);
        /*
        // fail. system reboot
        Bundle bundle = getIntent().getExtras();
        Object hotSpotInfo = bundle.getSerializable("hotSpotInfo");

        // work well
        //Object score = bundle.getSerializable("score");
        //Utils.l("Libo debug ", score.toString());
        */
    }
    protected void onStart() {
        super.onStart();

        mHotSpotInfoList =  GetHotSpotList();
        //  get ListView from layout
        mListViewOneDaySpot = (ListView)findViewById(R.id.lvOneDaySpot);

        mListViewOneDaySpot.setAdapter(new OneDaySpotAdapter( this, (ArrayList<HotSpotInfo>) mHotSpotInfoList));
        mListViewOneDaySpot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                String buffer="geo:"+mLatitude+mLongitude;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(buffer));
                startActivity(intent);
            }
        });
    }

    private ArrayList<HotSpotInfo> GetHotSpotList(){
        ArrayList<HotSpotInfo> HotSpotInfoList = new ArrayList<HotSpotInfo>();
        Location location = new Location();
        //HotSpotInfo(String name,String description,String picture, Location location, long viewCount, long planCount )
        for(int i=0;i<1;i++)
            HotSpotInfoList.add(new HotSpotInfo(mSpotName, mDescription, mPictureURL, location, Long.parseLong(mViewCount), Long.parseLong(mPlanCount) ));

        return HotSpotInfoList;
    }

    public class OneDaySpotAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<HotSpotInfo> HotSpotList;

        public OneDaySpotAdapter(Context context, ArrayList<HotSpotInfo> list) {
            layoutInflater = LayoutInflater.from(context);
            HotSpotList = list;
        }
        @Override
        public int getCount() {
            //Utils.l("Libo debug ");
            return HotSpotList.size();
        }
        @Override
        public Object getItem(int position) {
            //Utils.l("Libo debug ");
            return HotSpotList.get(position);
        }
        @Override
        public long getItemId(int position) {
            //Utils.l("Libo debug position " + position);
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.listview_one_day_hot_spot_item, parent, false);
            }
            HotSpotInfo hotSpotInfo = HotSpotList.get(position);
            ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);

            //  if string is not null or empty, show photo
            if(hotSpotInfo.getPicture() != null && !hotSpotInfo.getPicture().isEmpty()) {
                Picasso.with(getApplicationContext()).load(hotSpotInfo.getPicture()).fit().into(ivImage);
            }
            else
            {
                Utils.l("Libo debug: string is null ");
            }

            TextView tvSpotTitle = (TextView) convertView.findViewById(R.id.tvSpotTitle);
            tvSpotTitle.setText(hotSpotInfo.getName());

            TextView tvPlanCount = (TextView) convertView.findViewById(R.id.tvPlanCount);
            tvPlanCount.setText(String.valueOf(hotSpotInfo.getPlanCount())+"次規劃");

            TextView tvViewCount = (TextView) convertView.findViewById(R.id.tvViewCount);
            tvViewCount.setText(String.valueOf(hotSpotInfo.getViewCount())+"次瀏覽");

            TextView tvSpotDescription = (TextView) convertView.findViewById(R.id.tvSpotDescription);
            tvSpotDescription.setText(hotSpotInfo.getDescription());

            return convertView;
        }
    }

}
