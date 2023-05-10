package com.example.notgreenthumb;

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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MqttManager {

    private static final String TAG = "MqttManager";
    private MqttAndroidClient mqttClient;
    private String[] topics;
    private MqttDataListener mqttDataListener;

    public MqttManager(Context context, String brokerUrl, String clientId, String[] topics, MqttDataListener mqttDataListener) {
        this.topics = topics;
        this.mqttDataListener = mqttDataListener;
        mqttClient = new MqttAndroidClient(context, brokerUrl, clientId);
    }

    public void connect() {
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d(TAG, "Connection lost", cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                mqttDataListener.onMqttDataReceived(topic, new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(TAG, "Delivery complete");
            }
        });

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setConnectionTimeout(60);
        options.setKeepAliveInterval(60);

        try {
            mqttClient.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Connected to MQTT broker");
                    subscribeToTopics();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Not Connected to MQTT broker");
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, "Error connecting to MQTT broker", e);
        }
    }

    private void subscribeToTopics() {
        for (String topic : topics) {
            try {
                mqttClient.subscribe(topic, 0);
                Log.d(TAG, "Subscribed to topic: " + topic);
            } catch (MqttException e) {
                Log.e(TAG, "Error subscribing to topic: " + topic, e);
            }
        }
    }

    public void disconnect() {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                for (String topic : topics) {
                    mqttClient.unsubscribe(topic);
                    Log.d(TAG, "Unsubscribed from topic: " + topic);
                }
                mqttClient.disconnect();
                Log.d(TAG, "Disconnected from MQTT broker");
            } catch(MqttException e) {
                Log.e(TAG, "Error disconnecting from MQTT broker", e);
            }
        }
    }

    public interface MqttDataListener {
        void onMqttDataReceived(String topic, String data);
    }
}
