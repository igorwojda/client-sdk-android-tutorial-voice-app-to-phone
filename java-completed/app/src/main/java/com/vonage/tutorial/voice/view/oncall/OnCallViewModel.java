package com.vonage.tutorial.voice.view.oncall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.nexmo.client.*;
import com.nexmo.client.request_listener.NexmoApiError;
import com.nexmo.client.request_listener.NexmoRequestListener;
import com.vonage.tutorial.R;
import com.vonage.tutorial.voice.util.CallManager;
import com.vonage.tutorial.voice.util.NavManager;
import timber.log.Timber;

public class OnCallViewModel extends ViewModel {
    private CallManager callManager = CallManager.getInstance();
    private NavManager navManager = NavManager.getInstance();

    private MutableLiveData<String> toastMutableLiveData = new MutableLiveData<>();
    LiveData<String> toastLiveData = toastMutableLiveData;
    private NexmoCallEventListener callEventListener = new NexmoCallEventListener() {
        @Override
        public void onMemberStatusUpdated(NexmoCallMemberStatus callMemberStatus, NexmoCallMember callMember) {
            Timber.d("CallEventListener.onMemberStatusUpdated: %s : %s", callMember.getUser().getName(), callMemberStatus);

            if (callMemberStatus == NexmoCallMemberStatus.COMPLETED || callMemberStatus == NexmoCallMemberStatus.CANCELED) {
                callManager.setOnGoingCall(null);
                navManager.popBackStack(R.id.mainFragment, false);
            }
        }

        @Override
        public void onMuteChanged(NexmoMediaActionState mediaActionState, NexmoCallMember callMember) {
            Timber.d("CallEventListener.onMuteChanged: %s : %s", callMember.getUser().getName(), mediaActionState);
        }

        @Override
        public void onEarmuffChanged(NexmoMediaActionState mediaActionState, NexmoCallMember callMember) {
            Timber.d("CallEventListener.onEarmuffChanged: %s : %s", callMember.getUser().getName(), mediaActionState);
        }

        @Override
        public void onDTMF(String dtmf, NexmoCallMember callMember) {
            Timber.d("CallEventListener.onDTMF: %s : %s", callMember.getUser().getName(), dtmf);
        }
    };

    public OnCallViewModel() {
        NexmoCall onGoingCall;

        if (callManager.getOnGoingCall() == null) {
            throw new RuntimeException("Call is null");
        } else {
            onGoingCall = callManager.getOnGoingCall();
        }

        onGoingCall.addCallEventListener(callEventListener);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        NexmoCall ongoingCall = callManager.getOnGoingCall();

        if (ongoingCall != null) {
            ongoingCall.removeCallEventListener(callEventListener);
        }
    }

    public void onBackPressed() {
        hangupInternal();
    }

    public void hangup() {
        hangupInternal();
    }

    private void hangupInternal() {
        NexmoCall ongoingCall = callManager.getOnGoingCall();

        if (ongoingCall != null) {
            ongoingCall.hangup(new NexmoRequestListener<NexmoCall>() {
                @Override
                public void onSuccess(@Nullable NexmoCall call) {
                    callManager.setOnGoingCall(null);
                }

                @Override
                public void onError(@NonNull NexmoApiError apiError) {
                    toastMutableLiveData.postValue(apiError.getMessage());
                }
            });
        }

    }
}
