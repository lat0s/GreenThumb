package com.example.notgreenthumb;
import java.io.Serializable;

public class Data implements Serializable{
    private long timestamp;
    private int temperature;
    private int humidity;
    private int light;
    private int soilMoisture;

    public Data(long timestamp, int temperature, int humidity, int light, int soilMoisture) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.soilMoisture = soilMoisture;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Data(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(int soilMoisture) {
        this.soilMoisture = soilMoisture;
    }
}
