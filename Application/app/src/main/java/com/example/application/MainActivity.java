package com.example.application;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

// checkpoint: popular restaurants page is working with db, need to fix connection to myRestaurants page

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    ListView listView;
    FloatingActionButton fab;
    ArrayList<String> checkedPositions;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkedPositions = new ArrayList<>();
        mDatabaseHelper = new DatabaseHelper(this);

        listView = findViewById(R.id.listView);
        fab = findViewById(R.id.fab);
//        checkBox = findViewById(R.id.checkBox);

        fab.setOnClickListener(this);
//        checkBox.setOnClickListener(this);

//        Bundle bundle = getIntent().getExtras();

        Log.e(TAG, "db size: " + mDatabaseHelper.size());
        if (mDatabaseHelper.size() == 0) {
            initializeDatabase();
//            rList = new ArrayList<>();
//            myRList = new ArrayList<>();
        }

        populateListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor data = mDatabaseHelper.getData();

                data.moveToNext();
                while (data.getInt(14) != 0 && data.moveToNext()) { }
                int i = 0;
                while (i < position) {
                    if (data.getInt(14) == 0) { // only increment if item is popularRestaurants
                        i++;
                    }
                    data.moveToNext();
                }
                while (data.getInt(14) != 0 && data.moveToNext()) { }

                String business_id = data.getString(1);
                print("dbClicked #" + position + ": " + data.getString(2) + ", " + business_id);
                // if clickedItem was already checked, decrement checkedCount and "switch boolean"
                int x = checkedPositions.indexOf(business_id);
                if (x > -1) { // if item is checked
                    checkedPositions.remove(x);
                } else {
                    checkedPositions.add(business_id);
                }
                Log.e(TAG, "checkedPositions: " + checkedPositions.toString());
            }
        });

//        ImageView photo = findViewById(R.id.photo);
//        int imageResource = getResources().getIdentifier("@drawable/mcd_logo",
//                null, this.getPackageName());
//        photo.setImageResource(imageResource);


    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the List View.");

        // get data and append to the list
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> nameList = new ArrayList<>();
        Log.e(TAG, "db size: " + mDatabaseHelper.size() + ", popRnameList size: " + nameList.size());
//        Log.e(TAG, "Reset: " + mDatabaseHelper.resetAllData());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        while (data.moveToNext()) {
            stringBuilder.append(data.getInt(14));
            stringBuilder.append(", ");
        }
        stringBuilder.append("]");
        Log.i(TAG, stringBuilder.toString());

        data.moveToFirst();
        data.moveToPrevious();
        while (data.moveToNext()) {
            // get the value from the database in COL2 and add it to the nameList
            if (data.getInt(14) == 0) { // if item is NOT in MyRestaurants
                nameList.add(data.getString(2));
            }
        }

        // create the list adapter and set the adapter
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        ListAdapter listAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_activated_1, nameList);
        listView.setAdapter(listAdapter);
        Log.e(TAG, "db size: " + mDatabaseHelper.size() + ", popRNameList size: " + nameList.size());
    }

    private ArrayList<Restaurant> initializeDatabase() {
        ArrayList<Restaurant> rList = new ArrayList<>();
        try {
            InputStream input = getAssets().open("sample_data.json");
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            String jsonStr = new String(buffer, "UTF-8");
            print(jsonStr);
            JSONArray businesses = new JSONArray(jsonStr);

//            print("businesses count: " + businesses.length());
            for (int i = 0; i < businesses.length(); i++) {
                JSONObject obj = businesses.getJSONObject(i);
                String business_id = obj.getString("business_id");
                String name = obj.getString("name");
                String address = obj.getString("address");
                String city = obj.getString("city");
                String state = obj.getString("state");
                String postal_code = obj.getString("postal_code");
                String latitude = obj.getString("latitude");
                String longitude = obj.getString("longitude");
                String stars = obj.getString("stars");
                String review_count = obj.getString("review_count");
                String is_open = obj.getString("is_open");
                String categories = obj.getString("categories");
                String hours = obj.getJSONObject("hours").toString();

//                Restaurant restaurant = new Restaurant(business_id, name, address, city, state, postal_code,
//                        latitude, longitude, stars, review_count, is_open, categories, hours);
//                rList.add(restaurant);
//                Log.d(TAG, restaurant.getName());

                this.addData(business_id, name, address, city, state, postal_code, latitude,
                        longitude, stars, review_count, is_open, categories, hours);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "JSON Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(),
//                            "Json parsing error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//            });

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "IO Exception", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return rList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                // if in popularRestaurants, it's not in myRestaurants --> if checked, move to myRestaurants
                for (String business_id : checkedPositions) {
                    this.updateData(1, business_id);
                }

                Intent intent = new Intent(MainActivity.this, MyRestaurantsActivity.class);
                Cursor data = mDatabaseHelper.getData();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[");
                while (data.moveToNext()) {
                    stringBuilder.append(data.getInt(14));
                    stringBuilder.append(", ");
                }
                stringBuilder.append("]");
                Log.i(TAG, stringBuilder.toString());
                startActivity(intent);
                break;
            case R.id.checkBox:
//                checkBox.setSelected(!checkBox.isSelected());
                break;
        }
    }

    public void addData(String business_id, String name, String address, String city, String state,
                        String postal_code, String latitude, String longitude, String stars,
                        String review_count, String is_open, String categories, String hours) {
        boolean successfulInsert = mDatabaseHelper.addData(business_id, name, address, city, state,
                postal_code, latitude, longitude, stars, review_count, is_open, categories, hours);
        print("addData: " + name + ", " + successfulInsert);
    }

    public void updateData(int newVal, String business_id) {
        int numRowsUpdated = mDatabaseHelper.updateData(newVal, business_id);
        print("updateData: " + business_id + " to " + newVal + ", " + numRowsUpdated);
    }

    public void print(String message) {
        Log.d(TAG, message);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
