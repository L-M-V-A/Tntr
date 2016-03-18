package com.freakingoutjunior.tntr;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckLaws extends AppCompatActivity {
    DatabaseAdapter mDbHelper;
    Spinner spinner;
    TextView shield, front, back, rear, myTint;
    String selected_state;
    Cursor mCur;
    Float tint;
    float limit;
    String roundedTint;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        List<String> lst;
        int pos = 0;
        if (bundle != null) {
            selected_state = bundle.getString("state");
            tint = Float.parseFloat(bundle.getString("tint"));
            roundedTint = String.format("%.2f", tint);
            mDbHelper = new DatabaseAdapter(getApplicationContext());
            mDbHelper.createDatabase();
            mDbHelper.open();
            mCur = mDbHelper.getStateData(selected_state);
            lst = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.states_array)));
            pos = lst.indexOf(selected_state);
        }
        lst = null;
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setSelection(pos);
        shield = (TextView) findViewById(R.id.sheildLabel);
        front = (TextView) findViewById(R.id.frontLabel);
        back = (TextView) findViewById(R.id.backLabel);
        rear = (TextView) findViewById(R.id.rearLabel);
        myTint = (TextView)findViewById(R.id.yourTint);
        mCur = mDbHelper.getData();
        setTexts();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                selected_state = (String) spinner.getItemAtPosition(pos);
                mCur = mDbHelper.getStateData(selected_state);
                setTexts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void setTexts(){
        shield.setText(mCur.getString(1) + '%');
        limit = mCur.getFloat(1);
        checkLegal(shield);


        front.setText(mCur.getString(2) + '%');
        limit = 100 - mCur.getFloat(2);
        checkLegal(front);


        back.setText(String.valueOf(100 - mCur.getInt(3)) + '%');
        limit = 100 - mCur.getFloat(3);
        checkLegal(back);

        rear.setText(String.valueOf(100 - mCur.getInt(4)) + '%');
        limit = 100 - mCur.getFloat(4);
        checkLegal(rear);


        myTint.setText("Your Tint: " + roundedTint + '%');
    }

    public void checkLegal(TextView check){
        boolean any = false;
        check.setTextColor(Color.BLACK);
        if(check.getText().equals("-1%")) {
            check.setText("Illegal");
        }
        else if(check.getText().equals("0%")) {
            check.setText("Any");
            any = true;
        }
        if(tint.floatValue() > limit && !any)
            check.setTextColor(Color.RED);
        else
            check.setTextColor(Color.GREEN);
    }

    public void restart(View v) {
        Intent intent = new Intent(this, WindowSelector.class);
        startActivity(intent);
    }
}