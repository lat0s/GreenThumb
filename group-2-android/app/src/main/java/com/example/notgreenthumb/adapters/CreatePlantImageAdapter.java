package com.example.notgreenthumb.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notgreenthumb.R;

import java.util.List;

public class CreatePlantImageAdapter extends RecyclerView.Adapter<CreatePlantImageAdapter.ImageViewHolder> {

    private Context context;
    private List<Integer> imageList;
    private OnImageClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    public CreatePlantImageAdapter(Context context, List<Integer> imageList, OnImageClickListener listener) {
        this.context = context;
        this.imageList = imageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_vertical, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        int imageResId = imageList.get(position);
        holder.bind(imageResId, position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void setSelectedPosition(int position) {
        int previousSelectedPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previousSelectedPosition);
        notifyItemChanged(selectedPosition);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        setSelectedPosition(position);
                        listener.onImageClick(position);
                    }
                }
            });
        }

        public void bind(int imageResId, boolean isSelected) {
            imageView.setImageResource(imageResId);
            if (isSelected) {
                imageView.setBackgroundResource(R.drawable.selected_item_background);
            } else {
                imageView.setBackgroundResource(0);
            }
        }
    }
}
