package com.example.pure.pure;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Andrew on 3/18/17.
 */

public class Park extends Location {
    private double walkingDistance;

    public Park(String name, LatLng location) {
        super(name, location);
        this.walkingDistance = calculateDistance(location);
    }

    public double getWalkingDistance() {
        return this.walkingDistance;
    }

    public static double calculateDistance(LatLng location) {
        // Calculate distance from current location
        return 0;
    }
}
