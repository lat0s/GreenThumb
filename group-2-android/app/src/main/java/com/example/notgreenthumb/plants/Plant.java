package com.example.notgreenthumb.plants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Plant implements Serializable {
    // Identity variables
    private UUID id;
    private int imageIndex = 1;
    private String plantName = "DEFAULT";

    // Current data variables? wip!

    private int lightValue = -1;
    private int humidityValue = -1;
    private int temperatureValue = -1;
    private int moistureValue =-1;
    private List<Long> wateringHistory = new ArrayList<>();

    // Min Max variables

    private double minTemp = 15.0;  // Average plant temperature: 15°C
    private double maxTemp = 25.0;  // Average plant temperature: 25°C
    private double minHumidity = 40.0;  // Average plant humidity: 40% RH
    private double maxHumidity = 60.0;  // Average plant humidity: 60% RH
    private double minMoisture = 30.0;  // Average soil moisture: 30%
    private double maxMoisture = 70.0;  // Average soil moisture: 70%
    private double minLight = 1000.0;  // Average light intensity: 1000 lux
    private double maxLight = 2000.0;  // Average light intensity: 2000 lux

    public Plant(int imageIndex, String plantName) {
        this.imageIndex = imageIndex;
        this.id = UUID.randomUUID();
        this.plantName = plantName;
    }

    public UUID getId() {
        return id;
    }
    public Plant() {
    }
    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public void setMinHumidity(double minHumidity) {
        this.minHumidity = minHumidity;
    }

    public void setMaxHumidity(double maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public void setMinMoisture(double minMoisture) {
        this.minMoisture = minMoisture;
    }

    public void setMaxMoisture(double maxMoisture) {
        this.maxMoisture = maxMoisture;
    }

    public void setMinLight(double minLight) {
        this.minLight = minLight;
    }

    public void setMaxLight(double maxLight) {
        this.maxLight = maxLight;
    }

    public double getMinTemp() {
        return minTemp;
    }
    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinHumidity() {
        return minHumidity;
    }

    public double getMaxHumidity() {
        return maxHumidity;
    }

    public double getMinMoisture() {
        return minMoisture;
    }

    public double getMaxMoisture() {
        return maxMoisture;
    }

    public double getMinLight() {
        return minLight;
    }

    public double getMaxLight() {
        return maxLight;
    }


    public String getMinMax(){
        String minMax = minTemp+","+maxTemp+","+minHumidity+","+maxHumidity+","+minMoisture+","+maxMoisture+","+minLight+","+maxLight;
        return minMax;
    }
    public String getCurrentValues(){
        String values = temperatureValue+","+humidityValue+","+moistureValue+","+lightValue;
        return values;
    }
    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setName(String plantName) {
        this.plantName = plantName;
    }

    @Override
    public String toString() {
        return plantName;
    }

    public int getLightValue() {
        return lightValue;
    }

    public void setLightValue(int lightValue) {
        this.lightValue = lightValue;
    }

    public int getHumidityValue() {
        return humidityValue;
    }

    public void setHumidityValue(int humidityValue) {
        this.humidityValue = humidityValue;
    }

    public int getTemperatureValue() {
        return temperatureValue;
    }

    public void setTemperatureValue(int temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public int getMoistureValue() {
        return moistureValue;
    }

    public void setMoistureValue(int moistureValue) {
        this.moistureValue = moistureValue;
    }

    public List<Long> getWateringHistory() {
        return wateringHistory;
    }
    public void setWateringHistory(List<Long> wateringHistory){
        this.wateringHistory = wateringHistory;
    }

    public void addToWateringHistory(long timestamp) {
        wateringHistory.add(timestamp);
    }

    public void removeFromWateringHistory(long timestamp) {
        wateringHistory.remove(timestamp);
    }
}


