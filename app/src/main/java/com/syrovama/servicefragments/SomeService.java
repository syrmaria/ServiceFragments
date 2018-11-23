package com.syrovama.servicefragments;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import java.util.Random;

public class SomeService extends Service {
    public static final String TAG = "MySomeService";
    private static final Random RANDOM = new Random();
    static final String START_RESULT = "RESULT";

    private final IBinder mBinder = new SomeBinder();

    class SomeBinder extends Binder {
        public int getNumber() {
            return RANDOM.nextInt();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Got intent " + intent);
        int nextNumber = RANDOM.nextInt();
        Intent nextNumberIntent = new Intent("myaction");
        nextNumberIntent.putExtra(START_RESULT, nextNumber);
        sendBroadcast(nextNumberIntent);
        Log.d(TAG, "Broadcast sent");
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Binded with intent " + intent);
        return mBinder;
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind");
    }

    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "Unbinded with intent " + intent);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destroyed");
    }

    public static Intent newIntent(Context c) {
        return new Intent(c, SomeService.class);
    }

}
