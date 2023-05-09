package com.example.notgreenthumb;

import android.content.Context;
import android.util.Log;

public class MqttHelper implements MqttManager.MqttDataListener {
    private static final String TAG = "MqttHelper";
    private MqttManager mqttManager;
    private Dashboard.MqttDataUpdateListener dataUpdateListener;

    public MqttHelper(Context context, Dashboard.MqttDataUpdateListener dataUpdateListener) {
        this.dataUpdateListener = dataUpdateListener;
        initMqttClient(context);
    }

    private void initMqttClient(Context context) {
        String brokerUrl = "tcp://192.168.130.15:1883";
        String clientId = "latos";
        String[] topics = {
                "light",
                "humidity",
                "temperature",
                "soil_moisture"
        };

        mqttManager = new MqttManager(context, brokerUrl, clientId, topics, this);
        mqttManager.connect();
    }

    public void disconnect() {
        mqttManager.disconnect();
    }

    @Override
    public void onMqttDataReceived(String topic, String data) {
        dataUpdateListener.onMqttDataUpdate(topic, data);
    }
}
