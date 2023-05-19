package com.example.notgreenthumb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notgreenthumb.R;

public class DialogBuilderAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final int[] images;

    public DialogBuilderAdapter(Context context, String[] values, int[] images) {
        super(context, R.layout.custom_list_item, values);
        this.context = context;
        this.values = values;
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_list_item, parent, false);
        TextView textView = rowView.findViewById(R.id.text);
        ImageView imageView = rowView.findViewById(R.id.image);
        textView.setText(values[position]);
        imageView.setImageResource(images[position]);
        return rowView;
    }
}
