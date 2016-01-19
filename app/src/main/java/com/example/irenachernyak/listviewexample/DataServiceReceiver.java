package com.example.irenachernyak.listviewexample;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;



/**
 * Created by irenachernyak on 1/14/16.
 */
public class DataServiceReceiver extends ResultReceiver {

    private Listener listener;
    public DataServiceReceiver(Handler handler) {
        super(handler);
    }


    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (listener != null)
            listener.onReceiveResult(resultCode, resultData);
    }

    public static interface Listener {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

}
