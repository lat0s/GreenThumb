package com.example.notgreenthumb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {
    private static final String TAG = "Dashboard";
    private MqttHelper mqttHelper;
    public TextView lightValueTextView;
    public TextView humidityValueTextView;
    public TextView temperatureValueTextView;
    public TextView moistureValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mqttHelper = new MqttHelper(this, new MqttDataUpdateListener() {
            @Override
            public void onMqttDataUpdate(String topic, String data) {
                switch (topic) {
                    case "light":
                        updateLightValue(data);
                        break;
                    case "humidity":
                        updateHumidityValue(data);
                        break;
                    case "temperature":
                        updateTemperatureValue(data);
                        break;
                    case "soil_moisture":
                        updateMoistureValue(data);
                        break;
                }
            }
        });

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
        double value = Double.parseDouble(lightValue);
        String formattedValue = String.format("%.0f", value);
        lightValueTextView.setText(formattedValue + "%");
    }

    private void updateHumidityValue(String humidityValue) {
        double value = Double.parseDouble(humidityValue);
        String formattedValue = String.format("%.0f", value);
        humidityValueTextView.setText(formattedValue + "%RH");
    }

    private void updateTemperatureValue(String temperatureValue) {
        double value = Double.parseDouble(temperatureValue);
        String formattedValue = String.format("%.0f", value);
        temperatureValueTextView.setText(formattedValue + "°C");
    }

    private void updateMoistureValue(String moistureValue) {
        double value = Double.parseDouble(moistureValue);
        String formattedValue = String.format("%.0f", value);
        moistureValueTextView.setText(formattedValue + "%");
    }

    protected void onDestroy() {
        super.onDestroy();
        mqttHelper.disconnect();
    }

    public interface MqttDataUpdateListener {
        void onMqttDataUpdate(String topic, String data);
    }
}