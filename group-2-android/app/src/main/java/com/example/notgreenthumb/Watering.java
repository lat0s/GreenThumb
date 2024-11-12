package com.example.notgreenthumb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notgreenthumb.plants.Plant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Watering extends AppCompatActivity {
    ListView historyListView;
    private static final int REQUEST_CODE_PLANTPROFILE = 3;


    Plant plant;
    TextView name;
    ArrayAdapter<String> historyAdapter;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watering);

        name = findViewById(R.id.plantname);

        historyListView = findViewById(R.id.historyListView);
        Button back = findViewById(R.id.backButton);

        plant = (Plant) getIntent().getSerializableExtra("plant");

        historyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getFormattedHistory());
        historyListView.setAdapter(historyAdapter);
        Intent intent = getIntent();
        plant = (Plant) intent.getSerializableExtra("plant");
        if (plant != null) {
            name.setText(plant.getPlantName());
        }
        back.setOnClickListener(view -> backToProfile());

        historyListView.setOnItemClickListener((parent, view, position, id) -> {
            if (plant != null) {
                long timestamp = plant.getWateringHistory().get(position);
                String formattedTimestamp = "Watered: " + sdf.format(new Date(timestamp));
                long elapsedTimeMillis = System.currentTimeMillis() - timestamp;


                long hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMillis);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis) - TimeUnit.HOURS.toMinutes(hours);


                String elapsedTimeString = "This entry was " + hours + " hours and " + minutes + " minutes ago";
                showTimestampDialog(formattedTimestamp, elapsedTimeString);
            }
        });
    }

    private List<String> getFormattedHistory() {
        List<String> formattedHistory = new ArrayList<>();

        if (plant != null) {
            for (Long timestamp : plant.getWateringHistory()) {
                String formattedTimestamp = "Watered: " + sdf.format(new Date(timestamp));
                formattedHistory.add(formattedTimestamp);
            }
        }

        return formattedHistory;
    }

    private void backToProfile() {
        Intent intent = new Intent(Watering.this, PlantProfile.class);
        intent.putExtra("plant",plant);
        setResult(RESULT_OK,intent);
        finish();
    }


    private void showTimestampDialog(String formattedTimestamp, String elapsedTimeString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Watering Entry Details");
        builder.setMessage(formattedTimestamp + "\n" + elapsedTimeString);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
