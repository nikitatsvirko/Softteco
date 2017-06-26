package com.application.nikita.softteco.activities;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.application.nikita.softteco.R;
import com.application.nikita.softteco.adapters.GridFragmentPageAdapter;
import com.application.nikita.softteco.entities.GridItem;
import com.application.nikita.softteco.entities.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView mImage;
    private Button mSaveButton;
    private Animation animation;
    private ArrayList<Post> mPosts;
    private ArrayList<GridItem> mPostsList = new ArrayList<>();
    private LinearLayout mDotsIndicator;
    private ViewPager mViewPager;
    private ImageView[] mDots;
    private GridFragmentPageAdapter mGridFragmentPageAdapter;

    private JsonArrayRequest mJsonArrayRequest;
    private RequestQueue mRequestQueue;
    private final String REQUEST_TAG = "Volley Request";
    private int mDotsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUIComponents();

        loadPosts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImage.startAnimation(animation);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(REQUEST_TAG);
        }
    }

    private void loadPosts() {
        final String requestURL = "http://jsonplaceholder.typicode.com/posts";

        mRequestQueue = Volley.newRequestQueue(this);

        mJsonArrayRequest = new JsonArrayRequest(requestURL,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            putDataToAdapter(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(REQUEST_TAG, "Error: " + error.getMessage());
                    }
        });

        mJsonArrayRequest.setTag(REQUEST_TAG);
        mRequestQueue.add(mJsonArrayRequest);
    }

    private void putDataToAdapter(JSONArray response) throws JSONException {
        mPosts = new ArrayList<>();

        for (int i = 0; i < response.length(); i++) {
            JSONObject object = response.getJSONObject(i);
            mPosts.add(new Post(object.getInt("userId"),
                    object.getInt("id"),
                    object.getString("title"),
                    object.getString("body")));
            GridItem item = new GridItem();
            item.setId(String.valueOf(object.getInt("id")));
            item.setUserId(String.valueOf(object.getInt("userId")));
            item.setTitle(object.getString("title"));
            mPostsList.add(item);
        }

        mGridFragmentPageAdapter = new GridFragmentPageAdapter(getSupportFragmentManager(), mPostsList, getResources());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mGridFragmentPageAdapter);

        mDotsIndicator = (LinearLayout) findViewById(R.id.scroll_indicator);
        mDotsCount = mGridFragmentPageAdapter.getCount();
        mDots = new ImageView[mDotsCount];
        for (int i = 0; i < mDotsCount; i++) {
            mDots[i] = new ImageView(this);
            mDots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            mDotsIndicator.addView(mDots[i], params);
        }
        mDots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mDotsCount; i++) {
                    mDots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }
                mDots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUIComponents() {
        mImage = (ImageView) findViewById(R.id.imageView);
        setUpAnimation();

        mSaveButton = (Button) findViewById(R.id.saveLogcatButton);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    private void setUpAnimation() {
        animation = AnimationUtils.loadAnimation(this, R.anim.myanimation);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mSaveButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSaveButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void onSaveButtonClicked() {
        Log.w(TAG, "Before Logcat save");
        if ( isExternalStorageWritable() ) {

            File appDirectory = new File( Environment.getExternalStorageDirectory() + "/SofttecoApp" );
            File logDirectory = new File( appDirectory + "/log" );
            File logFile = new File( logDirectory, "logcat" + System.currentTimeMillis() + ".txt" );

            if ( !appDirectory.exists() ) {
                appDirectory.mkdir();
            }

            if ( !logDirectory.exists() ) {
                logDirectory.mkdir();
            }

            try {
                Process process = Runtime.getRuntime().exec( "logcat -c");
                process = Runtime.getRuntime().exec( "logcat -f " + logFile + " *:S MainActivity:D UserInfoActivity:D");
            } catch ( IOException e ) {
                e.printStackTrace();
            }

        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals( state );
    }
}