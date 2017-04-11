package com.example.pure.pure;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DownloadManager extends AsyncTask<String, Void, String> {
    private AppCompatActivity activity;

    DownloadManager(AppCompatActivity activity) {
        this.activity = activity;
    }

    protected String doInBackground(String... urlToRead) {
        StringBuilder result = new StringBuilder();
        URL url = null;
        try {
            url = new URL(urlToRead[0]);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = null;
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), "error found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return result.toString();
    }

    protected void onPostExecute(String result) {
        Log.d("RESULT", result);
        Toast.makeText(activity.getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        // convert json
        try{
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JSONObject resultJSON = new JSONObject(result);
            JSONArray dataJSON = resultJSON.getJSONArray("data");
            String type = resultJSON.getString("type");
            Log.d("RESULT LEN", "" + dataJSON.length());

            if (type.equals("UVI")) {
                for (int i = 0; i < MainActivity.uviDomainLabels.length; i++) {
                    JSONObject data = dataJSON.getJSONObject(i);
                    MainActivity.uviDomainLabels[i] = formatter.parse(data.getString("date")).getHours();
                    MainActivity.uviList[i] = Integer.parseInt(data.getString("value"));
                }

                MainActivity.createPlot(MainActivity.uviPlot, MainActivity.uviSeriesFormatter,
                        MainActivity.uviPointLabeler, MainActivity.uviDomainLabels, MainActivity.uviList, "time", "uv-index");

                ((Button) activity.findViewById(R.id.uvi_level)).setText("UVI: " + MainActivity.uviList[MainActivity.uviList.length >> 1]);
            } else if (type.equals("PSI")) {
                for (int i = 0; i < MainActivity.psiDomainLabels.length; i++) {
                    JSONObject data = dataJSON.getJSONObject(i);
                    MainActivity.psiDomainLabels[i] = formatter.parse(data.getString("date")).getHours();
                    MainActivity.psiList[i] = Integer.parseInt(data.getString("value"));
                }

                MainActivity.createPlot(MainActivity.psiPlot, MainActivity.psiSeriesFormatter,
                        MainActivity.psiPointLabeler, MainActivity.psiDomainLabels, MainActivity.psiList, "time", "psi");

                ((Button) activity.findViewById(R.id.psi_level)).setText("PSI: " + MainActivity.psiList[MainActivity.psiList.length >> 1]);
            } else if (type.equals("PM25")) {
                for (int i = 0; i < MainActivity.pm25DomainLabels.length; i++) {
                    JSONObject data = dataJSON.getJSONObject(i);
                    MainActivity.pm25DomainLabels[i] = formatter.parse(data.getString("date")).getHours();
                    MainActivity.pm25List[i] = Integer.parseInt(data.getString("value"));
                }

                MainActivity.createPlot(MainActivity.pm25Plot, MainActivity.pm25SeriesFormatter,
                        MainActivity.pm25PointLabeler, MainActivity.pm25DomainLabels, MainActivity.pm25List, "time", "pm 2.5 index");

                ((Button) activity.findViewById(R.id.pm25_level)).setText("PM 2.5: " + MainActivity.pm25List[MainActivity.pm25List.length >> 1]);
            } else {
                Toast.makeText(activity.getApplicationContext(), "Something was wrong with the server:(", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), "Something was wrong :(", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}