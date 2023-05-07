package com.example.notgreenthumb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity implements MqttManager.MqttDataListener {

    private static final String TAG = "Dashboard";
    private MqttManager mqttManager;
    public TextView lightValueTextView;
    public TextView humidityValueTextView;
    public TextView temperatureValueTextView;
    public TextView moistureValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initMqttClient();

        lightValueTextView = findViewById(R.id.light_value);
        humidityValueTextView = findViewById(R.id.humidity_value);
        temperatureValueTextView = findViewById(R.id.temperature_value);
        moistureValueTextView = findViewById(R.id.moisture_value);

        Button homeToPlantProfiles = (Button)findViewById(R.id.homeToPlantProfilesButton);
        homeToPlantProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPlantProfiles();
            }
        });

        Button homeToSettings = (Button)findViewById(R.id.homeToSettingsButton);
        homeToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettings();
            }
        });

        Button homeToNotifications = (Button)findViewById(R.id.homeToNotificationsButton);
        homeToNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNotifications();
            }
        });
    }


    public void onMqttDataReceived(String topic, String data) {
        switch (topic) {
            case "sensors/light":
                updateLightValue(data);
                break;
            case "sensors/humidity":
                updateHumidityValue(data);
                break;
            case "sensors/temperature":
                updateTemperatureValue(data);
                break;
            case "sensors/soil_moisture":
                updateMoistureValue(data);
                break;
            default:
                Log.d(TAG, "Unknown topic: " + topic);
        }
    }
    private void initMqttClient() {
        String brokerUrl = "tcp://192.168.130.15:1883";
        String clientId = "latos";
        String[] topics = {
                "sensors/light",
                "sensors/humidity",
                "sensors/temperature",
                "sensors/soil_moisture"
        };

        mqttManager = new MqttManager(this, brokerUrl, clientId, topics, this);
        mqttManager.connect();
    }

    private void goToPlantProfiles(){
        Intent toPlantProfiles = new Intent(this,PlantProfiles.class);
        startActivity(toPlantProfiles);
    }

    private void goToSettings(){
        Intent toSettings = new Intent(this,Settings.class);
        startActivity(toSettings);
    }

    private void goToNotifications(){
        Intent toNotifications = new Intent(this,CareNotifications.class);
        startActivity(toNotifications);
    }

    private void updateLightValue(String lightValue) {
        lightValueTextView.setText(lightValue + "%");
    }

    private void updateHumidityValue(String humidityValue) {
        humidityValueTextView.setText(humidityValue + "%RH");
    }

    private void updateTemperatureValue(String temperatureValue) {
        temperatureValueTextView.setText(temperatureValue + "°C");
    }

    private void updateMoistureValue(String moistureValue) {
        moistureValueTextView.setText(moistureValue + "%");
    }
    protected void onDestroy() {
        super.onDestroy();
        mqttManager.disconnect();
    }
}