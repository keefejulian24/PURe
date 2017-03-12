package com.example.pure.pure;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Keefe Julian on 3/12/2017.
 */

public abstract class MeasurementManager {
    private ArrayList<Measurement> listMeasurement;

    public ArrayList<Measurement> getListMeasurement() {
        return listMeasurement;
    }

    public void clearListPSI() {
        listMeasurement.clear();
    }

    public abstract void requestData(Location location, Date start, Date end)

    public abstract Measurement getRecentData()

    public void updateToMain() {

    }
}
