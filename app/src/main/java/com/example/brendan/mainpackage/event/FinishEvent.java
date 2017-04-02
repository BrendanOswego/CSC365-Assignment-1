package com.example.brendan.mainpackage.event;

import java.util.UUID;

/**
 * Event class for EventBus events
 */

public class FinishEvent {
    private static final int TAVG = 0;
    private static final int PRECIP = 1;
    boolean finished;
    UUID uuid;
    String setType;
    boolean cached;

    public FinishEvent(boolean finished, UUID uuid,String setType,boolean cached) {
        this.finished = finished;
        this.uuid =  uuid;
        this.setType = setType;
        this.cached = cached;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getSetType() {
        return setType;
    }

    public void setSetType(String setType) {
        this.setType = setType;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }
}
