package com.example.pure.pure;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.*;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.common.api.Status;

import android.widget.Toast;
import android.support.v7.widget.CardView;

import android.support.v4.app.FragmentTransaction;

import com.astuetz.PagerSlidingTabStrip;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class LocationPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_LOCATION_PAGE";

    private int pageNumber;

    public static LocationPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        LocationPageFragment fragment = new LocationPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Toast.makeText(getActivity(), "asdfasdf", Toast.LENGTH_SHORT).show();
        switch (pageNumber) {
            case 1:
                return inflater.inflate(R.layout.mrt_page, container, false);
            case 0:
                return inflater.inflate(R.layout.maps_page, container, false);
            default:
                return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (pageNumber == 0) {
            /*CREATE PLACES AUTOCOMPLETE*/
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            SupportPlaceAutocompleteFragment autocompleteFragment = new SupportPlaceAutocompleteFragment();

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    Toast.makeText(getActivity(), place.getAddress().toString(), Toast.LENGTH_SHORT).show();

                    ListView parkList = (ListView) getActivity().findViewById(R.id.park_list);
                    ParkListAdapter adapter = (ParkListAdapter) parkList.getAdapter();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }

                    MainActivity.locationLat = place.getLatLng().latitude;
                    MainActivity.locationLng = place.getLatLng().longitude;

                    MainActivity.refreshData(getContext());
                }

                @Override
                public void onError(Status status) {
                    Toast.makeText(getActivity(), status.getStatus().toString(), Toast.LENGTH_SHORT).show();
                }

            });
            Button setToCurrentLocationButton = (Button) getView().findViewById(R.id.set_to_current_location);
            setToCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LocationManager locationManager = (LocationManager)
                            getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                0);
                    }
                    else {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                               // ?
                            }

                            @Override
                            public void onStatusChanged(String s, int i, Bundle bundle) {
                                // ?
                            }

                            @Override
                            public void onProviderEnabled(String s) {
                                // ?
                            }

                            @Override
                            public void onProviderDisabled(String s) {
                                // ?
                            }
                        });
                        ListView parkList = (ListView) getActivity().findViewById(R.id.park_list);
                        ParkListAdapter adapter = (ParkListAdapter) parkList.getAdapter();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                        Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        MainActivity.locationLat = currentLocation.getLatitude();
                        MainActivity.locationLng = currentLocation.getLongitude();
                        MainActivity.refreshData(getContext());
                    }
                }
            });

            transaction.replace(R.id.place_autocomplete_fragment_nest, autocompleteFragment).commit();
        }
        else if (pageNumber == 1) {
            /*CREATE MRT PAGER*/
            ViewPager mrtPager = (ViewPager) getView().findViewById(R.id.mrt_pager);
            mrtPager.setAdapter(new MRTFragmentPagerAdapter(getFragmentManager()));
            final PagerSlidingTabStrip mrtTabsStrip = (PagerSlidingTabStrip) getView().findViewById(R.id.mrt_tab);
            mrtTabsStrip.setViewPager(mrtPager);
            mrtTabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    mrtTabsStrip.setIndicatorColor(getNewColor(position, positionOffset));
                }

                @Override
                public void onPageSelected(int position) {
                    mrtTabsStrip.setIndicatorColor(MainActivity.mrtTabsColor[position]);

                    //Toast.makeText(getActivity(), "asdf", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }

                private int getNewColor(int position, float offset) {
                    int next;
                    if (offset > 0) next = (position < 5) ? position + 1 : position;
                    else next = (position > 0) ? position - 1 : 0;
                    return blendColors(MainActivity.mrtTabsColor[next], MainActivity.mrtTabsColor[position], offset);
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
                ((TextView) ((LinearLayout) mrtTabsStrip.getChildAt(0)).getChildAt(i)).setTextColor(MainActivity.mrtTabsColor[i]);
        }
    }
}