package com.application.nikita.softteco.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.application.nikita.softteco.R;
import com.application.nikita.softteco.database.contracts.CompanyContract.CompanyEntry;
import com.application.nikita.softteco.database.DatabaseHelper;
import com.application.nikita.softteco.entities.Address;
import com.application.nikita.softteco.entities.Company;
import com.application.nikita.softteco.entities.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.application.nikita.softteco.database.contracts.AddressContract.AddressEntry;
import static com.application.nikita.softteco.database.contracts.UserContract.UserEntry;

public class UserInfoActivity extends AppCompatActivity {

    private String TAG = UserInfoActivity.class.getSimpleName();
    private TextView mPostID;
    private TextView mUserName;
    private TextView mUserNickname;
    private TextView mUserPhone;
    private TextView mUserEmail;
    private TextView mUserAddress;
    private Button mSaveUser;

    private JsonObjectRequest mJsonObjectRequest;
    private RequestQueue mRequestQueue;
    private final String REQUEST_TAG = "Volley Request";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase dbWrite, dbRead;

    private int userID;
    private int postID;
    private User mUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        userID = getIntent().getIntExtra("user_ID", 0);
        postID = getIntent().getIntExtra("post_ID", 0);

        loadUser(userID);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(REQUEST_TAG);
        }
    }

    private void setUIComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contact #" + userID);

        TextView mPostID = (TextView) findViewById(R.id.postId);
        mPostID.setText(String.valueOf(postID));
        TextView mUserName = (TextView) findViewById(R.id.userName);
        mUserName.setText(mUser.getName());
        TextView mUserNickname = (TextView) findViewById(R.id.userNickname);
        mUserNickname.setText(mUser.getUsername());
        final TextView mUserPhone = (TextView) findViewById(R.id.userPhone);
        mUserPhone.setText(mUser.getPhone());
        TextView mUserEmail = (TextView) findViewById(R.id.userEmail);
        mUserEmail.setText(mUser.getEmail());
        TextView mUserAddress = (TextView) findViewById(R.id.userAddress);
        mUserAddress.setText(mUser.getAddress().getCity());
        mUserAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddressClicked();
            }
        });
        Button mSaveUser = (Button) findViewById(R.id.saveUser);
        mSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveUserClicked();
            }
        });
    }

    private void loadUser(int id) {
        final String requestURL = "http://jsonplaceholder.typicode.com/users/" + String.valueOf(id);
        mRequestQueue = Volley.newRequestQueue(this);

        mJsonObjectRequest = new JsonObjectRequest(requestURL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "User Name is " + response.toString());
                            mUser.setId(response.getInt("id"));
                            mUser.setName(response.getString("name"));
                            mUser.setUsername(response.getString("username"));
                            mUser.setEmail(response.getString("email"));
                            mUser.setAddress(getAddressFromJSON(response.getJSONObject("address")));
                            mUser.setPhone(response.getString("phone"));
                            mUser.setWebsite(response.getString("website"));
                            mUser.setCompany(getCompanyFromJSON(response.getJSONObject("company")));

                            setUIComponents();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        mJsonObjectRequest.setTag(REQUEST_TAG);
        mRequestQueue.add(mJsonObjectRequest);
    }

    private Address getAddressFromJSON(JSONObject response) throws JSONException {
        Address address = new Address();

        address.setStreet(response.getString("street"));
        address.setSuite(response.getString("suite"));
        address.setCity(response.getString("city"));
        address.setZipCode(response.getString("zipcode"));
        address.setLat(response.getJSONObject("geo").getDouble("lat"));
        address.setLng(response.getJSONObject("geo").getDouble("lng"));

        return address;
    }

    private Company getCompanyFromJSON(JSONObject response) throws JSONException {
        Company company = new Company();

        company.setName(response.getString("name"));
        company.setCatchPhrase(response.getString("catchPhrase"));
        company.setBS(response.getString("bs"));

        return company;
    }

    private void onAddressClicked() {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + mUser.getAddress().getLat() + ","
                + mUser.getAddress().getLng() + "(label)");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void onSaveUserClicked() {
        mDbHelper = new DatabaseHelper(getApplicationContext());
        dbWrite = mDbHelper.getWritableDatabase();
        ContentValues addressValues = new ContentValues();
        ContentValues companyValues = new ContentValues();
        ContentValues userValues = new ContentValues();
        long addressID, companyID, userID;

        if (!isUserExists()) {
            addressValues.put(AddressEntry.COLUMN_NAME_ADDRESS_STREET, mUser.getAddress().getStreet());
            addressValues.put(AddressEntry.COLUMN_NAME_ADDRESS_SUITE, mUser.getAddress().getSuite());
            addressValues.put(AddressEntry.COLUMN_NAME_ADDRESS_CITY, mUser.getAddress().getCity());
            addressValues.put(AddressEntry.COLUMN_NAME_ADDRESS_ZIPCODE, mUser.getAddress().getZipCode());
            addressValues.put(AddressEntry.COLUMN_NAME_ADDRESS_LATITUDE, mUser.getAddress().getLat());
            addressValues.put(AddressEntry.COLUMN_NAME_ADDRESS_LONGITUDE, mUser.getAddress().getLng());
            addressID = dbWrite.insert(AddressEntry.TABLE_NAME, null, addressValues);
            Log.d(TAG, addressID + " row in " + AddressEntry.TABLE_NAME + " table added");

            companyValues.put(CompanyEntry.COLUMN_NAME_COMPANY_NAME, mUser.getCompany().getName());
            companyValues.put(CompanyEntry.COLUMN_NAME_COMPANY_CATCH_PHRASE, mUser.getCompany().getCatchPhrase());
            companyValues.put(CompanyEntry.COLUMN_NAME_COMPANY_BS, mUser.getCompany().getBS());
            companyID = dbWrite.insert(CompanyEntry.TABLE_NAME, null, companyValues);
            Log.d(TAG, companyID + " row in " + CompanyEntry.TABLE_NAME + " table added");

            userValues.put(UserEntry.COLUMN_NAME_USER_ID, mUser.getId());
            userValues.put(UserEntry.COLUMN_NAME_USER_NAME, mUser.getName());
            userValues.put(UserEntry.COLUMN_NAME_USER_USERNAME, mUser.getUsername());
            userValues.put(UserEntry.COLUMN_NAME_USER_EMAIL, mUser.getEmail());
            userValues.put(UserEntry.COLUMN_NAME_USER_ADDRESS, addressID);
            userValues.put(UserEntry.COLUMN_NAME_USER_PHONE, mUser.getPhone());
            userValues.put(UserEntry.COLUMN_NAME_USER_WEBSITE, mUser.getWebsite());
            userValues.put(UserEntry.COLUMN_NAME_USER_COMPANY, companyID);
            userID = dbWrite.insert(UserEntry.TABLE_NAME, null, userValues);
            Log.d(TAG, userID + " row in " + UserEntry.TABLE_NAME + " table added");
        } else {
            Log.d(TAG, "USER is exists");
        }

        dbWrite.close();
        mDbHelper.close();
    }

    private boolean isUserExists() {
        boolean isExists = false;
        dbRead = mDbHelper.getReadableDatabase();
        Cursor c;
        String[] columns = {UserEntry.COLUMN_NAME_USER_ID};
        List<Integer> usersID = new ArrayList<>();

        c = dbRead.query(UserEntry.TABLE_NAME,
                columns,
                null, null, null, null, null);

        if (c != null) {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    int id = c.getInt(c.getColumnIndex(UserEntry.COLUMN_NAME_USER_ID));
                    usersID.add(id);
                    c.moveToNext();
                }
            }
        }

        if (usersID.size() != 0) {
            for (int id: usersID) {
                if (mUser.getId() == id)
                    isExists = true;
            }
        }

        return isExists;
    }
}
