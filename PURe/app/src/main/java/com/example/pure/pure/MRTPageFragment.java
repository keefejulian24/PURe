package com.example.pure.pure;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MRTPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_MRT_PAGE";

    private int pageNumber;

    private Gson gson;
    private MRTManager mrtManager;

    public static MRTPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MRTPageFragment fragment = new MRTPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARG_PAGE);
        gson = new Gson();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("mrtManager.json")));
            mrtManager = gson.fromJson(bufferedReader, MRTManager.class);
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Toast.makeText(getActivity(), "asdf", Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.mrt_and_park, container, false);
        //view.setBackground(new ColorDrawable(Color.CYAN));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DiscreteSeekBar mrtSeekBar = (DiscreteSeekBar) getView().findViewById(R.id.mrt_seek_bar);
        mrtSeekBar.setScrubberColor(MainActivity.mrtTabsColor[pageNumber]);
        mrtSeekBar.setThumbColor(MainActivity.mrtTabsColor[pageNumber], 0);
        mrtSeekBar.setTrackColor(MainActivity.mrtTabsColor[pageNumber]);
        mrtSeekBar.setProgress(0);
        mrtSeekBar.setMax(mrtManager.getMrtLines().get(pageNumber).getMrtStations().size() - 1);
        TextView mrtName = (TextView) getView().findViewById(R.id.mrt_name);
        mrtName.setText("Select MRT Station");
        mrtSeekBar.setIndicatorPopupEnabled(false);
        mrtSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                TextView mrtName = (TextView) getView().findViewById(R.id.mrt_name);
                mrtName.setText(mrtManager.getMrtLines().get(pageNumber).getMrtStations().get(seekBar.getProgress()).getName());
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                TextView mrtName = (TextView) getView().findViewById(R.id.mrt_name);
                mrtName.setText(mrtManager.getMrtLines().get(pageNumber).getMrtStations().get(seekBar.getProgress()).getName());
                TextView headerPark = (TextView) getView().findViewById(R.id.header_park);
                headerPark.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                TextView headerPark = (TextView) getView().findViewById(R.id.header_park);
                headerPark.setVisibility(View.VISIBLE);
                ListView parkList = (ListView) getView().findViewById(R.id.park_list);
                ArrayList<Park> parks = mrtManager.getMrtLines().get(pageNumber).getMrtStations().get(seekBar.getProgress()).getParks();
                parkList.setItemChecked(-1, true);
                if (parks != null) {
                    parkList.setVisibility(View.VISIBLE);
                } else {
                    parkList.setVisibility(View.INVISIBLE);
                    parks = new ArrayList<Park>();
                }

                final ParkListAdapter adapter = new ParkListAdapter(getActivity().getBaseContext(), parks);
                parkList.setAdapter(adapter);
                parkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Park selectedPark = (Park) adapterView.getItemAtPosition(i);
                        //Toast.makeText(getActivity().getApplicationContext(), Double.toString(selectedPark.getLocation().latitude) + ", " + Double.toString(selectedPark.getLocation().longitude), Toast.LENGTH_SHORT).show();
                        ListView pList = (ListView) adapterView;
                        pList.setItemChecked(i, true);
                    }
                });
            }
        });

        Button setLocation = (Button) getView().findViewById(R.id.set_location);
        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView parkList = (ListView) getView().findViewById(R.id.park_list);
                ParkListAdapter adapter = (ParkListAdapter) parkList.getAdapter();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                DiscreteSeekBar mrtSeekBar = (DiscreteSeekBar) getView().findViewById(R.id.mrt_seek_bar);
                if (parkList.getCheckedItemPosition() != -1) {
                    MainActivity.locationLat = mrtManager.getMrtLines().get(pageNumber).getMrtStations().get(mrtSeekBar.getProgress()).getParks().get(parkList.getCheckedItemPosition()).getLocation().latitude;
                    MainActivity.locationLng = mrtManager.getMrtLines().get(pageNumber).getMrtStations().get(mrtSeekBar.getProgress()).getParks().get(parkList.getCheckedItemPosition()).getLocation().longitude;
                }
                else {
                    MainActivity.locationLat = mrtManager.getMrtLines().get(pageNumber).getMrtStations().get(mrtSeekBar.getProgress()).getLocation().latitude;
                    MainActivity.locationLng = mrtManager.getMrtLines().get(pageNumber).getMrtStations().get(mrtSeekBar.getProgress()).getLocation().longitude;
                }
                MainActivity.refreshData(getContext());
            }
        });
    }
}