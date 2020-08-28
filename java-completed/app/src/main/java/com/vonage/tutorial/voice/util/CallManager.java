package com.vonage.tutorial.voice.util;

import com.nexmo.client.NexmoCall;

public final class CallManager {

    private static CallManager INSTANCE;
    private static NexmoCall onGoingCall;

    public static CallManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CallManager();
        }

        return INSTANCE;
    }

    public NexmoCall getOnGoingCall() {
        return onGoingCall;
    }

    public void setOnGoingCall(NexmoCall onGoingCall) {
        CallManager.onGoingCall = onGoingCall;
    }
}