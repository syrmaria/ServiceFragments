package com.syrovama.servicefragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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

public class StartServiceFragment extends Fragment {
    private static final String TAG = "MyStartServiceFragment";

    private MainActivity parent;
    private TextView ourResult;
    private TextView otherResult;
    private Button goButton;
    private NumberReceiver numberReceiver;
    private int result;

    public interface Callbacks {
        void onTimerStartService(int result);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_start_service, container, false);
        goButton = v.findViewById(R.id.goButton);
        otherResult = v.findViewById(R.id.otherResultValueTextView);
        ourResult = v.findViewById(R.id.ourResultValueTextView);
        updateResultValue(result);
        setRetainInstance(true);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        parent = (MainActivity) getActivity();
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                parent.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parent.onTimerStartService(result);
                    }
                });
                Log.d(TAG, "onTimerStartService called");
            }
        }, 0, 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStartService();
            }
        });
        numberReceiver = new NumberReceiver(new Handler());
        IntentFilter intentFilter = new IntentFilter("myaction");
        parent.registerReceiver(numberReceiver, intentFilter);
        Log.d(TAG, "onResume - register receiver");
    }

    private void doStartService() {
        parent.startService(SomeService.newIntent(parent));
        Log.d(TAG, "Started service");
    }

    private void updateResultValue(int value) {
        ourResult.setText(String.format(getString(R.string.result_template), value));
        Log.d(TAG, "Result view updated");
    }
    public void updateOtherResultValue(int value) {
        otherResult.setText(String.format(getString(R.string.result_template), value));
        Log.d(TAG, "Result view updated");
    }

    @Override
    public void onPause() {
        parent.stopService(SomeService.newIntent(parent));
        parent.unregisterReceiver(numberReceiver);
        Log.d(TAG, "onPause - stopped service");
        super.onPause();
    }

    public class NumberReceiver extends BroadcastReceiver {
        private final Handler handler;

        public NumberReceiver(Handler handler) {
            this.handler = handler;
            Log.d(TAG, "Receiver: created");
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            final int number = intent.getIntExtra(SomeService.START_RESULT, 0);
            Log.d(TAG, "Receiver: got state, post to main activity");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    result = number;
                    updateResultValue(number);
                }
            });
        }
    }

}
