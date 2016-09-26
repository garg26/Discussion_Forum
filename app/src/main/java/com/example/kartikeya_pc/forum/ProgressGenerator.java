package com.example.kartikeya_pc.forum;

import android.os.Handler;
import com.dd.processbutton.ProcessButton;
import java.util.Random;


public class ProgressGenerator {

    public interface OnCompleteListener {

        public void onComplete();
        public void onError();
    }

    private OnCompleteListener mListener;
    private int mProgress;

    public ProgressGenerator(OnCompleteListener listener) {
        mListener = listener;
    }

    public void start(final ProcessButton button) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgress += 10;
                button.setProgress(mProgress);
                if (mProgress < 100 && mProgress>0) {
                    handler.postDelayed(this, generateDelay());
                }
                else if(mProgress<0){
                    mListener.onError();
                }
                else {
                    mListener.onComplete();
                }
            }
        }, generateDelay());
    }

    private Random random = new Random();

    private int generateDelay() {
        return random.nextInt(1000);
    }
}
