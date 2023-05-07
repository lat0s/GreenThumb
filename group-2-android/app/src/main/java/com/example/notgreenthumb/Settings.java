package com.example.notgreenthumb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends AppCompatActivity {
    Button url;
    TextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        url = findViewById(R.id.button);
        mail = findViewById(R.id.textView4);

        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL("https://www.youtube.com/watch?v=OwCZq-maNb0");
            }
        });

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