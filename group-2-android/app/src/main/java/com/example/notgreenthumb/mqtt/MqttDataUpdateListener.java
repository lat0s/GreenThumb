package com.example.notgreenthumb.mqtt;

public interface MqttDataUpdateListener {
    void onMqttDataUpdate(String parameter, String value);
}
