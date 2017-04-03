package com.example.pure.pure;

import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.XYSeries;

public class UVIPointLabeler implements PointLabeler {
    public int labeledIndex;
    UVIPointLabeler(int labeledIndex) {
        this.labeledIndex = labeledIndex;
    }
    @Override
    public String getLabel(XYSeries series, int index) {
        return index == this.labeledIndex ? series.getY(index).toString(): "";
    }
}