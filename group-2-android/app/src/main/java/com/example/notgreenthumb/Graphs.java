package com.example.notgreenthumb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notgreenthumb.plants.Plant;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

// The Graphs.java functionality has been sourced from this documentation: https://weeklycoding.com/mpandroidchart-documentation/
public class Graphs extends AppCompatActivity {
    private Plant plant;
    private final Gson gson = new Gson();
    private LineChart lineChart;
    private LineData lineData;

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
            List<Data> dataList = loadExistingData(plant);

            lineChart = findViewById(R.id.chartView);
            setupLineChart(lineChart);

            lineData = prepareLineData(dataList);
            lineChart.setData(lineData);
            lineChart.invalidate();


            lineChart.animateX(1000);

            Log.d("Graphs", "DataList size: " + dataList.size());
        }

        CheckBox checkboxTemperature = findViewById(R.id.checkboxTemperature);
        CheckBox checkboxLight = findViewById(R.id.checkboxLight);
        CheckBox checkboxHumidity = findViewById(R.id.checkboxHumidity);
        CheckBox checkboxSoilMoisture = findViewById(R.id.checkboxSoilMoisture);

        checkboxTemperature.setOnCheckedChangeListener((buttonView, isChecked) -> toggleLineVisibility("Temperature", isChecked));

        checkboxLight.setOnCheckedChangeListener((buttonView, isChecked) -> toggleLineVisibility("Light", isChecked));

        checkboxHumidity.setOnCheckedChangeListener((buttonView, isChecked) -> toggleLineVisibility("Humidity", isChecked));

        checkboxSoilMoisture.setOnCheckedChangeListener((buttonView, isChecked) -> toggleLineVisibility("Soil Moisture", isChecked));

        back.setOnClickListener(view -> backToProfile());
    }

    private void backToProfile() {
        Intent intent = new Intent(Graphs.this, PlantProfile.class);
        intent.putExtra("plant", plant);
        setResult(RESULT_OK, intent);
        finish();
    }


    private void toggleLineVisibility(String dataType, boolean isVisible) {
        for (ILineDataSet dataSet : lineData.getDataSets()) {
            if (dataSet.getLabel().equals(dataType)) {
                dataSet.setVisible(isVisible);
                lineChart.animateX(1000);
            }
        }
        lineChart.invalidate();
    }

    private List<Data> loadExistingData(Plant plant) {
        List<Data> dataList = new ArrayList<>();

        File internalDir = getFilesDir();
        String filename = "plant_data_" + plant.getPlantName() + ".json";
        File dataFile = new File(internalDir, filename);

        try (FileReader reader = new FileReader(dataFile)) {
            Type listType = new TypeToken<List<Data>>() {
            }.getType();
            dataList = gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private void setupLineChart(LineChart chart) {
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);

        Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(5);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(true);

        chart.getAxisRight().setEnabled(false);

        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0f) {
                    return "";
                } else {
                    return super.getFormattedValue(value);
                }
            }
        });

    }

    private LineData prepareLineData(List<Data> dataList) {
        List<Entry> temperatureEntries = prepareLineEntries(dataList, "temperature");
        LineDataSet temperatureDataSet = new LineDataSet(temperatureEntries, "Temperature");
        temperatureDataSet.setColor(Color.RED);
        temperatureDataSet.setMode(LineDataSet.Mode.LINEAR);

        List<Entry> lightEntries = prepareLineEntries(dataList, "light");
        LineDataSet lightDataSet = new LineDataSet(lightEntries, "Light");
        lightDataSet.setColor(Color.YELLOW);
        lightDataSet.setMode(LineDataSet.Mode.LINEAR);

        List<Entry> humidityEntries = prepareLineEntries(dataList, "humidity");
        LineDataSet humidityDataSet = new LineDataSet(humidityEntries, "Humidity");
        humidityDataSet.setColor(Color.BLUE);
        humidityDataSet.setMode(LineDataSet.Mode.LINEAR);

        List<Entry> soilMoistureEntries = prepareLineEntries(dataList, "soil_moisture");
        LineDataSet soilMoistureDataSet = new LineDataSet(soilMoistureEntries, "Soil Moisture");
        soilMoistureDataSet.setColor(Color.GREEN);
        soilMoistureDataSet.setMode(LineDataSet.Mode.LINEAR);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(temperatureDataSet);
        dataSets.add(lightDataSet);
        dataSets.add(humidityDataSet);
        dataSets.add(soilMoistureDataSet);

        return new LineData(dataSets);
    }

    private List<Entry> prepareLineEntries(List<Data> dataList, String dataType) {
        List<Entry> lineEntries = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            int yValue = 0;
            switch (dataType) {
                case "temperature":
                    yValue = dataList.get(i).getTemperature();
                    break;
                case "light":
                    yValue = dataList.get(i).getLight();
                    break;
                case "humidity":
                    yValue = dataList.get(i).getHumidity();
                    break;
                case "soil_moisture":
                    yValue = dataList.get(i).getSoilMoisture();
                    break;
            }


            if (yValue != 0) {
                lineEntries.add(new Entry(i, yValue));
            }
        }

        return lineEntries;
    }
}


