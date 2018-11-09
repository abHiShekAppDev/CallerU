package com.developer.abhishek.calleru.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.developer.abhishek.calleru.models.CallLogs;
import com.developer.abhishek.calleru.repository.CallLogsRepo;

import java.util.List;

public class CallLogVM extends AndroidViewModel {

    private final LiveData<List<CallLogs>> callLogsLiveData;

    public CallLogVM(@NonNull Application application) {
        super(application);

        CallLogsRepo callLogsRepo = new CallLogsRepo();
        callLogsLiveData = callLogsRepo.getCallLogsMutableLiveData(application.getApplicationContext());
    }

    public LiveData<List<CallLogs>> getCallLogs() {
        return callLogsLiveData;
    }
}
