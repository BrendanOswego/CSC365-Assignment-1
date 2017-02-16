package com.example.brendan.mainpackage.event;

/**
 * Event class for EventBus events
 */

public class StartEvent {

    private String time;

    public StartEvent(String time){
        this.time = time;
    }
    public String getTime(){
        return time;
    }
}
