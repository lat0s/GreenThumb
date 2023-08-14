            package com.example.notgreenthumb;


            import android.content.Intent;
            import android.content.res.ColorStateList;
            import android.graphics.Color;
            import android.graphics.drawable.AnimationDrawable;
            import android.os.Bundle;
            import android.text.Editable;
            import android.text.TextUtils;
            import android.text.TextWatcher;
            import android.util.Log;
            import android.view.View;
            import android.widget.Button;
            import android.widget.EditText;
            import android.widget.Toast;

            import androidx.appcompat.app.AppCompatActivity;
            import androidx.constraintlayout.widget.ConstraintLayout;
            import androidx.recyclerview.widget.LinearLayoutManager;
            import androidx.recyclerview.widget.RecyclerView;

            import com.example.notgreenthumb.adapters.CreatePlantImageAdapter;
            import com.example.notgreenthumb.plants.Plant;

            import java.util.ArrayList;
            import java.util.List;

            public class PlantMakerActivity extends AppCompatActivity {
                // Field declarations
                private EditText plantNameEditText;
                private Button createPlantButton;
                private static final String TAG = "PlantMaker";
                private static final int REQUEST_CODE_DASHBOARD = 1;
                private Button back;
                private int selectedImageIndex;
                private ConstraintLayout pmkerLayout;
                private Plant newPlant;

                private EditText minTempEditText, maxTempEditText, minHumidityEditText, maxHumidityEditText,
                        minSoilMoistureEditText, maxSoilMoistureEditText, minLightEditText, maxLightEditText;

                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_plant_maker);

                    // Initialize the UI components
                    plantNameEditText = findViewById(R.id.plantNameEditText);
                    createPlantButton = findViewById(R.id.createPlantButton);
                    pmkerLayout = findViewById(R.id.constraintLayoutPmker);
                    back = findViewById(R.id.backButton);
                    back.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));

                    // Initialize plant object
                    newPlant = new Plant();


                    // Initialize the EditText fields
                    minTempEditText = findViewById(R.id.minTemp);
                    maxTempEditText = findViewById(R.id.maxTemp);
                    minHumidityEditText = findViewById(R.id.minHumidity);
                    maxHumidityEditText = findViewById(R.id.maxHumidity);
                    minSoilMoistureEditText = findViewById(R.id.minSoilMoisture);
                    maxSoilMoistureEditText = findViewById(R.id.maxSoilMoisture);
                    minLightEditText = findViewById(R.id.minLight);
                    maxLightEditText = findViewById(R.id.maxLight);

                    // Set up the RecyclerView
                    RecyclerView imageRecyclerView = findViewById(R.id.selectPlantImage);
                    imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

                    List<Integer> imageList = getPlantImages();
                    CreatePlantImageAdapter createPlantImageAdapter = new CreatePlantImageAdapter(this, imageList, new CreatePlantImageAdapter.OnImageClickListener() {
                        @Override
                        public void onImageClick(int position) {
                            position++;
                            selectedImageIndex = position;
                        }
                    });
                    imageRecyclerView.setAdapter(createPlantImageAdapter);

                    // Define the button click behavior
                    createPlantButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String plantName = plantNameEditText.getText().toString();
                            newPlant.setImageIndex(selectedImageIndex);  // Set the image index of the Plant object here
                            newPlant.setName(plantName);  // Set the name of the Plant object here
                            Intent intent = new Intent();
                            intent.putExtra("newPlant", newPlant);
                            setResult(RESULT_OK, intent);
                            Log.d("TAG","IMage position is " + newPlant.getImageIndex() + selectedImageIndex);
                            finish();
                        }
                    });
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToDash();
                        }
                    });

                    // Add TextChangedListeners to all EditTexts
                    minTempEditText.addTextChangedListener(createTextWatcher());
                    maxTempEditText.addTextChangedListener(createTextWatcher());
                    minHumidityEditText.addTextChangedListener(createTextWatcher());
                    maxHumidityEditText.addTextChangedListener(createTextWatcher());
                    minSoilMoistureEditText.addTextChangedListener(createTextWatcher());
                    maxSoilMoistureEditText.addTextChangedListener(createTextWatcher());
                    minLightEditText.addTextChangedListener(createTextWatcher());
                    maxLightEditText.addTextChangedListener(createTextWatcher());
                }

                private List<Integer> getPlantImages() {
                    List<Integer> plantImages = new ArrayList<>();
                    for (int i = 1; i <= 6; i++) {
                        String imageName = "plant" + i;
                        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                        plantImages.add(resId);
                    }
                    return plantImages;
                }

                private void setupBackgroundAnimation() {
                    AnimationDrawable animationDrawable = (AnimationDrawable) pmkerLayout.getBackground();
                    animationDrawable.setEnterFadeDuration(1500);
                    animationDrawable.setExitFadeDuration(3000);
                    animationDrawable.start();
                }
                private void backToDash() {
                    Intent intent = new Intent(PlantMakerActivity.this, Dashboard.class);
                    startActivityForResult(intent, REQUEST_CODE_DASHBOARD);
                }

                private TextWatcher createTextWatcher() {
                    return new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            // Convert the EditText content to double values, use default values if empty
                            double minTemp = TextUtils.isEmpty(minTempEditText.getText()) ? 0.0 : Double.parseDouble(minTempEditText.getText().toString());
                            double maxTemp = TextUtils.isEmpty(maxTempEditText.getText()) ? 0.0 : Double.parseDouble(maxTempEditText.getText().toString());
                            double minHumidity = TextUtils.isEmpty(minHumidityEditText.getText()) ? 0.0 : Double.parseDouble(minHumidityEditText.getText().toString());
                            double maxHumidity = TextUtils.isEmpty(maxHumidityEditText.getText()) ? 0.0 : Double.parseDouble(maxHumidityEditText.getText().toString());
                            double minSoilMoisture = TextUtils.isEmpty(minSoilMoistureEditText.getText()) ? 0.0 : Double.parseDouble(minSoilMoistureEditText.getText().toString());
                            double maxSoilMoisture = TextUtils.isEmpty(maxSoilMoistureEditText.getText()) ? 0.0 : Double.parseDouble(maxSoilMoistureEditText.getText().toString());
                            double minLight = TextUtils.isEmpty(minLightEditText.getText()) ? 0.0 : Double.parseDouble(minLightEditText.getText().toString());
                            double maxLight = TextUtils.isEmpty(maxLightEditText.getText()) ? 0.0 : Double.parseDouble(maxLightEditText.getText().toString());

                            newPlant.setMinTemp(minTemp);
                            newPlant.setMaxTemp(maxTemp);
                            newPlant.setMinHumidity(minHumidity);
                            newPlant.setMaxHumidity(maxHumidity);
                            newPlant.setMinMoisture(minSoilMoisture);
                            newPlant.setMaxMoisture(maxSoilMoisture);
                            newPlant.setMinLight(minLight);
                            newPlant.setMaxLight(maxLight);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            // Parse all min and max values from EditTexts, using default values if empty
                            double minTemp = TextUtils.isEmpty(minTempEditText.getText()) ? 0.0 : Double.parseDouble(minTempEditText.getText().toString());
                            double maxTemp = TextUtils.isEmpty(maxTempEditText.getText()) ? 0.0 : Double.parseDouble(maxTempEditText.getText().toString());
                            double minHumidity = TextUtils.isEmpty(minHumidityEditText.getText()) ? 0.0 : Double.parseDouble(minHumidityEditText.getText().toString());
                            double maxHumidity = TextUtils.isEmpty(maxHumidityEditText.getText()) ? 0.0 : Double.parseDouble(maxHumidityEditText.getText().toString());
                            double minSoilMoisture = TextUtils.isEmpty(minSoilMoistureEditText.getText()) ? 0.0 : Double.parseDouble(minSoilMoistureEditText.getText().toString());
                            double maxSoilMoisture = TextUtils.isEmpty(maxSoilMoistureEditText.getText()) ? 0.0 : Double.parseDouble(maxSoilMoistureEditText.getText().toString());
                            double minLight = TextUtils.isEmpty(minLightEditText.getText()) ? 0.0 : Double.parseDouble(minLightEditText.getText().toString());
                            double maxLight = TextUtils.isEmpty(maxLightEditText.getText()) ? 0.0 : Double.parseDouble(maxLightEditText.getText().toString());

                            // Show a warning if max value is less than min value for any parameter
                            if (minTemp > maxTemp) {
                                Toast.makeText(PlantMakerActivity.this, "Warning: max temperature is less than min temperature!", Toast.LENGTH_SHORT).show();
                            }
                            if (minHumidity > maxHumidity) {
                                Toast.makeText(PlantMakerActivity.this, "Warning: max humidity is less than min humidity!", Toast.LENGTH_SHORT).show();
                            }
                            if (minSoilMoisture > maxSoilMoisture) {
                                Toast.makeText(PlantMakerActivity.this, "Warning: max soil moisture is less than min soil moisture!", Toast.LENGTH_SHORT).show();
                            }
                            if (minLight > maxLight) {
                                Toast.makeText(PlantMakerActivity.this, "Warning: max light is less than min light!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                }
            }


