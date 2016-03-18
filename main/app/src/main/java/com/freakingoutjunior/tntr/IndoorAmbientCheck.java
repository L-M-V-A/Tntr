package com.freakingoutjunior.tntr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class IndoorAmbientCheck extends Activity implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor mLight;
    private TextView countDown;
    private String light;
    private String outside_light;
    private String state;
    private String TAG = "INDOOR";
    private float tint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_indoor_ambient_check);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Log.i(TAG, bundle.getString("state"));
            Log.i(TAG, bundle.getString("sun"));
            outside_light = bundle.getString("sun");
            state = bundle.getString("state");
        } else {
            Log.i(TAG, "Nothing to see, here.");
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        countDown = (TextView) findViewById(R.id.countdown);
    }

    @Override
    protected void onResume() {
        // register listener
        super.onResume();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void checkLight(View v) {
        Log.i(TAG, "Checking light");
        new CountDownTimer(5500, 1000) {

            public void onTick(long millisUntilFinished) {
                countDown.setText(String.valueOf((millisUntilFinished/1000)));
                tint += Float.valueOf(light);
                Log.i("tint lux", String.valueOf(tint));
            }

            public void onFinish() {
                countDown.setText("0");
                tint /= 5;
                Intent intent = new Intent(getApplicationContext(), CheckLaws.class);
                Bundle bundle = new Bundle();
                bundle.putString("state", state);
                Log.i("tint lux", checkDelta(tint));
                bundle.putString("tint", checkDelta(tint));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }.start();

    }

    private String checkDelta(float f) {
        float inside = f;
        float outside = Float.parseFloat(outside_light);
        float res = (outside-inside)/outside;
        Log.i("Inside: ", String.valueOf(inside));
        Log.i("Outside: ", String.valueOf(outside));
        res *= 100.0;
        Log.i("Res: ", String.valueOf(res));
        return String.valueOf(res);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Print out light data???
        float lux = event.values[0];
        light = String.valueOf(lux);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
