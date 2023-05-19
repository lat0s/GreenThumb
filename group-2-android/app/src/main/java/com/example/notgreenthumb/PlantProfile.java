package com.example.notgreenthumb;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notgreenthumb.mqtt.MqttDataUpdateListener;
import com.example.notgreenthumb.mqtt.MqttHelper;

public class PlantProfile extends AppCompatActivity {
    private TextView yourPlant;
    private ImageView plantPicture;

    private ConstraintLayout layoutProfile;
    private Button back;

    private static final int REQUEST_CODE_DASHBOARD = 1;

    private MqttHelper mqttHelper;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_profile);

        layoutProfile = findViewById(R.id.layoutProfile);

        yourPlant = findViewById(R.id.yourplant);
        plantPicture = findViewById(R.id.plantPicture);

        back = findViewById(R.id.backButton);
        back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToDash();
            }
        });

        mqttHelper = new MqttHelper(this, new MqttDataUpdateListener() {
            @Override
            public void onMqttDataUpdate(String parameter, String value) {
                updatePlantData(parameter, value);
            }
        });

        // Get the Intent that started this activity and extract the plant details
        Intent intent = getIntent();
        String plantName = intent.getStringExtra("plantName");
        int imageIndex = intent.getIntExtra("plantImageIndex", 1);

        // Now set these values to your TextView and ImageView
        yourPlant.setText(plantName);
        String imageName = "plant" + imageIndex;
        int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
        plantPicture.setImageResource(resID);

        setupBackgroundAnimation();
    }

    private void backToDash() {
        Intent intent = new Intent(PlantProfile.this, Dashboard.class);
        startActivityForResult(intent, REQUEST_CODE_DASHBOARD);
    }

    private void setupBackgroundAnimation() {
        AnimationDrawable animationDrawable = (AnimationDrawable) layoutProfile.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
    }
    private void updatePlantData(String parameter, String value) {
        switch (parameter) {
            case "temperature":
                TextView temperatureValue = findViewById(R.id.temperature_value);
                temperatureValue.setText(value + " °C");
                break;
            case "humidity":
                TextView humidityValue = findViewById(R.id.humidity_value);
                humidityValue.setText(value + " %RH");
                break;
            case "light":
                TextView lightValue = findViewById(R.id.light_value);
                lightValue.setText(value + " Lux");
                break;
            case "soil_moisture":
                TextView moistureValue = findViewById(R.id.moisture_value);
                moistureValue.setText(value + " %");
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DASHBOARD && resultCode == RESULT_OK) {
            mqttHelper.disconnect();
        }
    }

}
