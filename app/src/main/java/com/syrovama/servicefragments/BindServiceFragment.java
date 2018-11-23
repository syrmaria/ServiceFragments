package com.syrovama.servicefragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

public class BindServiceFragment extends BaseFragment {
    public static final String TAG = "MyBindServiceFragment";

    public interface Callbacks {
        void onTimerBindService(int result);
    }

    private boolean isServiceBinded;
    private SomeService.SomeBinder mServiceBinder;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mServiceBinder = (SomeService.SomeBinder)service;
            isServiceBinded = true;
            Log.d(TAG, "Service connected.");
            showServiceResult(mServiceBinder.getNumber());
        }
        public void onServiceDisconnected(ComponentName className) {
            mConnection = null;
            mServiceBinder = null;
            isServiceBinded = false;
            Log.d(TAG, "Service disconnected.");
        }
    };

    @Override
    protected View.OnClickListener getButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceBinded) {
                    showServiceResult(mServiceBinder.getNumber());
                } else {
                    doBindService();
                }
            }
        };
    }

    @Override
    protected int getInfoText() {
        return R.string.bind_service;
    }

    @Override
    protected Runnable getTaskForUI() {
        return new Runnable() {
            @Override
            public void run() {
                mParentActivity.onTimerBindService(mOurResult);
            }
        };
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause called");
        doUnbindService();
        super.onPause();
    }

    void doBindService() {
        if (mParentActivity.bindService(new Intent(mParentActivity, SomeService.class),
                mConnection, Context.BIND_NOT_FOREGROUND)) {
            Log.d(TAG, "Called to bind service");
        } else {
            Log.e(TAG, "Service doesn't exist");
        }
    }

    private void doUnbindService() {
        if (isServiceBinded) {
            mParentActivity.unbindService(mConnection);
            isServiceBinded = false;
            Log.d(TAG, "Unbind service");
        } else {
            Log.d(TAG, "No need to unbind service");
        }
    }
}
