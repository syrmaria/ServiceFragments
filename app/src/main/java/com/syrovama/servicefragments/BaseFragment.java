package com.syrovama.servicefragments;

import android.os.Bundle;
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

public abstract class BaseFragment extends Fragment {
    public static final String TAG = "BaseFragment";
    protected MainActivity mParentActivity;
    private TextView mOurResultTextView;
    private TextView mOtherResultTextView;
    protected int mOurResult;
    private Timer mTimer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_base, container, false);
        mOurResultTextView = v.findViewById(R.id.ourResultValueTextView);
        mOtherResultTextView = v.findViewById(R.id.otherResultValueTextView);
        Button goButton = v.findViewById(R.id.goButton);
        goButton.setOnClickListener(getButtonListener());
        TextView infoTextView = v.findViewById(R.id.goTextView);
        infoTextView.setText(getInfoText());
        showServiceResult(mOurResult);
        setRetainInstance(true);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mParentActivity = (MainActivity) getActivity();
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mParentActivity.runOnUiThread(getTaskForUI());
                Log.d(TAG, "Timer event sent");
            }
        }, 0, 3000);
    }

    @Override
    public void onDestroyView() {
        mTimer.cancel();
        super.onDestroyView();
    }

    protected abstract int getInfoText();

    protected abstract Runnable getTaskForUI();

    protected abstract View.OnClickListener getButtonListener();

    protected void showServiceResult(int value) {
        mOurResult = value;
        mOurResultTextView.setText(getString(R.string.result_template, mOurResult));
    }

    protected void showServiceOtherResult(int value) {
        mOtherResultTextView.setText(getString(R.string.result_template, value));
    }
}
