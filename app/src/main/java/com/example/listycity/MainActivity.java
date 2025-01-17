package com.example.listycity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnTouchListener {

    ListView cityList;
    ArrayAdapter<String> cityAdapter;
    ArrayList<String> dataList;
    Button addCity, removeCity,confirm;
    Group cityInputGroup;
    EditText cityText;
    Boolean cityIsSelected;
    String selectedCityString;
    View selectedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityList = findViewById(R.id.city_list);
        addCity = findViewById(R.id.add_button);
        removeCity = findViewById(R.id.remove_button);
        confirm = findViewById(R.id.confirm_city);
        cityInputGroup = findViewById(R.id.input_group);
        cityText = findViewById(R.id.city_edit_text);
        cityIsSelected = Boolean.FALSE;

        addCity.setOnClickListener(this);
        removeCity.setOnClickListener(this);
        confirm.setOnClickListener(this);
        cityList.setOnItemClickListener(this);
        cityList.setOnTouchListener(this);

        dataList = new ArrayList<>();

        cityAdapter = new ArrayAdapter<>(this, R.layout.content, dataList);
        cityList.setAdapter(cityAdapter);

    }
    /*
    Not copied 1 to 1 but heavily influenced my onClick format case switch
    https://stackoverflow.com/questions/76430646/constant-expression-required-when-trying-to-create-a-switch-case-block
     */
    @Override
    public void onClick(View v) {
        if (cityIsSelected == Boolean.TRUE && v.getId() != R.id.remove_button){
            selectedView.setBackgroundColor(Color.WHITE);
            cityIsSelected = Boolean.FALSE;
            selectedCityString = "";
        }

        switch (v.getId()) {
            case R.id.add_button:
                cityInputGroup.setVisibility(View.VISIBLE);

                break;
            case R.id.remove_button:
                if (cityIsSelected == Boolean.TRUE){
                    selectedView.setBackgroundColor(Color.WHITE);
                    dataList.remove(selectedCityString);
                    cityAdapter.notifyDataSetChanged();
                    cityIsSelected = Boolean.FALSE;
                    selectedCityString = "";
                }

                break;
            case R.id.confirm_city:
                cityInputGroup.setVisibility(View.GONE);
                String text = cityText.getText().toString();
                if (!text.isEmpty()) {
                    dataList.add(text);
                    cityAdapter.notifyDataSetChanged();
                }
                cityText.getText().clear();
                /*
                Taken: on Jan 16 2025
                Taken by: Noah Carter
                From: Nick Campion
                Link: https://stackoverflow.com/questions/4841228/after-type-in-edittext-how-to-make-keyboard-disappear
                 */
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(cityText.getWindowToken(), 0);
                }
                break;
            default:
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (cityIsSelected == Boolean.TRUE) {
            selectedCityString = "";
            selectedView.setBackgroundColor(Color.WHITE);
            cityIsSelected = Boolean.FALSE;
            return;
        }
        if (cityIsSelected == Boolean.FALSE) {
            selectedCityString = dataList.get(position);
            view.setBackgroundColor(Color.GRAY);
            cityIsSelected = Boolean.TRUE;
            selectedView = view;
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Check if the user clicked on an empty space
            if (cityList.pointToPosition((int) event.getX(), (int) event.getY()) == AdapterView.INVALID_POSITION) {
                if(cityIsSelected == Boolean.TRUE) {
                    selectedCityString = "";
                    selectedView.setBackgroundColor(Color.WHITE);
                    cityIsSelected = Boolean.FALSE;
                    return false;
                }
            }
        }
        return false;
    }
}