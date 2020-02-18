package dk.itu.moapd.threadsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class HandlerFragment extends Fragment {

    private static final String CONT = "CONT";

    private byte cont;
    private boolean status;

    private Handler handler;
    private ProgressBar mProgressBar;
    private TextView mProgressText;
    private Button mResetButton;

    private Fragment mFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragment = this;
        handler = new Handler();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_threads, container, false);

        mProgressBar = view.findViewById(R.id.progressBar);

        mResetButton = view.findViewById(R.id.resetButton);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cont = 0;
            }
        });

        ToggleButton startButton = view.findViewById(R.id.startButton);
        startButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    status = true;
                    new Thread(new Task()).start();
                } else
                    status = false;

                mResetButton.setEnabled(b);
            }
        });

        if (savedInstanceState != null)
            cont = savedInstanceState.getByte(CONT, (byte) 0);

        String text = String.format(
                getString(R.string.progressText), (int) cont);

        mProgressText = view.findViewById(R.id.progressText);
        mProgressText.setText(text);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putByte(CONT, cont);
    }

    private class Task implements Runnable {

        @Override
        public void run() {

            while (status) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mFragment.isVisible()) {
                            String text = String.format(
                                    getString(R.string.progressText),
                                             (int) cont);
                            mProgressText.setText(text);
                        }
                        mProgressBar.setProgress(cont++);
                        cont %= 100;
                    }
                });
            }
        }
    }

}
