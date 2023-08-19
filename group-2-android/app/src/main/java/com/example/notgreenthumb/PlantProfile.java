package com.example.notgreenthumb;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notgreenthumb.mqtt.MqttDataUpdateListener;
import com.example.notgreenthumb.mqtt.MqttHelper;
import com.example.notgreenthumb.plants.Plant;

public class PlantProfile extends AppCompatActivity {
    private static final int REQUEST_CODE_WATERING = 3;
    private static final int REQUEST_CODE_GRAPHS = 5;
    private TextView yourPlant;
    private ImageView plantPicture;

    private ConstraintLayout layoutProfile;
    private Button back;
    private Button waterButton;
    private Button historyButton;
    private Context context;

    private static final int REQUEST_CODE_DASHBOARD = 2;

    private MqttHelper mqttHelper;
    private Plant plant;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_profile);

        Button graphButton = findViewById(R.id.graphButton);

        graphButton.setOnClickListener(view -> toGraphs());

        waterButton = findViewById(R.id.waterButton);
        historyButton = findViewById(R.id.historyButton);

        waterButton.setOnClickListener(view -> water());
        historyButton.setOnClickListener(view -> toHistory());


        layoutProfile = findViewById(R.id.layoutProfile);

        yourPlant = findViewById(R.id.yourplant);
        plantPicture = findViewById(R.id.plantPicture);

        back = findViewById(R.id.backButton);
        back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));



        back.setOnClickListener(view -> backToDash());

        SharedPreferences prefs = getSharedPreferences("MqttPrefs", MODE_PRIVATE);
        String brokerUrl = prefs.getString("brokerUrl", "tcp://192.168.50.9:1883");
        String clientId = prefs.getString("clientId", "GreenThumb");

            mqttHelper = new MqttHelper(this, new MqttDataUpdateListener() {
                @Override
                public void onMqttDataUpdate(String parameter, String value) {
                    updatePlantData(parameter, value,plant);
            }
        }, brokerUrl, clientId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Wait if the mqttClient is not connected
                    while (!mqttHelper.mqttClient.isConnected()) {
                        Thread.sleep(500);
                    }

                    // If connected, publish the message
                    mqttHelper.publishMessage("welcome/topic", "hello");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Get the Intent that started this activity and extract the plant details
        Intent intent = getIntent();
        plant = (Plant) intent.getSerializableExtra("plant");

        if (plant != null) {
            String plantName = plant.getPlantName();
            int imageIndex = plant.getImageIndex();

            yourPlant.setText(plantName);
            String imageName = "plant" + imageIndex;
            int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
            plantPicture.setImageResource(resID);
        }


        setupBackgroundAnimation();
    }

    private void toGraphs() {
        Intent intent = new Intent(PlantProfile.this,Graphs.class);
        intent.putExtra("plant",plant);
        startActivity(intent);
    }

    private void toHistory() {
        Intent intent = new Intent(PlantProfile.this,Watering.class);
        intent.putExtra("plant",plant);
        startActivityForResult(intent,REQUEST_CODE_WATERING);
    }

    private void water() {
        long currentTime = System.currentTimeMillis();
        plant.addToWateringHistory(currentTime);
        showWateredDialog();
    }
    private void showWateredDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(plant.getPlantName());
        builder.setMessage("Was watered!");
        builder.setPositiveButton("OK", null);
        builder.setIcon(getResources().getDrawable(R.drawable.wateringcan));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void backToDash() {
        Intent intent = new Intent();
        intent.putExtra("plant",plant);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void setupBackgroundAnimation() {
        AnimationDrawable animationDrawable = (AnimationDrawable) layoutProfile.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
    }
    private void updatePlantData(String parameter, String value, Plant plant) {
        switch (parameter) {
            case "temperature":
                TextView temperatureValue = findViewById(R.id.temperature_value);
                    plant.setTemperatureValue(Integer.parseInt(value));
                    temperatureValue.setText(plant.getTemperatureValue() + " °C");
                    break;
                case "humidity":
                    TextView humidityValue = findViewById(R.id.humidity_value);
                    plant.setHumidityValue(Integer.parseInt(value));
                    humidityValue.setText(plant.getHumidityValue() + " %RH");
                    break;
                case "light":
                    TextView lightValue = findViewById(R.id.light_value);
                    plant.setLightValue(Integer.parseInt(value));
                    lightValue.setText(plant.getLightValue() + " Lux");
                    break;
                case "soil_moisture":
                    TextView moistureValue = findViewById(R.id.moisture_value);
                    plant.setMoistureValue(Integer.parseInt(value));
                    moistureValue.setText(plant.getMoistureValue() + " %");
                    break;
            }
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DASHBOARD && resultCode == RESULT_OK) {
            mqttHelper.disconnect();
        }
        else if (requestCode == REQUEST_CODE_WATERING && resultCode == RESULT_OK){
            if (data.hasExtra("plant")) {
                plant = (Plant) data.getSerializableExtra("plant");
            }

        }
        else if (requestCode == REQUEST_CODE_GRAPHS && resultCode == RESULT_OK){
            if (data.hasExtra("plant")) {
                plant = (Plant) data.getSerializableExtra("plant");
            }
        }
    }

}
