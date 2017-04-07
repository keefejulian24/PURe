package com.example.pure.pure;

import java.util.ArrayList;

/**
 * Created by Andrew on 3/18/17.
 */

public class MRTLine {
    String name;
    String color;
    ArrayList<MRTStation> mrtStations;

    public MRTLine(String name, String color, ArrayList<MRTStation> mrtStations) {
        this.name = name;
        this.color = color;
        this.setMrtStations(mrtStations);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ArrayList<MRTStation> getMrtStations() {
        return this.mrtStations;
    }

    public void setMrtStations(ArrayList<MRTStation> mrtStations) {
        this.mrtStations = mrtStations;
        /* If needed: Deep-copy mrtStations to this.mrtStations
        for (MRTStation mrtStation : mrtStations)
            this.mrtStations.add(new MRTStation(mrtStation.getParks()));
        */
    }
}
