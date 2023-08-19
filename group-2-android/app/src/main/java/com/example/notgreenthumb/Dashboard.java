package com.example.notgreenthumb;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.notgreenthumb.adapters.DialogBuilderAdapter;
import com.example.notgreenthumb.adapters.PlantAdapter;
import com.example.notgreenthumb.adapters.Transformer;
import com.example.notgreenthumb.plants.Plant;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    private static final String TAG = "Dashboard";
    private static final int REQUEST_CODE_PLANT_MAKER = 1;
    private static final int REQUEST_CODE_PLANT_PROFILE = 2;
    private static final int REQUEST_CODE_WATERING = 3;

    private ArrayList<Plant> plantList;
    private PlantAdapter plantAdapter;

    private ConstraintLayout mainLayout;
    private ImageButton menuButton;

    private FloatingActionButton addPlantButton;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
//    private Button hey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        initializeViews();

        // Set click listeners
        setListeners();

        // Setup background animation
        setupBackgroundAnimation();

        // Setup RecyclerView
        setupRecyclerView();

        // Load plant list from shared preferences
        loadPlantList();
    }

    /**
     * Initializes the views from the layout XML.
     */
    private void initializeViews() {
        mainLayout = findViewById(R.id.constraintLayout);
        menuButton = findViewById(R.id.menuButton);
        addPlantButton = findViewById(R.id.addPlantButton);
        viewPager = findViewById(R.id.horizontalRecyclerView);
        tabLayout = findViewById(R.id.tab_layout);
//        hey = findViewById(R.id.GetSensorData);

    }

    /**
     * Sets the click listeners for the buttons.
     */
    private void setListeners() {
        menuButton.setOnClickListener(view -> showCustomPopupMenu(view));

        addPlantButton.setOnClickListener(view -> openPlantMakerActivity());

//        hey.setOnClickListener(view -> getSensorData());
    }

    /**
     * Sets up the background animation.
     */
    private void setupBackgroundAnimation() {
        AnimationDrawable animationDrawable = (AnimationDrawable) mainLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
    }

    /**
     * Sets up the RecyclerView for displaying plants.
     * Tab layout with indicators was sourced from : https://stackoverflow.com/questions/38459309/how-do-you-create-an-android-view-pager-with-a-dots-indicator
     */
    private void setupRecyclerView() {
        plantList = new ArrayList<>();
        plantAdapter = new PlantAdapter(this, plantList);

        viewPager = findViewById(R.id.horizontalRecyclerView);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(plantAdapter);
        viewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer((int) getResources().getDimension(R.dimen.pageMargin)));
        compositePageTransformer.addTransformer(new Transformer());
        viewPager.setPageTransformer(compositePageTransformer);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText("" + (position + 1));
        }).attach();

        plantAdapter.setOnItemClickListener(new PlantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Plant clickedPlant = plantList.get(position);
                Intent intent = new Intent(Dashboard.this, PlantProfile.class);
                intent.putExtra("plant", clickedPlant);
                startActivityForResult(intent, REQUEST_CODE_PLANT_PROFILE);
            }
        });
    }




    /**
     * Loads the plant list from shared preferences.
     */
    private void loadPlantList() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        String json = sharedPreferences.getString("plant list", null);
        if (json != null) {
            Gson gson = new Gson();
            TypeToken<ArrayList<Plant>> token = new TypeToken<ArrayList<Plant>>() {};
            ArrayList<Plant> loadedPlantList = gson.fromJson(json, token.getType());

            if (loadedPlantList != null) {
                plantList.clear();
                plantList.addAll(loadedPlantList);
                plantAdapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "Failed to parse plant list from shared preferences");
            }
        } else {
            Log.i(TAG, "No saved plant list found in shared preferences");
        }
    }

    /**
     * Clears the plant list and updates the adapter.
     */
    private void clearPlantList() {
        plantList.clear();
        plantAdapter.notifyDataSetChanged();
        savePlantList();

        Log.d(TAG, "Plant list cleared");
    }

    /**
     * Saves the plant list to shared preferences using Gson.
     */
    private void savePlantList() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(plantList);
        editor.putString("plant list", json);
        editor.apply();

        Log.d(TAG, "Plant list saved to shared preferences");
    }

    /**
     * Opens the PlantMakerActivity to add a new plant.
     */
    private void openPlantMakerActivity() {
        Intent intent = new Intent(Dashboard.this, PlantMakerActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PLANT_MAKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG","works");

        if (requestCode == REQUEST_CODE_PLANT_MAKER && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("newPlant")) {
                Plant newPlant = (Plant) data.getSerializableExtra("newPlant");

                plantList.add(newPlant);
                plantAdapter.notifyDataSetChanged();

                savePlantList();

                Log.d(TAG, "New plant added: " + newPlant.toString());
            }
        }
       else if (requestCode == REQUEST_CODE_PLANT_PROFILE && resultCode == RESULT_OK){
            if (data.hasExtra("plant")) {
                Plant plantFromProfile = (Plant) data.getSerializableExtra("plant");
                replacePlant(plantFromProfile);
                }
            }
        }

    private void replacePlant(Plant updatedPlant) {
        for (int i = 0; i < plantList.size(); i++) {
            Plant plant = plantList.get(i);
            if (plant.getPlantName().equals(updatedPlant.getPlantName())) {
                plantList.set(i, updatedPlant);
                plantAdapter.notifyDataSetChanged();
                savePlantList();
                break; // Exit loop after finding and replacing the plant
            }
        }
    }

    /**
     * Shows a popup menu when the menu button is clicked.
     */
    private void showCustomPopupMenu(View view) {
        // Create arrays of menu items and their icons
        String[] menuItems = {"Settings", "Care Notifications", "Clear Plant List"};
        int[] menuIcons = {R.drawable.settings, R.drawable.notifications, R.drawable.delete};

        DialogBuilderAdapter adapter = new DialogBuilderAdapter(this, menuItems, menuIcons);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Select an option")
                .setAdapter(adapter, (dialog,which) -> {
                        // The 'which' argument contains the index position of the selected item
                        switch (which) {
                            default:
                                break;
                            case 0:
                                goToSettings();
                                break;
                            case 1:
                                goToNotifications();
                                break;
                            case 2:
                                showClearPlantListConfirmation();
                                break;
                        }
                })
                .show();
    }



    // Displays a confirmation message to see if the user is sure about deleting the plants
    private void showClearPlantListConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Clear Plant List");
        builder.setMessage("Are you sure you want to clear the plant list?");
        builder.setPositiveButton("Yes", (dialog, which) -> clearPlantList());
        builder.setNegativeButton("No", null);
        builder.show();
    }


    /**
     * Opens the Settings activity.
     */
    private void goToSettings() {
        Intent toSettings = new Intent(this, Settings.class);
        startActivity(toSettings);
    }


    private void goToNotifications() {
        Intent toNotifications = new Intent(this, CareNotifications.class);
        startActivity(toNotifications);
    }

//    private void getSensorData() {
//        Intent sensorData = new Intent(this, SensorData.class);
//        startActivity(sensorData);
//    }
}
