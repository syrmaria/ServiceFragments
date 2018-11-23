package com.syrovama.servicefragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends FragmentActivity
        implements StartServiceFragment.Callbacks, BindServiceFragment.Callbacks {
    private Fragment startServiceFragment;
    private Fragment bindServiceFragment;

    private static final String TAG = "MyMainActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate");
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        startServiceFragment = fragmentManager.findFragmentById(R.id.fragmentStartServiceContainer);
        if (startServiceFragment == null) {
            Log.d(TAG, "New start fragment");
            startServiceFragment = new StartServiceFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentStartServiceContainer, startServiceFragment)
                    .commit();
        }
        bindServiceFragment = fragmentManager.findFragmentById(R.id.fragmentBindServiceContainer);
        if (bindServiceFragment == null) {
            Log.d(TAG, "New bind fragment");
            bindServiceFragment = new BindServiceFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentBindServiceContainer, bindServiceFragment)
                    .commit();
        }
    }


    @Override
    public void onTimerBindService(int result) {
        Log.d(TAG, "onTimerBindService");
        ((StartServiceFragment)startServiceFragment).updateOtherResultValue(result);

    }

    @Override
    public void onTimerStartService(int result) {
        Log.d(TAG, "onTimerStartService");
        ((BindServiceFragment)bindServiceFragment).updateOtherResultValue(result);
    }
}
