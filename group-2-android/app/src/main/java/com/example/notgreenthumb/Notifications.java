package com.example.notgreenthumb;

import androidx.core.content.ContextCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.notgreenthumb.plants.Plant;
import com.example.notgreenthumb.R;

public class Notifications {
    private static final String CHANNEL_ID = "my_channel";
    private static final String CHANNEL_NAME = "My Channel";

    private int notificationId = 0;

    public void showNotification(Context context, String title, String text) {
        // Create a notification channel
        createNotificationChannel(context);

        notificationId++;
        // Create a notification using the support library's NotificationCompat.Builder class
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.aloevera__traced_)
                .setContentTitle(title)
                .setContentText(text)
                .setColor(ContextCompat.getColor(context, R.color.primaryTextColor))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void checkPlantConditions(Context context, Plant plant) {
        // Retrieve the current plant data values
        double temperature = plant.getTemperatureValue();
        double humidity = plant.getHumidityValue();
        double light = plant.getLightValue();
        double moisture = plant.getMoistureValue();

        // Check temperature value against the specified normal range
        if (temperature < plant.getMinTemp()) {
            String notificationText = plant.getPlantName() + " needs to be moved to warmer conditions";
            showNotification(context, "Temperature Alert", notificationText);
        } else if (temperature > plant.getMaxTemp()) {
            String notificationText = plant.getPlantName() + " needs to be moved to colder conditions";
            showNotification(context, "Temperature Alert", notificationText);
        }

        // Check humidity value against the specified normal range
        if (humidity < plant.getMinHumidity()) {
            String notificationText = "Air humidity is below the minimum threshold for " + plant.getPlantName() + ", please improve surrounding conditions";
            showNotification(context, "Humidity Alert", notificationText);
        } else if (humidity > plant.getMaxHumidity()) {
            String notificationText = "Humidity is above the maximum threshold for " + plant.getPlantName() + ", please improve surrounding conditions";
            showNotification(context, "Humidity Alert", notificationText);
        }

        // Check light value against the specified normal range
        if (light < plant.getMinLight()) {
            String notificationText = plant.getPlantName() + " needs more light";
            showNotification(context, "Light Alert", notificationText);
        } else if (light > plant.getMaxLight()) {
            String notificationText = "Light is above the maximum threshold for " + plant.getPlantName() + ", please protect it from excessive direct sunlight";
            showNotification(context, "Light Alert", notificationText);
        }

        // Check soil moisture value against the specified normal range
        if (moisture < plant.getMinMoisture()) {
            String notificationText = plant.getPlantName() + " needs watering";
            showNotification(context, "Moisture Alert", notificationText);
        } else if (moisture > plant.getMaxMoisture()) {
            String notificationText = "Soil moisture is above the maximum threshold for " + plant.getPlantName() + ", please water less frequently";
            showNotification(context, "Moisture Alert", notificationText);
        }
    }

}
