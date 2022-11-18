package com.f0x1d.epos.utils;

public class Event {

    private boolean mConsumed;
    private final String mType;
    private final Object mData;

    public Event(String type, Object data) {
        mType = type;
        mData = data;
    }

    public Event(String type) {
        mType = type;
        mData = null;
    }

    public String type() {
        if (mData == null) {
            mConsumed = true;
        }
        return mType;
    }

    public <T> T consume() {
        if (mConsumed)
            return null;

        mConsumed = true;
        return (T) mData;
    }

    public boolean isConsumed() {
        return mConsumed;
    }
}