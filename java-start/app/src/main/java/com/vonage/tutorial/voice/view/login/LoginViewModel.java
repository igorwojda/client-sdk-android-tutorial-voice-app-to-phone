package com.vonage.tutorial.voice.view.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavDirections;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.request_listener.NexmoConnectionListener.ConnectionStatus;
import com.vonage.tutorial.voice.User;
import com.vonage.tutorial.voice.util.NavManager;
import com.vonage.tutorial.voice.util.Tutorial;

public class LoginViewModel extends ViewModel {

    NavManager navManager = NavManager.getInstance();
    private NexmoClient client = null; // TODO: Retrieve NexmoClient instance"
    private MutableLiveData<ConnectionStatus> connectionStatusMutableLiveData = new MutableLiveData<ConnectionStatus>();
    public LiveData<ConnectionStatus> _connectionStatusLiveData = connectionStatusMutableLiveData;

    private User user;


    public LoginViewModel() {
        Tutorial.todo("Init connection listener");
    }

    private void navigate() {
        String userName = user.getName();

        if (userName == null) {
            throw new RuntimeException("user name is null");
        }

        NavDirections navDirections = LoginFragmentDirections.actionLoginFragmentToVoiceFragment(userName);
        navManager.navigate(navDirections);
    }

    void onLoginUser(User user) {
        Tutorial.todo("Login user");
    }
}
