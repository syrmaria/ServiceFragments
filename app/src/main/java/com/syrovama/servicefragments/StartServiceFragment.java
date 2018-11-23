package com.syrovama.servicefragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.view.View;

public class StartServiceFragment extends BaseFragment {
    private static final String TAG = "MyStartServiceFragment";

    private NumberReceiver numberReceiver;

    public interface Callbacks {
        void onTimerStartService(int result);
    }

    @Override
    public void onResume() {
        super.onResume();
        numberReceiver = new NumberReceiver(new Handler());
        IntentFilter intentFilter = new IntentFilter(SomeService.ACTION);
        mParentActivity.registerReceiver(numberReceiver, intentFilter);
        Log.d(TAG, "onResume - register receiver");
    }

    @Override
    public void onPause() {
        mParentActivity.stopService(SomeService.newIntent(mParentActivity));
        mParentActivity.unregisterReceiver(numberReceiver);
        Log.d(TAG, "onPause - stopped service");
        super.onPause();
    }

    @Override
    protected View.OnClickListener getButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStartService();
            }
        };
    }

    @Override
    protected int getInfoText() {
        return R.string.start_service;
    }


    @Override
    protected Runnable getTaskForUI() {
        return new Runnable() {
            @Override
            public void run() {
                mParentActivity.onTimerStartService(mOurResult);
            }
        };
    }

    private void doStartService() {
        mParentActivity.startService(SomeService.newIntent(mParentActivity));
        Log.d(TAG, "Started service");
    }

    public class NumberReceiver extends BroadcastReceiver {
        private final Handler handler;

        public NumberReceiver(Handler handler) {
            this.handler = handler;
            Log.d(TAG, "Receiver: created");
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            final int number = intent.getIntExtra(SomeService.EXTRA_RESULT, 0);
            Log.d(TAG, "Receiver: got state, post to main activity");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    showServiceResult(number);
                }
            });
        }
    }
}
