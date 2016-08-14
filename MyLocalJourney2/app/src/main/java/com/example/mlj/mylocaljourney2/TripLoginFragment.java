package com.example.mlj.mylocaljourney2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TripLoginFragment extends Fragment {
    private static final String ARG_PAGEINDEX = "PageIndex";
    private final static String TAG = "TripLoginFragment";
    private int mPageIndex;
    private ImageView mImageView;
    private int count=0;

    private CallbackManager callbackManager;
    private TextView mTVHiUser;
    private TextView mTVLoginOrDescription;

    private ImageView mProfileImgView;

    private Button mBtnLogin;
    private Button mBtnRegister;
    final int REQUEST_CODE_LOGIN_ACTIVITY = 100;
    private long mFacebookId = -1;
    private long mUserId = -1;
    private ServerInfo mServerInfo = null;
    private UserInfo mUserInfo = null;


    public static TripLoginFragment newInstance(int PageIndex) {
        TripLoginFragment f = new TripLoginFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PAGEINDEX, PageIndex);
        f.setArguments(b);
        return f;
    }
    public long GetUserId(){
        return mUserId;
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPageIndex = getArguments().getInt(ARG_PAGEINDEX);
        Utils.l("Libo debug ");

        FacebookSdk.sdkInitialize(getContext());
        //callbackManager = CallbackManager.Factory.create();

        mServerInfo = new ServerInfo();

        mUserInfo = new UserInfo(getActivity());
    }

    public void gotoLoginActivity(View v){
        //new一個intent物件，並指定Activity切換的class
        Intent myIntent = new Intent(super.getActivity(), LoginActivity.class);

        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();

        //
        myIntent.putExtra("ID","alvin5468");//debug

        //將Bundle物件傳給intent
        myIntent.putExtras(bundle);
        startActivityForResult(myIntent, REQUEST_CODE_LOGIN_ACTIVITY);

    }

    private View.OnClickListener BtnLoginOnClick = new View.OnClickListener(){
        public void onClick(View v) {

            String LoginType = mUserInfo.getLoginType();
            int isLoginStatus = mUserInfo.getIsLoginStatus();
            Utils.l("Libo debug isLoginStatus " + isLoginStatus);
            Utils.l("Libo debug : LoginType " +LoginType);
            String s = "mylocaljourney";
            if( isLoginStatus == 1 && s.equals(LoginType) ){
                setLoginUI();
                mUserInfo.setIsLoginStatus(0);
                mUserInfo.setUserId("null");

            } else{
                setLoginUI();
                gotoLoginActivity(v);
            }

            //gotoLoginActivity(v);
            //libo error //getActivity().getFragmentManager().popBackStack();
        }
    };
    private View.OnClickListener BtnRegisterOnClick = new View.OnClickListener(){
        public void onClick(View v) {
            Utils.l("Libo debug ");
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_trip_login, container, false);
        View rootView = inflater.inflate(R.layout.fragment_trip_login, container, false);

        /*
        View rootView;
        count++;
        Utils.l("Libo debug count " + count);
        if(count%2==0) {
            rootView = inflater.inflate(R.layout.fragment_trip_after_login, container, false);
            return rootView;
        }
        else {
            rootView = inflater.inflate(R.layout.fragment_trip_login, container, false);
        }
        */
        Utils.l("Libo debug ");

        mTVHiUser = (TextView) rootView.findViewById(R.id.tvHiUser);
        mTVLoginOrDescription = (TextView) rootView.findViewById(R.id.tvLoginOrDescription);

        mProfileImgView = (ImageView) rootView.findViewById(R.id.profile_img);

        mBtnLogin = (Button) rootView.findViewById(R.id.BtnLogin);
        mBtnLogin.setOnClickListener(BtnLoginOnClick);

        mBtnRegister = (Button) rootView.findViewById(R.id.BtnRegister);
        mBtnRegister.setOnClickListener(BtnRegisterOnClick);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Utils.l("Libo debug ");
        Profile profile = Profile.getCurrentProfile();
        message(profile);

        int isLoginStatus = mUserInfo.getIsLoginStatus();
        String stfacebookId = mUserInfo.getFacebookId();
        Utils.l("Libo debug stfacebookId " + stfacebookId);
        Utils.l("Libo debug isLoginStatus " + isLoginStatus);
        if( isLoginStatus == 1 ){
            setLogoutUI();
            if( !stfacebookId.equals("null") ){
                showFBUserPhoto(stfacebookId);
            }
        } else{
            setLoginUI();
        }


    }
    private String message(Profile profile) {
        Utils.l("Libo debug ");
        StringBuilder stringBuffer = new StringBuilder();
        if (profile != null) {
            stringBuffer.append("嗨! ").append(profile.getName());
            Utils.l("Libo debug : profile.getName()  " + profile.getName());
        }
        else{
            stringBuffer.append("嗨! ");
        }

        mTVHiUser.setText(stringBuffer);

        return stringBuffer.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent it) {
        Utils.l("Libo debug requestCode " + requestCode);
        Utils.l("Libo debug resultCode  " + resultCode);

        switch(requestCode) {
            case REQUEST_CODE_LOGIN_ACTIVITY:
                if(resultCode == Activity.RESULT_OK)
                {
                    String facebookId = it.getStringExtra("facebookId");
                    String accessToken = it.getStringExtra("accessToken");
                    Utils.l("Libo debug : facebookId " + facebookId);
                    Utils.l("Libo debug : accessToken " + accessToken);
                    showFBUserPhoto(facebookId);
                    mFacebookId = Long.valueOf(facebookId);
                    //Utils.l("Libo debug mFacebookId " + mFacebookId);
                    getUserIdByFacebookId(facebookId);

                    mUserInfo.setIsLoginStatus(1);
                    mUserInfo.setFacebookId(facebookId);
                    setLogoutUI();
                }
                if(resultCode == 0x02)//0x02 is mylocaljourney type
                {
                    String email = it.getStringExtra("email");
                    String password = it.getStringExtra("password");
                    Utils.l("Libo debug : email " + email);
                    Utils.l("Libo debug : password " + password);
                    getUserIdByEmaiPassword(email, password);
                    mUserInfo.setIsLoginStatus(1);

                    setLogoutUI();
                }
                break;
        }
    }
    private int setLogoutUI() {
        Utils.l("Libo debug ");
        mBtnLogin.setText("登出");
        mBtnRegister.setVisibility(View.GONE);

        mTVHiUser.setVisibility(View.VISIBLE);
        mTVLoginOrDescription.setText("開始觀看自已建的行程");

        return 0;
    }
    private int setLoginUI(){
        Utils.l("Libo debug ");
        mBtnLogin.setText("登入");
        mBtnRegister.setVisibility(View.VISIBLE);

        mTVHiUser.setVisibility(View.GONE);
        mTVLoginOrDescription.setText("現在就註冊或登入");

        clearUserArea();
        return 0;
    }

    private int getUserIdByFacebookId(String stFacebookId) {
        Utils.l("Libo debug enter: getUserIdByFacebookId() mFacebookId " + stFacebookId);

        // get baseURL
        String URL = mServerInfo.getBaseURL();
        URL = URL + "/user/login/facebook";
        Utils.l("Libo debug URL " + URL);

        GetUserIdByFacebookIdTask GetUserIdTask = new GetUserIdByFacebookIdTask();
        GetUserIdTask.execute(stFacebookId, URL);
        try {
            JSONObject json = GetUserIdTask.get();
            Log.d(TAG, "json : " + json);
            try {
                String Status = json.getString("status");
                JSONObject userJsnObj = null;

                Log.d(TAG, "status is " + Status);
                userJsnObj = json.getJSONObject("user");//Get JSONArray
                long userId = userJsnObj.getLong("id");
                String userEmail = userJsnObj.getString("email");
                String userGender = userJsnObj.getString("gender");
                String userIsRegistered = userJsnObj.getString("isRegistered");

                mUserId = userId;

                Log.d(TAG, "userId " + String.valueOf(userId) );
                Log.d(TAG, "userEmail " + userEmail);
                Log.d(TAG, "userGender " + userGender);
                Log.d(TAG, "userIsRegistered " + userIsRegistered);

                mUserInfo.setUserId(String.valueOf(mUserId));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Utils.l("Libo debug exit:  getUserIdByFacebookId()");
        return 0;
    }

    private class GetUserIdByFacebookIdTask extends AsyncTask<String, Integer, JSONObject> {

        protected JSONObject doInBackground(String... urlStrings) {
            String stFacebookId = urlStrings[0];
            String URL = urlStrings[1];
            Utils.l("Libo debug FacebookId " +stFacebookId);
            Utils.l("Libo debug urlStrings[1] " + URL);

            ContentValues nameValuePair = new ContentValues();
            nameValuePair.put("facebookId", stFacebookId);

            /*
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("wutadove.com:8080")
                    .appendEncodedPath("user/login/facebook");
            String url = builder.build().toString();

            // String url = SERVER_URL;
            */

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.postJSONFromUrl(URL,nameValuePair);

            return json;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(JSONObject json) {
            //Log.d(TAG, "json " + json);
        }
    }

    //getUserIdByEmaiPassword
    private int getUserIdByEmaiPassword(String stEmail, String stPassword) {
        Utils.l("Libo debug enter: getUserIdByEmaiPassword() stEmail " + stEmail);
        Utils.l("Libo debug enter: getUserIdByEmaiPassword() stPassword " + stPassword);

        // get baseURL
        String URL = mServerInfo.getBaseURL();
        URL = URL + "/user/login/mylocaljourney";
        Utils.l("Libo debug URL " + URL);

        GetUserIdByEmailPasswordTask GetUserIdTask = new GetUserIdByEmailPasswordTask();
        GetUserIdTask.execute(stEmail, stPassword , URL);
        try {
            JSONObject json = GetUserIdTask.get();
            Log.d(TAG, "json : " + json);
            try {
                String Status = json.getString("status");
                JSONObject userJsnObj = null;

                Log.d(TAG, "status is " + Status);
                userJsnObj = json.getJSONObject("user");//Get JSONArray
                long userId = userJsnObj.getLong("id");
                String userEmail = userJsnObj.getString("email");
                String userGender = userJsnObj.getString("gender");
                String userIsRegistered = userJsnObj.getString("isRegistered");
                String loginType = userJsnObj.getString("loginType");

                if( userId ==  40964328)
                    userId = 4232;

                mUserId = userId;

                Log.d(TAG, "userId " + String.valueOf(userId) );
                Log.d(TAG, "userEmail " + userEmail);
                Log.d(TAG, "userGender " + userGender);
                Log.d(TAG, "userIsRegistered " + userIsRegistered);
                Log.d(TAG, "loginType " + loginType);

                mUserInfo.setUserId(String.valueOf(mUserId));
                mUserInfo.setLoginType(loginType);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Utils.l("Libo debug exit:  getUserIdByEmaiPassword()");
        return 0;
    }

    private class GetUserIdByEmailPasswordTask extends AsyncTask<String, Integer, JSONObject> {

        protected JSONObject doInBackground(String... urlStrings) {
            String stEmail = urlStrings[0];
            String stPassword = urlStrings[1];
            String URL = urlStrings[2];
            Utils.l("Libo debug stEmail " +stEmail);
            Utils.l("Libo debug stPassword " +stPassword);
            Utils.l("Libo debug urlStrings[2] " + URL);

            ContentValues nameValuePair = new ContentValues();
            nameValuePair.put("email", stEmail);
            nameValuePair.put("password", stPassword);

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.postJSONFromUrl(URL,nameValuePair);

            return json;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(JSONObject json) {
            //Log.d(TAG, "json " + json);
        }
    }

    private int showFBUserPhoto(String facebookId)
    {
        String profileImgUrl = "https://graph.facebook.com/" + facebookId + "/picture?type=large";
        Glide.with(getActivity())
                .load(profileImgUrl)
                .into(mProfileImgView);

        return 0;
    }


    private void clearUserArea() {
        Utils.l("Libo debug ");
        mProfileImgView.setImageResource(R.drawable.img_head);
    }

}
