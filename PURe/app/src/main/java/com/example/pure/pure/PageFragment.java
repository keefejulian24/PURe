package com.example.pure.pure;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int pageNumber;
    private int layoutId;

    public static PageFragment newInstance(int page, int layoutId) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment(layoutId);
        fragment.setArguments(args);
        return fragment;
    }

    PageFragment(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*switch (pageNumber) {
            case 0:
                return inflater.inflate(R.layout.mrt_page, container, false);
            case 1:
                return inflater.inflate(R.layout.maps_page, container, false);
            default:
                return null;
        }*/
        return inflater.inflate(this.layoutId, container, false);
    }
}