package com.example.application;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static android.widget.Toast.makeText;

public class MyRestaurantsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MyRestaurantActivity";
    ListView listView2;
    FloatingActionButton fab2;
    ArrayList<String> checkedPositions;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_restaurants);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkedPositions = new ArrayList<>();
        mDatabaseHelper = new DatabaseHelper(this);

        listView2 = findViewById(R.id.listView2);
        fab2 = findViewById(R.id.fab2);
//        checkBox = findViewById(R.id.checkBox);

        fab2.setOnClickListener(this);
//        checkBox.setOnClickListener(this);

//        Bundle bundle = getIntent().getExtras();

        populateListView();
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor data = mDatabaseHelper.getData();

                data.moveToNext();
                while (data.getInt(14) != 1 && data.moveToNext()) { }
                int i = 0;
                while (i < position) {
                    if (data.getInt(14) == 1) { // only increment if item is myRestaurants
                        i++;
                    }
                    data.moveToNext();
                }
                while (data.getInt(14) != 1 && data.moveToNext()) { }

                String business_id = data.getString(1);
                print("dbClicked #" + position + ": " + data.getString(2) + ", " + business_id);
                // if clickedItem was already checked, decrement checkedCount and "switch boolean"
                int x = checkedPositions.indexOf(business_id);
                if (x > -1) { // if item is already checked
                    checkedPositions.remove(x);
                } else {
                    checkedPositions.add(business_id);
                }
                Log.e(TAG, "checkedPositions: " + checkedPositions.toString());
            }
        });
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the List View.");

        // get data and append to the list
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> nameList = new ArrayList<>();
        Log.e(TAG, "db size: " + mDatabaseHelper.size() + ", myRnameList size: " + nameList.size());
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
            if (data.getInt(14) == 1) { // if item is in MyRestaurants
                nameList.add(data.getString(2));
            }
        }

        // create the list adapter and set the adapter
        listView2.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        ListAdapter listAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_activated_1, nameList);
        listView2.setAdapter(listAdapter);
        Log.e(TAG, "db size: " + mDatabaseHelper.size() + ", myRNameList size: " + nameList.size());
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
            case R.id.fab2:
                for (String business_id : checkedPositions) {
                    this.updateData(0, business_id);
                }
                Intent intent = new Intent(MyRestaurantsActivity.this, MainActivity.class);
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

    public void updateData(int newVal, String business_id) {
        int numRowsUpdated = mDatabaseHelper.updateData(newVal, business_id);
        print("updateData: " + business_id + " to " + newVal + ", " + numRowsUpdated);
    }

    public void print(String message) {
        Log.e(TAG, message);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
