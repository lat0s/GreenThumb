package com.example.notgreenthumb.mqtt;


import android.content.Context;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.android.service.MqttAndroidClient;

import java.util.Arrays;

public class MqttHelper {
    private static final String TAG = "MqttHelper";
    public MqttAndroidClient mqttClient;
    private String[] topics;
    private MqttDataUpdateListener dataUpdateListener;


    public MqttHelper(Context context, MqttDataUpdateListener dataUpdateListener, String brokerUrl, String clientId) {
        this.dataUpdateListener = dataUpdateListener;
        initMqttClient(context, brokerUrl, clientId);
    }

    private void initMqttClient(Context context, String brokerUrl, String clientId) {
        topics = new String[] {
                "sensor/data"
        };

        mqttClient = new MqttAndroidClient(context, brokerUrl, clientId);
        mqttClient.setCallback(createMqttCallback());

        MqttConnectOptions options = createMqttConnectOptions();

        try {
            mqttClient.connect(options, null, createMqttConnectActionListener());
        } catch (MqttException e) {
            Log.e(TAG, "Error connecting to MQTT broker", e);
        }
    }

    private MqttCallback createMqttCallback() {
        return new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d(TAG, "Connection lost", cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                onMqttDataReceived(topic, new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(TAG, "Delivery complete");
            }
        };
    }

    private MqttConnectOptions createMqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setConnectionTimeout(60);
        options.setKeepAliveInterval(60);
        return options;
    }

    private IMqttActionListener createMqttConnectActionListener() {
        return new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.d(TAG, "Connected to MQTT broker");
                subscribeToTopics();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.d(TAG, "Not connected to MQTT broker");
            }
        };
    }

    private void subscribeToTopics() {
        try {
            mqttClient.subscribe(topics, new int[topics.length]);
            Log.d(TAG, "Subscribed to topics: " + Arrays.toString(topics));
        } catch (MqttException e) {
            Log.e(TAG, "Error subscribing to topics", e);
        }
    }

    public void disconnect() {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttClient.unsubscribe(topics);
                mqttClient.disconnect();
                Log.d(TAG, "Disconnected from MQTT broker");
            } catch (MqttException e) {
                Log.e(TAG, "Error disconnecting from MQTT broker", e);
            }
        }
    }

    public void publishMessage(String topic, String messageContent) {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                MqttMessage message = new MqttMessage();
                message.setPayload(messageContent.getBytes());
                mqttClient.publish(topic, message);
                Log.d(TAG, "Message published to topic: " + topic);
            } else {
                Log.e(TAG, "Error publishing message, client is not connected");
            }
        } catch (MqttException e) {
            Log.e(TAG, "Error publishing message: " + e.getMessage(), e);
        }
    }


    private void onMqttDataReceived(String topic, String data) {
        // parse the data received
        String[] dataValues = data.split(",");
        if (dataValues.length == 4) {
            String temperature = dataValues[0];
            String humidity = dataValues[1];
            String light = dataValues[2];
            String soil_moisture = dataValues[3];

            // update the data for each parameter
            dataUpdateListener.onMqttDataUpdate("temperature", temperature);
            dataUpdateListener.onMqttDataUpdate("humidity", humidity);
            dataUpdateListener.onMqttDataUpdate("light", light);
            dataUpdateListener.onMqttDataUpdate("soil_moisture", soil_moisture);
        } else {
            Log.e(TAG, "Received data in incorrect format");
        }
    }
}