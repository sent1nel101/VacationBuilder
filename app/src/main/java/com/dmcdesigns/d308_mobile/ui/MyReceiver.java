package com.dmcdesigns.d308_mobile.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    // public static final String EXTRA_NOTIFICATION_TYPE = "EXTRA_NOTIFICATION_TYPE";
    // public static final String EXTRA_TITLE = "EXTRA_TITLE";
    // public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    // public static final String EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE"; // Though not used for Toast content

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyReceiver", "Alarm received!");

        if (intent != null) {
            final String message = intent.getStringExtra("EXTRA_MESSAGE");

            if (message != null && !message.isEmpty()) {
                Log.d("MyReceiver", "Message to toast: " + message);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            Log.d("MyReceiver", "Toast attempt made for: " + message);
                        } catch (Exception e) {
                            Log.e("MyReceiver", "Error showing toast: " + e.getMessage(), e);
                        }
                    }
                });
            } else {
                Log.w("MyReceiver", "Received intent with no message to display.");
            }
        } else {
            Log.w("MyReceiver", "Received a null intent.");
        }
    }
}
