package com.example.pure.pure;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MRTPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_MRT_PAGE";

    private int pageNumber;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Toast.makeText(getActivity(), "asdf", Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.cl, container, false);
        //view.setBackground(new ColorDrawable(Color.CYAN));
        return view;
    }
}