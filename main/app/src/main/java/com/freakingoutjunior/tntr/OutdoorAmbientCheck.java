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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OutdoorAmbientCheck extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mLight;
    private String light;
    private String TAG = "OUTDOOR";
    private float sun = 0;
    private TextView countDown;
    TextView header;
    TextView footnote;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdoor_ambient_check);
        iv = (ImageView) findViewById(R.id.imageView);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        countDown = (TextView) findViewById(R.id.countdown);

    }
    public void checkSun(View v) {


        new CountDownTimer(5500,1000) {
            public void onTick(long millisUntilFinished) {
                sun += Float.valueOf(light);
                Log.i("Sun Lux", String.valueOf(sun));
                countDown.setText(String.valueOf((millisUntilFinished/1000)));
            }

            public void onFinish() {
                countDown.setText("0");
                sun /= 5;
                Intent intent = new Intent(getApplicationContext(), IndoorAmbientCheck.class);
                Bundle bundle = new Bundle();
                bundle.putString("state", "Alabama");
                String sunstring = String.valueOf(sun);
                bundle.putString("sun", sunstring);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        }.start();

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not sure if this is even needed
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        //Print out light data???
        float lux = event.values[0];
        light = String.valueOf(lux);
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
}
