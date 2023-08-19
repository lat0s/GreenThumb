package com.example.notgreenthumb;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;

import com.example.notgreenthumb.plants.Plant;

public class Graphs extends AppCompatActivity {
    Plant plant;
    private static final int REQUEST_CODE_PLANTPROFILE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        Button back = findViewById(R.id.backButton);
        TextView name = findViewById(R.id.plantname);

        Intent intent = getIntent();
        plant = (Plant) intent.getSerializableExtra("plant");
        if (plant != null) {
            name.setText(plant.getPlantName());
        }

        back.setOnClickListener(view -> backToProfile());


    }

    private void backToProfile() {
        Intent intent = new Intent(Graphs.this, PlantProfile.class);
        intent.putExtra("plant",plant);
        setResult(RESULT_OK,intent);
        finish();
    }
}