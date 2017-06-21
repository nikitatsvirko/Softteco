package com.application.nikita.softteco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.application.nikita.softteco.R;
import com.application.nikita.softteco.adapters.PostsAdapter;
import com.application.nikita.softteco.entities.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView mImage;
    private Button mSaveButton;
    private Animation animation;
    private ListView mPostsList;
    private PostsAdapter mPostsAdapter;
    private ArrayList<Post> mPosts;

    private JsonArrayRequest mJsonArrayRequest;
    private RequestQueue mRequestQueue;
    private final String REQUEST_TAG = "Volley Request";

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
        }

        mPostsAdapter = new PostsAdapter(getApplicationContext(), mPosts);
        mPostsList.setAdapter(mPostsAdapter);
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

        mPostsList = (ListView) findViewById(R.id.posts_list);
        mPostsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                onPostItemClicked(position);
            }
        });
    }

    private void setUpAnimation() {
        animation = AnimationUtils.loadAnimation(this, R.anim.myanimation);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mSaveButton.setVisibility(View.INVISIBLE);
                mPostsList.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSaveButton.setVisibility(View.VISIBLE);
                mPostsList.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void onSaveButtonClicked() {
        Log.w(TAG, "Before Logcat save");
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            process = Runtime.getRuntime().exec("logcat -f " + "/storage/emulated/0/"+"Logging.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onPostItemClicked(int position) {
        Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
        intent.putExtra("post_ID", mPosts.get(position).getId());
        intent.putExtra("user_ID", mPosts.get(position).getUserId());
        startActivity(intent);
    }
}