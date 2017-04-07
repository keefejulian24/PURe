package com.example.pure.pure;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Andrew on 3/18/17.
 */

public class MRTStation extends Location {
    private ArrayList<Park> parks;

    public MRTStation(String name, LatLng location, ArrayList<Park> parks) {
        super(name, location);
        this.setParks(parks);
    }

    public ArrayList<Park> getParks() {
        return this.parks;
    }

    public void setParks(ArrayList<Park> parks) {
        this.parks = parks;
        /* If needed: Deep-copy parks to this.parks
        for (Park park : parks)
            this.parks.add(new Park(park.getWalkingDistance()));
        */
    }
}
