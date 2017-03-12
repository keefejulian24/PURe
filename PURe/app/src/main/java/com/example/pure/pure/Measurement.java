package com.example.pure.pure;

import java.util.Date;

/**
 * Created by Keefe Julian on 3/12/2017.
 */

public abstract class Measurement {
    private Date time;
    private float index;

    public Date getTime(){
        return time;
    }

    public void setTime(Date time){
        this.time = time;
    }

    public float getIndex() {
        return index;
    }

    public void setIndex(float index){
        this.index = index;
    }
}
