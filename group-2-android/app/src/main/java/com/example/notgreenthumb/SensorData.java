//package com.example.notgreenthumb;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.View;
//import android.widget.ArrayAdapter;
//
//import com.example.notgreenthumb.databinding.ActivitySensorDataBinding;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//
//
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//
//public class SensorData extends AppCompatActivity {
//
//    ActivitySensorDataBinding binding;
//    ArrayList<String> dataList;
//    ArrayAdapter<String> listAdapter;
//
//    Handler mainHandler = new Handler();
//    ProgressDialog progressDialog;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivitySensorDataBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        initializeDataList();
//
//        binding.FetchData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new FetchData().start();
//            }
//        });
//    }
//
//    private void initializeDataList() {
//        dataList = new ArrayList<>();
//        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, dataList);
//        binding.ListData.setAdapter(listAdapter);
//    }
//
//    class FetchData extends Thread {
//        String data = "";
//
//        @Override
//        public void run() {
//            mainHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    progressDialog = new ProgressDialog(SensorData.this);
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//                }
//            });
//
//            try {
//                URL url = new URL("http://192.168.216.192:3000/data.json");
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                String line;
//
//                while ((line = bufferedReader.readLine()) != null) {
//                    data = data + line;
//                }
//
//                if (!data.isEmpty()) {
//                    JSONArray dataArray = new JSONArray(data);
//
//                    dataList.clear();
//
//                    for (int i = 0; i < dataArray.length(); i++) {
//                        JSONObject jsonObject = dataArray.getJSONObject(i);
//                        String timestamp = jsonObject.optString("timestamp");
//                        String temperature = jsonObject.optString("temperature");
//                        String humidity = jsonObject.optString("humidity");
//                        String light = jsonObject.optString("light");
//                        String soilmoisture = jsonObject.optString("soilmoisture");
//
//                        String formattedDate = convertTimestampToDate(timestamp);
//                        dataList.add("Date: " + formattedDate);
//                        dataList.add("Temperature: " + temperature);
//                        dataList.add("Humidity: " + humidity);
//                        dataList.add("Light: " + light);
//                        dataList.add("Soil Moisture: " + soilmoisture);
//                    }
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            mainHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (progressDialog.isShowing())
//                        progressDialog.dismiss();
//
//                    listAdapter.notifyDataSetChanged();
//                }
//            });
//        }
//    }
//
//    private String convertTimestampToDate(String timestamp) {
//        try {
//            // Parse the timestamp string to a long value
//            long timestampValue = Long.parseLong(timestamp);
//
//            // Create a Date object using the timestamp (assuming it's in milliseconds)
//            Date date = new Date(timestampValue);
//
//            // Define the desired date format
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//            // Format the date to the desired format
//            String formattedDate = dateFormat.format(date);
//
//            // Return the formatted date
//            return formattedDate;
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//
//        return ""; // Return an empty string if the conversion fails
//    }
//}
