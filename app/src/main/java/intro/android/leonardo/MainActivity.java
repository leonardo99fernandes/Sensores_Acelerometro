package intro.android.leonardo;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SensorEventListener mSensorListener;
    private View view;
    private long updateTime;
    private boolean color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.textView);
        view.setBackgroundColor(getResources().getColor(R.color.black));

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(mSensor == null){
            finish();
        }

        mSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                calcAcelerometro(event);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        updateTime = System.currentTimeMillis();
    }

    private void calcAcelerometro(SensorEvent event){
        float[] values = event.values;
        float x = values[0], y = values[1], z = values[2];
        long timestamp = System.currentTimeMillis();
        float aceleration = (x*x + y*y + z*z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        if(aceleration >= 2){
            if(timestamp - updateTime < 200) {return;}
            updateTime = timestamp;
            if(color){
                view.setBackgroundColor(getResources().getColor(R.color.black));
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
        color = !color;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorListener);
    }

}