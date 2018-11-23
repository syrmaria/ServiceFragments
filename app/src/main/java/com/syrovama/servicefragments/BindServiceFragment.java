package com.syrovama.servicefragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class BindServiceFragment extends Fragment {
    public static final String TAG = "MyBindServiceFragment";
    private MainActivity parent;
    private TextView ourResult;
    private int result;
    private boolean isServiceBinded;
    private SomeService.SomeBinder serviceBinder;

    public interface Callbacks {
        void onTimerBindService(int result);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            serviceBinder = (SomeService.SomeBinder)service;
            result = serviceBinder.getNumber();
            isServiceBinded = true;
            Log.d(TAG, "Service connected.");
            showResult();
        }
        public void onServiceDisconnected(ComponentName className) {
            mConnection = null;
            Log.d(TAG, "Service disconnected.");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bind_service, container, false);
        Button goButton = v.findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceBinded) {
                    result = serviceBinder.getNumber();
                    showResult();
                } else {
                    doBindService();
                }
            }
        });
        ourResult = v.findViewById(R.id.ourResultValueTextView2);
        showResult();
        setRetainInstance(true);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent = (MainActivity) getActivity();
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                parent.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parent.onTimerBindService(result);
                    }
                });
                Log.d(TAG, "onTimerStartService called");
            }
        }, 0, 3000);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause called");
        doUnbindService();
        super.onPause();
    }

    void doBindService() {
        if (parent.bindService(new Intent(parent, SomeService.class),
                mConnection, Context.BIND_NOT_FOREGROUND)) {
            Log.d(TAG, "Called to bind service");
        } else {
            Log.e(TAG, "Service doesn't exist");
        }
    }

    private void showResult() {
        ourResult.setText(getString(R.string.result_template, result));
    }

    private void doUnbindService() {
        if (isServiceBinded) {
            parent.unbindService(mConnection);
            isServiceBinded = false;
            Log.d(TAG, "Unbind service");
        } else {
            Log.d(TAG, "No need to unbind service");
        }
    }

    public void updateOtherResultValue(int result) {
        TextView tmp = parent.findViewById(R.id.otherResultValueTextView2);
        tmp.setText(String.format(getString(R.string.result_template),result));
    }
}
