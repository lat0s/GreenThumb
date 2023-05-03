package com.example.notgreenthumb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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
}