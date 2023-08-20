package com.example.notgreenthumb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class Settings extends AppCompatActivity {
    Button button;
    TextView mail;
    Button theme;
    boolean isNightModeOn;
    Button database;
    Button mqttSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        theme = findViewById(R.id.themeChange);

        // Initialize the night mode state
        isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;

        theme.setOnClickListener(view -> {
            // Toggle the night mode state
            isNightModeOn = !isNightModeOn;

            // Update the night mode immediately
            if (isNightModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        database = findViewById(R.id.PlantDatabaseButton);
        database.setOnClickListener(view -> URL("https://www.dropbox.com/s/iwppwrmsyxeitt1/GreenThumb%20Plant%20Database%20.pdf?dl=0"));

        button = findViewById(R.id.button);

        button.setOnClickListener(view -> URL("https://youtu.be/ro6_CHGE1a4"));
        mail = findViewById(R.id.textView4);

        mail.setOnClickListener(view -> URL("mailto:gusaryni@student.gu.se"));

        Button settingsToHome = (Button)findViewById(R.id.settingsToHomeButton);
        settingsToHome.setOnClickListener(view -> goHome());

        mqttSettings = findViewById(R.id.MqttSettingsButton);
        mqttSettings.setOnClickListener(view -> goMqttSettings());
    }

    private void URL(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void goHome(){
        Intent goHome = new Intent(this, Dashboard.class);
        startActivity(goHome);
    }
    private void goMqttSettings() {
        Intent goMqttSettings = new Intent(this, Mqtt_Settings.class);
        startActivity(goMqttSettings);
    }

}