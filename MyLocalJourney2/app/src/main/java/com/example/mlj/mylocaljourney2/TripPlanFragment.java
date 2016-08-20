package com.example.mlj.mylocaljourney2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Member;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.mlj.mylocaljourney2.TripLoginFragment.*;

public class TripPlanFragment extends Fragment {
    private static final String ARG_PAGEINDEX = "PageIndex";
    private final static String TAG = "TripPlanFragment";
    private final String STRING_SUCCESS="SUCCESS";
    private int mPageIndex;
    private ImageView mImageView;
    private TextView mTextView;
    private ListView mListViewTripPlan;
    private List<TripPlanInfo> mTripPlanList;
    private int count=0;
    private String mUserId = null;
    private ServerInfo mServerInfo = null;
    private UserInfo mUserInfo = null;
    private TripInfoDB mTripInfoDB = null;
    private boolean mIsConnected = false;

    public static TripPlanFragment newInstance(int PageIndex) {
        TripPlanFragment f = new TripPlanFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PAGEINDEX, PageIndex);
        f.setArguments(b);
        Utils.l("Libo debug ");
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.l("Libo debug ");

        mServerInfo = new ServerInfo();
        Utils.l("Libo debug baseURL " + mServerInfo.getBaseURL());

        mPageIndex = getArguments().getInt(ARG_PAGEINDEX);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_trip_plan, container, false);
        Utils.l("Libo debug ");
        View rootView = inflater.inflate(R.layout.fragment_trip_plan, container, false);
        mTextView = (TextView) rootView.findViewById(R.id.textView1);

        mTripInfoDB = new TripInfoDB(getActivity());
        mTripInfoDB.Init();


        mUserInfo = new UserInfo(getActivity());
        mUserId = mUserInfo.getUserId();
        Utils.l("Libo debug : mUserId " + mUserId);

        int is_user_id_valid = mUserInfo.getIsLoginStatus();
        Utils.l("Libo debug : is_user_id_valid " + is_user_id_valid);

        //TripLoginFragment.GetUserId();

        if( is_user_id_valid == -1 || is_user_id_valid == 0) {
            mTextView.setText("請先登入，才能看自已建的行程\n");
        }
        else {

        /*
        Drawable drawable = getResources().getDrawable(android.R.drawable.ic_menu_my_calendar);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        SpannableString spannable = new SpannableString(mTextView.getText().toString() + "[smile]");
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        spannable.setSpan(span, mTextView.getText().length(),
                mTextView.getText().length()+"[smile]".length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mTextView.setText(spannable);
        */

            //  get ListView from layout
            mListViewTripPlan = (ListView) rootView.findViewById(R.id.lvTripPlan);
            try {
                mTripPlanList = GetTripPlanList(); // get trip list from web server (firebase).
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mListViewTripPlan.setAdapter(new TripPlanAdapter(getActivity(), (ArrayList<TripPlanInfo>) mTripPlanList));
            mListViewTripPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Log.d(TAG, "id " + id);
                    Log.d(TAG, "position " + position);
                    String title;
                    String uniqueID;

                    title = mTripPlanList.get(position).getTitle();
                    uniqueID = mTripPlanList.get(position).getUniqueID();
                    gotoTripDetailActivity(uniqueID, title);
                }
            });
        }

        return rootView;
    }
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
    private void gotoTripDetailActivity(String uniqueID,String title)
    {
        //new一個intent物件，並指定Activity切換的class
        Intent myIntent = new Intent(super.getActivity(), TripDetailActivity.class);

        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();
        bundle.putString("uniqueID",uniqueID);
        bundle.putString("title", title);

        //將Bundle物件傳給intent
        myIntent.putExtras(bundle);
        startActivity(myIntent);
    }

    private class DownloadTripPlanTask extends AsyncTask<String, Integer, JSONObject> {

        protected JSONObject doInBackground(String... urlStrings) {
            /*
            ContentValues nameValuePairs = new ContentValues();
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .encodedAuthority("brilliant-inferno-7297.firebaseio.com")
                    .appendEncodedPath("tripPlan/.json");
            String url = builder.build().toString();
            */
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
            //Log.d(TAG, "json " + json);
        }
    }

    private ArrayList<TripPlanInfo> ParseJSONAddTripPlanInfo(JSONObject json, boolean isNetworkReady){
        ArrayList<TripPlanInfo> TripPlanList = new ArrayList<TripPlanInfo>();

            try {
                String Status = json.getString("status");
                String userId = json.getString("userId");

                if (STRING_SUCCESS.compareToIgnoreCase(Status) == 0) {
                    Utils.l("Libo debug : Status " + Status);
                    if(isNetworkReady == true) {  // if network is ready, allow add and update database
                        if (mTripInfoDB.GetJourneyBeginCount() == 0) {
                            Utils.l("Libo debug : AddJourneyBeginData ");
                            //add data
                            mTripInfoDB.AddJourneyBeginData(mUserInfo.getUserId(), json.toString());
                        } else if (mTripInfoDB.GetJourneyBeginCount() >= 2) {
                            Utils.l("Libo debug : DeleteAllJourneyBegid ");
                            // test delete database // if data count is larger than 2, delete all data
                            mTripInfoDB.DeleteAllJourneyBegid();
                        } else {
                            Utils.l("Libo debug : UpdateJourneyBeginData ");
                            //update data, not verify
                            mTripInfoDB.UpdateJourneyBeginData(mUserInfo.getUserId(), json.toString());
                        }
                    }

                    JSONArray tripPlanInfoJson = json.getJSONArray("journeys");//Get JSONArray

                    String hashUrl;
                    String id;
                    String picture;
                    //String startDate;
                    String title;

                    for (int index = 0; index < tripPlanInfoJson.length(); index++) {
                        JSONObject oj = tripPlanInfoJson.getJSONObject(index);
                        title = oj.getString("title");
                        //hashUrl=oj.getString("hashUrl");
                        id = oj.getString("id");
                        picture = oj.getString("picture");
                        Date startDate = new Date(oj.getString("startDate"));
                        Date endDate = new Date(oj.getString("endDate"));
                        TripPlanList.add(new TripPlanInfo(id, title, Utils.dateToString(startDate) + " ~ " + Utils.dateToString(endDate), picture));

                        Log.d(TAG, "title " + title);
                        Log.d(TAG, "picture " + picture);
                        Log.d(TAG, "startDate " + startDate);
                        Log.d(TAG, "id " + id);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        return TripPlanList;
    }

    private ArrayList<TripPlanInfo> GetTripPlanList() throws JSONException {
        String URL = mServerInfo.getBaseURL()+"/journey_begin/user/"+ mUserInfo.getUserId(); // "http://52.197.58.253//journey_begin/user/4232";
        ArrayList<TripPlanInfo> TripPlanList = new ArrayList<TripPlanInfo>();
        mIsConnected = isConnected();
        if( mIsConnected == true) // network is ready
        {
            Utils.l("Libo debug : network is ready ");
            // get JSON from web server, add data
            DownloadTripPlanTask TripPlanTask = new DownloadTripPlanTask();
            TripPlanTask.execute(URL);
            try {
                JSONObject json = TripPlanTask.get();
                Utils.l("Libo debug : json " + json);
                TripPlanList = ParseJSONAddTripPlanInfo( json, mIsConnected);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else{
            Utils.l("Libo debug : GetJourneyBeginCount() " + mTripInfoDB.GetJourneyBeginCount());
            Utils.l("Libo debug : network is not ready ");
            if (mTripInfoDB.GetJourneyBeginCount() > 0)
            {
                String jsonStr = mTripInfoDB.GetJourneyBeginData();
                Utils.l("Libo debug : jsonStr " + jsonStr);
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(jsonStr);

                TripPlanList = ParseJSONAddTripPlanInfo( jsonObject, mIsConnected);
            }
        }
        return TripPlanList;
    }

    public class TripPlanAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<TripPlanInfo> TripPlanList;

        public TripPlanAdapter(Context context, ArrayList<TripPlanInfo> list) {
            layoutInflater = LayoutInflater.from(context);
            TripPlanList = list;
        }

        @Override
        public int getCount() {
            //Utils.l("Libo debug ");
            return TripPlanList.size();
        }

        @Override
        public Object getItem(int position) {
            //Utils.l("Libo debug ");
            return TripPlanList.get(position);
        }


        @Override
        public long getItemId(int position) {
            Utils.l("Libo debug position " + position);
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                //Utils.l("Libo debug ");
                convertView = layoutInflater.inflate(R.layout.listview_item, parent, false);
            }

            /*
            TripPlanInfo tripplaninfo = TripPlanList.get(position);
            ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            ivImage.setImageResource(tripplaninfo.getImage());
            */

            //Utils.l("Libo debug ");
            TripPlanInfo tripplaninfo = TripPlanList.get(position);
            ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);

            if(mIsConnected==true){
                //  if string is not null or empty, show photo
                if(tripplaninfo.getImageURL() != null && !tripplaninfo.getImageURL().isEmpty()) {
                    Picasso.with(getContext()).load(tripplaninfo.getImageURL()).fit().into(ivImage);
                }
                else
                {
                    Utils.l("Libo debug: string is null ");
                }
            }
            else{
                Utils.l("Libo debug: network is not ready, load default photo ");
                Picasso.with(getContext()).load(R.drawable.berlin_dom).fit().into(ivImage);
            }




            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            tvTitle.setText(String.valueOf(tripplaninfo.getTitle()));

            TextView tvDay = (TextView) convertView.findViewById(R.id.tvDay);
            tvDay.setText(tripplaninfo.getDay());
            return convertView;
        }
    }

}
