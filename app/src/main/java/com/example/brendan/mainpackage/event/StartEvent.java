package com.example.brendan.mainpackage.event;

/**
 * Created by brendan on 2/8/17.
 */

public class StartEvent {

    String time;

    public StartEvent(String time){
        this.time = time;
    }
    public String getTime(){
        return time;
    }
}
