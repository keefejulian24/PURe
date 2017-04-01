package com.example.pure.pure;

import android.os.Bundle;
import android.graphics.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private XYPlot uviPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        uviPlot = (XYPlot) findViewById(R.id.uvi_plot);

        Number[] domainLabels = {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
        Number[] uvIndex = {0, 2, 5, 6, 10, 9, 11, 8, 1, 1, 0};

        XYSeries uviSeries = new SimpleXYSeries(
                Arrays.asList(uvIndex),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                "uv-index"
        );

        Paint lineFill = new Paint();
        lineFill.setAlpha(200);
        lineFill.setShader(new LinearGradient(0, 0, 0, 250, Color.WHITE, Color.GREEN, Shader.TileMode.MIRROR));

        LineAndPointFormatter uviSeriesFormatter = new LineAndPointFormatter();
        uviSeriesFormatter.setFillPaint(lineFill);
        uviSeriesFormatter.setInterpolationParams(
                new CatmullRomInterpolator.Params(20, CatmullRomInterpolator.Type.Centripetal));

        uviPlot.addSeries(uviSeries, uviSeriesFormatter);
        uviPlot.setDomainLabel("time");
        uviPlot.setRangeLabel("uv index");

        uviPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new CustomUVIFormat(domainLabels));
        uviPlot.setRangeBoundaries(0, 13, BoundaryMode.FIXED);
        uviPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, 3);

        final Button buttonUVI = (Button) findViewById(R.id.uvi_level);

        buttonUVI.setOnClickListener(new View.OnClickListener() {
            LinearLayoutCompat uviMainContentLayout = (LinearLayoutCompat) findViewById(R.id.uvi_main_content);
            LinearLayoutCompat.LayoutParams uviMainContentLayoutParams = (LinearLayoutCompat.LayoutParams) uviMainContentLayout.getLayoutParams();
            boolean isChecked = false;
            @Override
            public void onClick(View v) {
                if (!isChecked) {
                    uviMainContentLayoutParams.height = LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
                } else {
                    uviMainContentLayoutParams.height = 0;
                }

                isChecked = !isChecked;
                v.requestLayout();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
