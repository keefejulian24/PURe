package com.example.pure.pure;

import android.os.Bundle;
import android.graphics.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.androidplot.SeriesRegistry;
import com.androidplot.ui.SeriesBundle;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;
import com.androidplot.Region;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private XYPlot uviPlot;
    public Number[] domainLabels = {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
    public Number[] uvIndex = {0, 2, 5, 6, 10, 9, 11, 8, 1, 1, 0};
    public LineAndPointFormatter uviSeriesFormatter = null;
    public UVIPointLabeler uviPointLabeler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        uviPlot = (XYPlot) findViewById(R.id.uvi_plot);
        uviSeriesFormatter = new LineAndPointFormatter();
        uviPointLabeler = new UVIPointLabeler(-1);
        XYSeries uviSeries = new SimpleXYSeries(
                Arrays.asList(uvIndex),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                "uv-index"
        );

        Paint lineFill = new Paint();
        lineFill.setAlpha(200);
        lineFill.setColor(Color.YELLOW);
        //lineFill.setShader(new LinearGradient(0, 250, 0, 0, Color.WHITE, Color.YELLOW, Shader.TileMode.REPEAT));

        uviSeriesFormatter.setFillPaint(lineFill);
        uviSeriesFormatter.setInterpolationParams(
                new CatmullRomInterpolator.Params(20, CatmullRomInterpolator.Type.Centripetal));
        uviSeriesFormatter.setPointLabeler(uviPointLabeler);
        uviSeriesFormatter.setPointLabelFormatter(new PointLabelFormatter(Color.WHITE));

        uviPlot.addSeries(uviSeries, uviSeriesFormatter);
        uviPlot.setDomainLabel("time");
        uviPlot.setRangeLabel("uv index");
        uviPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new UVIFormat(domainLabels));
        uviPlot.setRangeBoundaries(0, 13, BoundaryMode.FIXED);
        uviPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).getPaint().setColor(Color.TRANSPARENT);
        uviPlot.getGraph().getDomainGridLinePaint().setColor(Color.TRANSPARENT);
        uviPlot.getGraph().getRangeGridLinePaint().setColor(Color.TRANSPARENT);

        ((CardView) findViewById(R.id.weather_card))/*uviPlot*/.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        setUVIPointLabeler(new PointF(motionEvent.getX(), motionEvent.getY()));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        setUVIPointLabeler(null);
                        break;
                    case MotionEvent.ACTION_UP:
                        setUVIPointLabeler(null);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        setUVIPointLabeler(new PointF(motionEvent.getX(), motionEvent.getY()));
                        break;
                    case MotionEvent.ACTION_HOVER_ENTER:
                        setUVIPointLabeler(new PointF(motionEvent.getX(), motionEvent.getY()));
                        break;
                    case MotionEvent.ACTION_HOVER_MOVE:
                        setUVIPointLabeler(new PointF(motionEvent.getX(), motionEvent.getY()));
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:
                        setUVIPointLabeler(null);
                        break;
                    default:
                        //Toast.makeText(getApplicationContext(), "DEFAULT", Toast.LENGTH_SHORT).show();
                        setUVIPointLabeler(null);
                        break;
                }

                return true;
            }
        });

        final Button buttonUVI = (Button) findViewById(R.id.uvi_level);
        buttonUVI.setText("UVI: " + uvIndex[uvIndex.length >> 1]);

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

    void setUVIPointLabeler(PointF point) {
        if (point == null/* || !uviPlot.containsPoint(point.x, point.y)*/) {
            uviPointLabeler.labeledIndex = -1;
        } else {
            Number x = uviPlot.getXVal(point);
            double distanceX = 0;
            int selectedIndex = -1;

            for (SeriesBundle<XYSeries, ? extends XYSeriesFormatter> sfPair : uviPlot
                    .getRegistry().getSeriesAndFormatterList()) {

                XYSeries series = sfPair.getSeries();
                for (int i = 0; i < series.size(); i++) {
                    Number currentX = series.getX(i);
                    Number currentY = series.getY(i);

                    if (currentX != null && currentY != null) {
                        double currentDistanceX = Region.measure(x, currentX).doubleValue();

                        if (selectedIndex == -1 || distanceX > currentDistanceX) {
                            selectedIndex = i;
                            distanceX = currentDistanceX;
                        }
                    }
                }
            }

            uviPointLabeler.labeledIndex = selectedIndex;
        }

        uviPlot.redraw();
    }
}
