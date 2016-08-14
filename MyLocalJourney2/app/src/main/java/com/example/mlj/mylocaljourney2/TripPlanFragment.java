package com.example.mlj.mylocaljourney2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
    private int mPageIndex;
    private ImageView mImageView;
    private TextView mTextView;
    private ListView mListViewTripPlan;
    private List<TripPlanInfo> mTripPlanList;
    private int count=0;
    private String mUserId = null;
    private ServerInfo mServerInfo = null;
    private UserInfo mUserInfo = null;

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
            mTripPlanList = GetTripPlanList(); // get trip list from web server (firebase).
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


    private ArrayList<TripPlanInfo> GetTripPlanList(){
        String URL = mServerInfo.getBaseURL()+"/journey_begin/user/"+ mUserInfo.getUserId(); // "http://52.197.58.253//journey_begin/user/4232";
        ArrayList<TripPlanInfo> TripPlanList = new ArrayList<TripPlanInfo>();

        /*
        //Add data by using hard code
        TripPlanList.add( new TripPlanInfo("India", "2015/12/5~2015/12/20", "https://www.liverpool.ac.uk/media/research/india-fellowship-brochure-image.jpg"));
        TripPlanList.add( new TripPlanInfo("German", "2015/11/15~2015/12/01", "http://www.eztravel.com.tw/img/pm/FRT/FRT0000003084D03.gif"));
        TripPlanList.add( new TripPlanInfo("美國--美西十日遊", "2015/09/01~2015/09/10", "http://www.artisan.com.tw/images/blogs/%E8%8E%8A%E7%AB%8B%E8%82%B2_%E7%BE%8E%E5%9C%8B%E5%A4%A7%E5%B3%BD%E8%B0%B703.jpg"));
        TripPlanList.add( new TripPlanInfo("中國--內蒙古十日遊", "2015/08/12~2015/08/20", "http://www.znhzzxwneimenggu.com/eWebEditor/uploadfile/20150804/20150804105805841.jpg"));
        */

        // get JSON from web server, add data
        DownloadTripPlanTask TripPlanTask = new DownloadTripPlanTask();
        TripPlanTask.execute(URL);
        try {
            JSONObject json = TripPlanTask.get();
            Log.d(TAG, "json : " + json);

            try {
                String Status = json.getString("status");
                String userId = json.getString("userId");

                JSONArray tripPlanInfoJson = json.getJSONArray("journeys");//Get JSONArray
                String hashUrl;
                String id;
                String picture;
                String startDate;
                String title;

                for(int index=0;index<tripPlanInfoJson.length();index++){
                    JSONObject oj = tripPlanInfoJson.getJSONObject(index);
                    title=oj.getString("title");
                    //hashUrl=oj.getString("hashUrl");
                    id=oj.getString("id");
                    picture=oj.getString("picture");
                    startDate=oj.getString("startDate");

                   // String tmpPicture = picture.substring(28);

                    TripPlanList.add(new TripPlanInfo(id, title, startDate, picture));

                    Log.d(TAG, "title " + title);
                    Log.d(TAG, "picture " + picture);
                    Log.d(TAG, "startDate " + startDate);
                    Log.d(TAG, "id " + id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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

            //  if string is not null or empty, show photo
            if(tripplaninfo.getImageURL() != null && !tripplaninfo.getImageURL().isEmpty()) {
                Picasso.with(getContext()).load(tripplaninfo.getImageURL()).fit().into(ivImage);
            }
            else
            {
                Utils.l("Libo debug: string is null ");
            }


            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            tvTitle.setText(String.valueOf(tripplaninfo.getTitle()));

            TextView tvDay = (TextView) convertView.findViewById(R.id.tvDay);
            tvDay.setText(tripplaninfo.getDay());
            return convertView;
        }
    }

}
