package com.example.pure.pure;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
}