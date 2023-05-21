package com.example.notgreenthumb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends AppCompatActivity {
    Button button;
    TextView mail;
    Button theme;
    boolean isNightModeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        theme = findViewById(R.id.themeChange);

        // Initialize the night mode state
        isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;

        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle the night mode state
                isNightModeOn = !isNightModeOn;

                // Update the night mode immediately
                if (isNightModeOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL("https://www.youtube.com/watch?v=OwCZq-maNb0");
            }
        });
        mail = findViewById(R.id.textView4);

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL("mailto:nishchya@hotmail.com");
            }
        });

        Button settingsToHome = (Button)findViewById(R.id.settingsToHomeButton);
        settingsToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });
    }

    private void URL(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void goHome(){
        Intent goHome = new Intent(this, Dashboard.class);
        startActivity(goHome);
    }

}