package com.example.notgreenthumb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CareNotifications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_notifications);

        Button careNotificationsToHome = (Button)findViewById(R.id.NotificationsToHomeButton);
        careNotificationsToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });

    }

    private void goHome(){
        Intent goHome = new Intent(this,Dashboard.class);
        startActivity(goHome);
    }
}