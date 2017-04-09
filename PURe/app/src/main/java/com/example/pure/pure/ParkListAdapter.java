package com.example.pure.pure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Andrew on 3/29/17.
 */

public class ParkListAdapter extends ArrayAdapter<Park> {
    public ParkListAdapter(Context context, ArrayList<Park> parks) {
        super(context, 0, parks);
    }
    private int selectedItem;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Park park = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.park_item, parent, false);
        }
        // Lookup view for data population
        TextView parkName = (TextView) convertView.findViewById(R.id.park_name);
        TextView parkWalkingDistance = (TextView) convertView.findViewById(R.id.park_walking_distance);
        // Populate the data into the template view using the data object
        parkName.setText(park.getName());
        parkWalkingDistance.setText("Distance: " + Double.toString(getWalkingDistance()));
        // Return the completed view to render on screen
        return convertView;
    }

    public double getWalkingDistance() {
        return 0;
    }
}
