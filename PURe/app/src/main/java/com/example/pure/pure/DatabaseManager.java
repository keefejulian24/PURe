package com.example.pure.pure;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class DatabaseManager {
    public static int UVI = 0;
    public static int PSI = 1;
    public static int PM25 = 2;
    public static int SOUTH = 0;
    public static int NORTH = 1;
    public static int CENTRAL = 2;
    public static int WEST = 3;
    public static int EAST = 4;
    public static int NATIONAL = 5;
    private static String[] tableName = new String[]{"UVI", "PSI", "PM25"};
    private static String[] regionName = new String[]{"south", "north", "central", "west", "east", "national"};
    private static double[] latitude = new double[] {1.29587, 1.41803, 1.35735, 1.35735, 1.35735, 0};
    private static double[] longitude = new double[] {103.82, 103.82, 103.82, 103.7, 103.94, 0};

    private AppCompatActivity activity;

    DatabaseManager(AppCompatActivity activity) {
        this.activity = activity;
    }

    private String dateToString(Date date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
        return formatter.format(date);
    }

    public int getNearestRegion(double lat, double lon) {
        double dist = Haversine.haversine(latitude[5], longitude[5], lat, lon);
        int ret = 5;
        for (int i = 0; i < 5; i++) {
            double currentDist = Haversine.haversine(latitude[i], longitude[i], lat, lon);
            if (currentDist < dist) {
                dist = currentDist;
                ret = i;
            }
        }
        return ret;
    }

    public void query(Date startDate, Date endDate, int table, int region) throws Exception{
        if (table < 0 || table > 2) throw new DatabaseManagerException("wrong table value");
        if (region < 0 || region > 5) throw new DatabaseManagerException("wrong region value");

        String targetURL = "http://10.27.121.166/pure/?type=" +
                tableName[table] + "&region=" + regionName[region] + "&start=" +
                dateToString(startDate) + "&end=" + dateToString(endDate);

        Toast.makeText(activity.getApplicationContext(), targetURL, Toast.LENGTH_SHORT).show();
        new DownloadManager(activity).execute(targetURL);
    }

    public class DatabaseManagerException extends Exception {
        public DatabaseManagerException(String msg) {
            super(msg);
        }
    }
}