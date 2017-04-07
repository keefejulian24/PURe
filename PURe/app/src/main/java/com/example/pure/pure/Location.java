package com.example.pure.pure;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Andrew on 3/30/17.
 */

public class Location {
    private String name;
    private LatLng location;

    public Location(String name, LatLng location) {
        this.name = name;
        this.location = new LatLng(location.latitude, location.longitude);
    }

    public String getName() {
        return this.name;
    }

    public LatLng getLocation() {
        return this.location;
    }
}
