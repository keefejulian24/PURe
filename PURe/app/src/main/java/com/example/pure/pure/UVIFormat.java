package com.example.pure.pure;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

public class UVIFormat extends Format {
    public Number[] domainLabels = null;

    UVIFormat(Number[] domainLabels) {
        this.domainLabels = domainLabels;
    }

    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        int i = Math.round(((Number) obj).floatValue());
        if (i % 3 == 0)
            return toAppendTo.append(domainLabels[i]);
        else return toAppendTo.append("");
    }
    @Override
    public Object parseObject(String source, ParsePosition pos) {
        return null;
    }
}