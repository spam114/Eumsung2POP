package com.symbol.eumsung2pop.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.symbol.eumsung2pop.R;

import java.util.Set;

public class MC3300XReceiver extends BroadcastReceiver {
    private final String EXTRA_PROFILENAME = "DWDataCapture1";

    // DataWtras
    private final String EXTRA_GET_VERSION_INFO = "com.symbol.datawedge.api.GET_VERSION_INFO";
    private final String EXTRA_CREATE_PROFILE = "com.symbol.datawedge.api.CREATE_PROFILE";
    private final String EXTRA_KEY_APPLICATION_NAME = "com.symbol.datawedge.api.APPLICATION_NAME";
    private final String EXTRA_KEY_NOTIFICATION_TYPE = "com.symbol.datawedge.api.NOTIFICATION_TYPE";
    private final String EXTRA_SOFT_SCAN_TRIGGER = "com.symbol.datawedge.api.SOFT_SCAN_TRIGGER";
    private final String EXTRA_RESULT_NOTIFICATION = "com.symbol.datawedge.api.NOTIFICATION";
    private final String EXTRA_REGISTER_NOTIFICATION = "com.symbol.datawedge.api.REGISTER_FOR_NOTIFICATION";
    private final String EXTRA_UNREGISTER_NOTIFICATION = "com.symbol.datawedge.api.UNREGISTER_FOR_NOTIFICATION";
    private final String EXTRA_SET_CONFIG = "com.symbol.datawedge.api.SET_CONFIG";

    private final String EXTRA_RESULT_NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
    private final String EXTRA_KEY_VALUE_SCANNER_STATUS = "SCANNER_STATUS";
    private final String EXTRA_KEY_VALUE_PROFILE_SWITCH = "PROFILE_SWITCH";
    private final String EXTRA_KEY_VALUE_CONFIGURATION_UPDATE = "CONFIGURATION_UPDATE";
    private final String EXTRA_KEY_VALUE_NOTIFICATION_STATUS = "STATUS";
    private final String EXTRA_KEY_VALUE_NOTIFICATION_PROFILE_NAME = "PROFILE_NAME";
    private final String EXTRA_SEND_RESULT = "SEND_RESULT";

    private final String EXTRA_EMPTY = "";

    private final String EXTRA_RESULT_GET_VERSION_INFO = "com.symbol.datawedge.api.RESULT_GET_VERSION_INFO";
    private final String EXTRA_RESULT = "RESULT";
    private final String EXTRA_RESULT_INFO = "RESULT_INFO";
    private final String EXTRA_COMMAND = "COMMAND";

    // DataWtions
    private final String ACTION_DATAWEDGE = "com.symbol.datawedge.api.ACTION";
    private final String ACTION_RESULT_NOTIFICATION = "com.symbol.datawedge.api.NOTIFICATION_ACTION";
    private final String ACTION_RESULT = "com.symbol.datawedge.api.RESULT_ACTION";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle b = intent.getExtras();

        // Get DataWedge version info
        if (intent.hasExtra(EXTRA_RESULT_GET_VERSION_INFO)) {
            Bundle versionInfo = intent.getBundleExtra(EXTRA_RESULT_GET_VERSION_INFO);
            String DWVersion = versionInfo.getString("DATAWEDGE");

                /*TextView txtDWVersion = (TextView) findViewById(R.id.txtGetDWVersion);
                txtDWVersion.setText(DWVersion);*/
        }

        if (action.equals(context.getResources().getString(R.string.activity_intent_filter_action))) {
            //  Received a barcode scan
            try {
                //getMC3300XResult(intent, "via Broadcast");
                String decodedData = intent.getStringExtra(context.getResources().getString(R.string.datawedge_intent_key_data));
                // store decoder type
                String decodedLabelType = intent.getStringExtra(context.getResources().getString(R.string.datawedge_intent_key_label_type));

                //final TextView lblScanData = (TextView) findViewById(R.id.lblScanData);
                //final TextView lblScanLabelType = (TextView) findViewById(R.id.lblScanDecoder);
                Intent i = new Intent("mycustombroadcast");
                i.putExtra("mcx3300result", decodedData);
                context.sendBroadcast(i);
            } catch (Exception e) {
                //  Catch error if the UI does not exist when we receive the broadcast...
            }
        } else if (action.equals(ACTION_RESULT)) {
            // Register to receive the result code
            if ((intent.hasExtra(EXTRA_RESULT)) && (intent.hasExtra(EXTRA_COMMAND))) {
                String command = intent.getStringExtra(EXTRA_COMMAND);
                String result = intent.getStringExtra(EXTRA_RESULT);
                String info = "";

                if (intent.hasExtra(EXTRA_RESULT_INFO)) {
                    Bundle result_info = intent.getBundleExtra(EXTRA_RESULT_INFO);
                    Set<String> keys = result_info.keySet();
                    for (String key : keys) {
                        Object object = result_info.get(key);
                        if (object instanceof String) {
                            info += key + ": " + object + "\n";
                        } else if (object instanceof String[]) {
                            String[] codes = (String[]) object;
                            for (String code : codes) {
                                info += key + ": " + code + "\n";
                            }
                        }
                    }
                    Toast.makeText(context.getApplicationContext(), "Error Resulted. Command:" + command + "\nResult: " + result + "\nResult Info: " + info, Toast.LENGTH_LONG).show();
                }
            }

        }

        // Register for scanner change notification
        else if (action.equals(ACTION_RESULT_NOTIFICATION)) {
            if (intent.hasExtra(EXTRA_RESULT_NOTIFICATION)) {
                Bundle extras = intent.getBundleExtra(EXTRA_RESULT_NOTIFICATION);
                String notificationType = extras.getString(EXTRA_RESULT_NOTIFICATION_TYPE);
                if (notificationType != null) {
                    switch (notificationType) {
                        case EXTRA_KEY_VALUE_SCANNER_STATUS:
                            // Change in scanner status occurred
                            String displayScannerStatusText = extras.getString(EXTRA_KEY_VALUE_NOTIFICATION_STATUS) +
                                    ", profile: " + extras.getString(EXTRA_KEY_VALUE_NOTIFICATION_PROFILE_NAME);
                            //Toast.makeText(getApplicationContext(), displayScannerStatusText, Toast.LENGTH_SHORT).show();
                            String result = displayScannerStatusText;
                            break;

                        case EXTRA_KEY_VALUE_PROFILE_SWITCH:
                            // Received change in profile
                            // For future enhancement
                            break;

                        case EXTRA_KEY_VALUE_CONFIGURATION_UPDATE:
                            // Configuration change occurred
                            // For future enhancement
                            break;
                    }
                }
            }
        }
    }
}
