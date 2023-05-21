package com.example.notgreenthumb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notgreenthumb.mqtt.MqttDataUpdateListener;
import com.example.notgreenthumb.mqtt.MqttHelper;

public class Mqtt_Settings extends AppCompatActivity {

    private static final String PREFS_NAME = "MqttPrefs";
    private static final String PREF_BROKER_URL = "brokerUrl";
    private static final String PREF_CLIENT_ID = "clientId";

    private SharedPreferences prefs;
    private Button back, connect;
    private EditText host, port;
    private static final int REQUEST_CODE_SETTINGS = 1;

    private MqttDataUpdateListener dataUpdateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt_settings);

        back = findViewById(R.id.MqttSettingstoSettingsButton);
        connect = findViewById(R.id.connectButton);
        host = findViewById(R.id.mqttHostEditText);
        port = findViewById(R.id.mqttPortEditText);

        back.setOnClickListener(v -> backToSettings());

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String savedBrokerUrl = prefs.getString(PREF_BROKER_URL, "tcp://192.168.50.9:1883"); // default value
        String savedClientId = prefs.getString(PREF_CLIENT_ID, "GreenThumb"); // default value

        host.setText(savedBrokerUrl);
        port.setText(savedClientId);

        connect.setOnClickListener(v -> {
            String brokerUrl = host.getText().toString();
            String clientId = port.getText().toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(PREF_BROKER_URL, brokerUrl);
            editor.putString(PREF_CLIENT_ID, clientId);
            editor.apply();

            MqttHelper mqttHelper = new MqttHelper(Mqtt_Settings.this, dataUpdateListener, brokerUrl, clientId, new MqttHelper.MqttHelperListener() {
                @Override
                public void onConnected() {
                    Toast.makeText(Mqtt_Settings.this, "Connected successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(Mqtt_Settings.this, "Error: " + message, Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void backToSettings() {
        Intent backToSettings = new Intent(this, Settings.class);
        startActivity(backToSettings);
    }
}
