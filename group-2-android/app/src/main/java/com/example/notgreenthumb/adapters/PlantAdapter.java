package com.example.notgreenthumb.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import com.example.notgreenthumb.PlantProfile;
import com.example.notgreenthumb.R;
import com.example.notgreenthumb.plants.Plant;

import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private Context context;
    private ArrayList<Plant> plantList;

    public PlantAdapter(Context context, ArrayList<Plant> plantList) {
        this.context = context;
        this.plantList = plantList;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plantItem = plantList.get(position);
        holder.bind(plantItem);
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public class PlantViewHolder extends RecyclerView.ViewHolder {

        private ImageView plantImageView;
        private TextView plantNameTextView;
        private CardView plantCardView;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImageView = itemView.findViewById(R.id.plantImageView);
            plantNameTextView = itemView.findViewById(R.id.plantNameTextView);
            plantCardView = itemView.findViewById(R.id.cardViewPlant);

            plantCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Plant clickedPlant = plantList.get(position);
                        Intent intent = new Intent(context, PlantProfile.class);
                        intent.putExtra("plantName", clickedPlant.getPlantName());
                        intent.putExtra("plantImageIndex", clickedPlant.getImageIndex());
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(Plant plantItem) {
            String imageName = "plant" + plantItem.getImageIndex();
            int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            plantImageView.setImageResource(resID);
            plantNameTextView.setText(plantItem.getPlantName());
        }

    }
}
