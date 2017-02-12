package com.example.brendan.mainpackage.event;

/**
 * Created by brendan on 2/9/17.
 */

public class FinishEvent {

    private boolean finished;

    public FinishEvent(boolean finished){
        this.finished = finished;
    }

    public boolean isFinished(){
        return finished;
    }

}
