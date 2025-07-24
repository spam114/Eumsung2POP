package com.symbol.eumsung2pop.view;

import static android.content.Context.RECEIVER_EXPORTED;
import static android.provider.ContactsContract.Intents.Insert.ACTION;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.symbol.eumsung2pop.R;
import com.symbol.eumsung2pop.view.receiver.MC3300XReceiver;

public class MC3300X {
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

    // private variables
    private Boolean bRequestSendResult = false;
    final String LOG_TAG = "DataCapture1";

    private Context context;
    MC3300XReceiver mc3300XReceiver;

    public MC3300X(Context context) {
        this.context = context;
        this.mc3300XReceiver = new MC3300XReceiver();
        CreateProfile();
        // Register for status change notification
        // Use REGISTER_FOR_NOTIFICATION: http://techdocs.zebra.com/datawedge/latest/guide/api/registerfornotification/
        Bundle b = new Bundle();
        b.putString(EXTRA_KEY_APPLICATION_NAME, context.getPackageName());
        b.putString(EXTRA_KEY_NOTIFICATION_TYPE, "SCANNER_STATUS");     // register for changes in scanner status
        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_REGISTER_NOTIFICATION, b);

        registerReceivers();

        // Get DataWedge version
        // Use GET_VERSION_INFO: http://techdocs.zebra.com/datawedge/latest/guide/api/getversioninfo/
        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_GET_VERSION_INFO, EXTRA_EMPTY);    // must be called after registering BroadcastReceiver

    }

    public void CreateProfile (){
        String profileName = EXTRA_PROFILENAME;

        // Send DataWedge intent with extra to create profile
        // Use CREATE_PROFILE: http://techdocs.zebra.com/datawedge/latest/guide/api/createprofile/
        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_CREATE_PROFILE, profileName);

        // Configure created profile to apply to this app
        Bundle profileConfig = new Bundle();
        profileConfig.putString("PROFILE_NAME", EXTRA_PROFILENAME);
        profileConfig.putString("PROFILE_ENABLED", "true");
        profileConfig.putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST");  // Create profile if it does not exist

        // Configure barcode input plugin
        Bundle barcodeConfig = new Bundle();
        barcodeConfig.putString("PLUGIN_NAME", "BARCODE");
        barcodeConfig.putString("RESET_CONFIG", "true"); //  This is the default
        Bundle barcodeProps = new Bundle();
        barcodeConfig.putBundle("PARAM_LIST", barcodeProps);
        profileConfig.putBundle("PLUGIN_CONFIG", barcodeConfig);

        // Associate profile with this app
        Bundle appConfig = new Bundle();
        appConfig.putString("PACKAGE_NAME", context.getPackageName());
        appConfig.putStringArray("ACTIVITY_LIST", new String[]{"*"});
        profileConfig.putParcelableArray("APP_LIST", new Bundle[]{appConfig});
        profileConfig.remove("PLUGIN_CONFIG");

        // Apply configs
        // Use SET_CONFIG: http://techdocs.zebra.com/datawedge/latest/guide/api/setconfig/
        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_SET_CONFIG, profileConfig);

        // Configure intent output for captured data to be sent to this app
        Bundle intentConfig = new Bundle();
        intentConfig.putString("PLUGIN_NAME", "INTENT");
        intentConfig.putString("RESET_CONFIG", "true");
        Bundle intentProps = new Bundle();
        intentProps.putString("intent_output_enabled", "true");
        intentProps.putString("intent_action", "com.zebra.datacapture1.ACTION");
        intentProps.putString("intent_delivery", "2");
        intentConfig.putBundle("PARAM_LIST", intentProps);
        profileConfig.putBundle("PLUGIN_CONFIG", intentConfig);
        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_SET_CONFIG, profileConfig);

        //Toast.makeText(getApplicationContext(), "Created profile.  Check DataWedge app UI.", Toast.LENGTH_LONG).show();
    }

    private void sendDataWedgeIntentWithExtra(String action, String extraKey, Bundle extras) {
        Intent dwIntent = new Intent();
        dwIntent.setAction(action);
        dwIntent.putExtra(extraKey, extras);
        if (bRequestSendResult)
            dwIntent.putExtra(EXTRA_SEND_RESULT, "true");
        context.sendBroadcast(dwIntent);
    }

    private void sendDataWedgeIntentWithExtra(String action, String extraKey, String extraValue)
    {
        Intent dwIntent = new Intent();
        dwIntent.setAction(action);
        dwIntent.putExtra(extraKey, extraValue);
        if (bRequestSendResult)
            dwIntent.putExtra(EXTRA_SEND_RESULT, "true");
        context.sendBroadcast(dwIntent);
    }

    public void registerReceivers() {

        Log.d(LOG_TAG, "registerReceivers()");

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_RESULT_NOTIFICATION);   // for notification result
        filter.addAction(ACTION_RESULT);                // for error code result
        filter.addCategory(Intent.CATEGORY_DEFAULT);    // needed to get version info

        // register to received broadcasts via DataWedge scanning
        filter.addAction(this.context.getResources().getString(R.string.activity_intent_filter_action));
        filter.addAction(this.context.getResources().getString(R.string.activity_action_from_service));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(mc3300XReceiver, filter, RECEIVER_EXPORTED);
        }else {
            context.registerReceiver(mc3300XReceiver, filter);
        }
    }

    public void unRegisterReceivers() {
        context.unregisterReceiver(mc3300XReceiver);
        unRegisterScannerStatus();
    }

    public void unRegisterScannerStatus() {
        Log.d(LOG_TAG, "unRegisterScannerStatus()");
        Bundle b = new Bundle();
        b.putString(EXTRA_KEY_APPLICATION_NAME, context.getPackageName());
        b.putString(EXTRA_KEY_NOTIFICATION_TYPE, EXTRA_KEY_VALUE_SCANNER_STATUS);
        Intent i = new Intent();
        i.setAction(ACTION);
        i.putExtra(EXTRA_UNREGISTER_NOTIFICATION, b);
        context.sendBroadcast(i);
    }
}
