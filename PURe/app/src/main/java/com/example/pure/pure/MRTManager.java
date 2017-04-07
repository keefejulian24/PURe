package com.example.pure.pure;

import java.util.ArrayList;

/**
 * Created by Andrew on 3/18/17.
 */

public class MRTManager {
    ArrayList<MRTLine> mrtLines;
    MRTLine selectedLine;

    public MRTManager() {
        this.mrtLines = null;
    }

    public MRTManager(ArrayList<MRTLine> mrtLines) {
        this.setMrtLines(mrtLines);
    }

    public ArrayList<MRTLine> getMrtLines() {
        return this.mrtLines;
    }

    public void setMrtLines(ArrayList<MRTLine> mrtLines) {
        this.mrtLines = mrtLines;
        /* If needed: Deep-copy mrtLines to this.mrtLines
        for (MRTLine mrtLine : mrtLines)
            this.mrtLines.add(new MRTLine(mrtLine.getColor(), mrtLine.getMrtStations()));
        */
    }

    public ArrayList<MRTStation> getStationsByLine(String name) {
        for (MRTLine mrtLine : this.mrtLines)
            if (mrtLine.getName().equals(name)) {
                selectedLine = mrtLine;
                return selectedLine.getMrtStations();
            }
        return null;
    }

    public ArrayList<Park> getParksByStation(String name) {
        for (MRTStation mrtStation : selectedLine.getMrtStations())
            if (mrtStation.getName().equals(name))
                return mrtStation.getParks();
        return null;
    }
}
