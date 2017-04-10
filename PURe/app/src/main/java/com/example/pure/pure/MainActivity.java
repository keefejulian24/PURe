package com.example.pure.pure;

import android.os.Bundle;
import android.graphics.*;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.ui.SeriesBundle;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;
import com.androidplot.Region;
import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.maps.model.LatLng;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private XYPlot uviPlot;
    private XYPlot psiPlot;
    private XYPlot pm25Plot;
    public Number[] domainLabels = {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
    public Number[] uviList = {0, 2, 5, 6, 10, 9, 11, 8, 1, 1, 0};
    public Number[] psiList = {55, 54, 54, 55, 55, 55, 55, 56, 56, 57, 57};
    public Number[] pm25List = {16, 11, 4, 38, 30, 9, 9, 43, 30, 21, 21};
    public LineAndPointFormatter uviSeriesFormatter = null;
    public LineAndPointFormatter psiSeriesFormatter = null;
    public LineAndPointFormatter pm25SeriesFormatter = null;
    public CustomPointLabeler uviPointLabeler = null;
    public CustomPointLabeler psiPointLabeler = null;
    public CustomPointLabeler pm25PointLabeler = null;

    public static double locationLat;
    public static double locationLng;

    public static int[] mrtTabsColor = new int[]{
            Color.RED,
            Color.rgb(126, 87, 194),
            Color.GREEN,
            Color.rgb(255, 196, 0),
            Color.rgb(141, 110, 99)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*CREATE UVI PLOT*/
        uviPlot = (XYPlot) findViewById(R.id.uvi_plot);
        uviSeriesFormatter = new LineAndPointFormatter();
        uviPointLabeler = new CustomPointLabeler(-1);
        createPlot(uviPlot, uviSeriesFormatter, uviPointLabeler, domainLabels, uviList, "time", "uv-index");

        ((CardView) findViewById(R.id.weather_card))/*uviPlot*/.setOnTouchListener(new PlotTouchListener(
                uviPlot,
                uviPointLabeler
        ));

        /*CREATE PSI PLOT*/
        psiPlot = (XYPlot) findViewById(R.id.psi_plot);
        psiSeriesFormatter = new LineAndPointFormatter();
        psiPointLabeler = new CustomPointLabeler(-1);
        createPlot(psiPlot, psiSeriesFormatter, psiPointLabeler, domainLabels, psiList, "time", "psi");
        ((CardView) findViewById(R.id.psi_card)).setOnTouchListener(new PlotTouchListener(
                psiPlot,
                psiPointLabeler
        ));

        /*CREATE PM25 PLOT*/
        pm25Plot = (XYPlot) findViewById(R.id.pm25_plot);
        pm25SeriesFormatter = new LineAndPointFormatter();
        pm25PointLabeler = new CustomPointLabeler(-1);
        createPlot(pm25Plot, pm25SeriesFormatter, pm25PointLabeler, domainLabels, pm25List, "time", "pm 2.5 index");
        ((CardView) findViewById(R.id.pm25_card)).setOnTouchListener(new PlotTouchListener(
                pm25Plot,
                pm25PointLabeler
        ));

        /*CREATE UVI BUTTON*/
        final Button buttonUVI = (Button) findViewById(R.id.uvi_level);
        buttonUVI.setText("UVI: " + uviList[uviList.length >> 1]);
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

        /*CREATE PSI BUTTON*/
        final Button buttonPSI = (Button) findViewById(R.id.psi_level);
        buttonPSI.setText("PSI: " + psiList[psiList.length >> 1]);
        buttonPSI.setOnClickListener(new View.OnClickListener() {
            LinearLayoutCompat psiMainContentLayout = (LinearLayoutCompat) findViewById(R.id.psi_main_content);
            LinearLayoutCompat.LayoutParams psiMainContentLayoutParams = (LinearLayoutCompat.LayoutParams) psiMainContentLayout.getLayoutParams();
            boolean isChecked = false;
            @Override
            public void onClick(View v) {
                if (!isChecked) {
                    psiMainContentLayoutParams.height = LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
                } else {
                    psiMainContentLayoutParams.height = 0;
                }

                isChecked = !isChecked;
                v.requestLayout();
            }
        });

        /*CREATE PSI BUTTON*/
        final Button buttonPM25 = (Button) findViewById(R.id.pm25_level);
        buttonPM25.setText("PM 2.5: " + pm25List[pm25List.length >> 1]);
        buttonPM25.setOnClickListener(new View.OnClickListener() {
            LinearLayoutCompat pm25MainContentLayout = (LinearLayoutCompat) findViewById(R.id.pm25_main_content);
            LinearLayoutCompat.LayoutParams pm25MainContentLayoutParams = (LinearLayoutCompat.LayoutParams) pm25MainContentLayout.getLayoutParams();
            boolean isChecked = false;
            @Override
            public void onClick(View v) {
                if (!isChecked) {
                    pm25MainContentLayoutParams.height = LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
                } else {
                    pm25MainContentLayoutParams.height = 0;
                }

                isChecked = !isChecked;
                v.requestLayout();
            }
        });

        /*CREATE LOCATION PAGER*/
        ViewPager locationPager = (ViewPager) findViewById(R.id.location_pager);
        locationPager.setAdapter(new LocationFragmentPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip locationTabsStrip = (PagerSlidingTabStrip) findViewById(R.id.location_tab);
        locationTabsStrip.setViewPager(locationPager);
        locationTabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 1) return;
                /*CREATE MRT PAGER*/
                ViewPager mrtPager = (ViewPager) findViewById(R.id.mrt_pager);
                mrtPager.setAdapter(new MRTFragmentPagerAdapter(getSupportFragmentManager()));
                final PagerSlidingTabStrip mrtTabsStrip = (PagerSlidingTabStrip) findViewById(R.id.mrt_tab);
                mrtTabsStrip.setViewPager(mrtPager);
                mrtTabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        mrtTabsStrip.setIndicatorColor(getNewColor(position, positionOffset));
                    }

                    @Override
                    public void onPageSelected(int position) {
                        mrtTabsStrip.setIndicatorColor(mrtTabsColor[position]);

                        //Toast.makeText(getApplicationContext(), mrtTabsStrip.getIndicatorColor(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }

                    private int getNewColor(int position, float offset) {
                        int next;
                        if (offset > 0) next = (position < 5) ? position + 1 : position;
                        else next = (position > 0) ? position - 1 : 0;
                        return blendColors(mrtTabsColor[next], mrtTabsColor[position], offset);
                    }

                    private int blendColors(int color1, int color2, float ratio) {
                        final float inverseRation = 1f - ratio;
                        float a = (Color.alpha(color1) * ratio) + (Color.alpha(color2) * inverseRation);
                        float r = (Color.red  (color1) * ratio) + (Color.red  (color2) * inverseRation);
                        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
                        float b = (Color.blue (color1) * ratio) + (Color.blue (color2) * inverseRation);
                        return Color.argb((int) a, (int) r, (int) g, (int) b);
                    }

                });

                for (int i = 0; i < 5; i++)
                    ((TextView) ((LinearLayout) mrtTabsStrip.getChildAt(0)).getChildAt(i)).setTextColor(mrtTabsColor[i]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0; i < 2; i++)
            ((TextView) ((LinearLayout) locationTabsStrip.getChildAt(0)).getChildAt(i)).setTextColor(Color.WHITE);
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

    private void createPlot(XYPlot plot, LineAndPointFormatter seriesFormatter, CustomPointLabeler pointLabeler,
                            Number[] domainLabels, Number[] indexList, String domainLabel, String rangeLabel) {
        XYSeries series = new SimpleXYSeries(
                Arrays.asList(indexList),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                rangeLabel
        );

        Paint lineFill = new Paint();
        lineFill.setAlpha(200);
        lineFill.setColor(Color.YELLOW);
        //lineFill.setShader(new LinearGradient(0, 250, 0, 0, Color.WHITE, Color.YELLOW, Shader.TileMode.REPEAT));

        seriesFormatter.setFillPaint(lineFill);
        seriesFormatter.setInterpolationParams(
                new CatmullRomInterpolator.Params(20, CatmullRomInterpolator.Type.Centripetal));
        seriesFormatter.setPointLabeler(pointLabeler);
        seriesFormatter.setPointLabelFormatter(new PointLabelFormatter(Color.WHITE));

        Integer mins = (Integer) indexList[0];
        Integer maxs = (Integer) indexList[0];
        for (int i = 1; i < indexList.length; i++) {
            if ((Integer) indexList[i] > maxs) maxs = (Integer) indexList[i];
            if ((Integer) indexList[i] < mins) mins = (Integer) indexList[i];
        }

        int length = maxs - mins;
        if (length / 7 > 2) {
            maxs += length / 7;
            mins -= length / 7;
        } else {
            maxs += 2;
            mins -= 2;
        }
        if (mins < 0) mins = 0;

        plot.addSeries(series, seriesFormatter);
        plot.setDomainLabel(domainLabel);
        plot.setRangeLabel(rangeLabel);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new CustomFormat(domainLabels));
        plot.setRangeBoundaries(mins, maxs, BoundaryMode.FIXED);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).getPaint().setColor(Color.TRANSPARENT);
        plot.getGraph().getDomainGridLinePaint().setColor(Color.TRANSPARENT);
        plot.getGraph().getRangeGridLinePaint().setColor(Color.TRANSPARENT);
    }

    public void setPointLabeler(PointF point, XYPlot uviPlot, CustomPointLabeler uviPointLabeler) {
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

    public class PlotTouchListener implements View.OnTouchListener {
        public XYPlot plot;
        public CustomPointLabeler pointLabeler;

        PlotTouchListener(XYPlot plot, CustomPointLabeler pointLabeler) {
            this.plot = plot;
            this.pointLabeler = pointLabeler;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    setPointLabeler(new PointF(motionEvent.getX(), motionEvent.getY()), this.plot, this.pointLabeler);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    setPointLabeler(null, this.plot, this.pointLabeler);
                    break;
                case MotionEvent.ACTION_UP:
                    setPointLabeler(null, this.plot, this.pointLabeler);
                    break;
                case MotionEvent.ACTION_MOVE:
                    setPointLabeler(new PointF(motionEvent.getX(), motionEvent.getY()), this.plot, this.pointLabeler);
                    break;
                case MotionEvent.ACTION_HOVER_ENTER:
                    setPointLabeler(new PointF(motionEvent.getX(), motionEvent.getY()), this.plot, this.pointLabeler);
                    break;
                case MotionEvent.ACTION_HOVER_MOVE:
                    setPointLabeler(new PointF(motionEvent.getX(), motionEvent.getY()), this.plot, this.pointLabeler);
                    break;
                case MotionEvent.ACTION_HOVER_EXIT:
                    setPointLabeler(null, this.plot, this.pointLabeler);
                    break;
                default:
                    //Toast.makeText(getApplicationContext(), "DEFAULT", Toast.LENGTH_SHORT).show();
                    setPointLabeler(null, this.plot, this.pointLabeler);
                    break;
            }

            return true;
        }
    }
}
