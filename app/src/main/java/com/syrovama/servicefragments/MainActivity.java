package com.syrovama.servicefragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends FragmentActivity
        implements StartServiceFragment.Callbacks, BindServiceFragment.Callbacks {
    private static final String TAG = "MyMainActivity" ;
    private Fragment mStartServiceFragment;
    private Fragment mBindServiceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate");
        setContentView(R.layout.activity_main);
        mStartServiceFragment = addAndGetFragment(R.id.fragmentStartServiceContainer);
        mBindServiceFragment = addAndGetFragment(R.id.fragmentBindServiceContainer);
    }

    private Fragment addAndGetFragment(int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(id);
        if (fragment == null) {
            if (id == R.id.fragmentStartServiceContainer) {
                Log.d(TAG, "New start fragment");
                fragment = new StartServiceFragment();
            } else {
                Log.d(TAG, "New bind fragment");
                fragment = new BindServiceFragment();
            }
            fragmentManager.beginTransaction()
                    .add(id, fragment)
                    .commit();
        }
        return fragment;
    }

    @Override
    public void onTimerBindService(int result) {
        Log.d(TAG, "onTimerBindService");
        ((StartServiceFragment) mStartServiceFragment).showServiceOtherResult(result);

    }

    @Override
    public void onTimerStartService(int result) {
        Log.d(TAG, "onTimerStartService");
        ((BindServiceFragment) mBindServiceFragment).showServiceOtherResult(result);
    }
}
