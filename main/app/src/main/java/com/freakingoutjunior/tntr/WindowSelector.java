package com.freakingoutjunior.tntr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class WindowSelector extends AppCompatActivity {
    RadioGroup choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_selector);

    }
    public void pickWhich(View v) {
        Log.e("Window Selector", "Test");

            Intent intent = new Intent(this, OutdoorAmbientCheck.class);
            Bundle bundle = new Bundle();
            bundle.putString("state", "Alabama");
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

