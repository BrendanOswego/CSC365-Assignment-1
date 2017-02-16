package com.example.brendan.mainpackage.event;

/**
 * Event class for EventBus events
 */

public class FinishEvent {

    private boolean finished;

    public FinishEvent(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

}
