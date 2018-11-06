package com.noumsi.christian.mynews.controller.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_BEGIN_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_FQ;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_QUERY_TERM;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_SWITCH_STATE;

public class BootCompleteReceiver extends BroadcastReceiver {

    private static final String TAG = "BootCompleteReceiver";
    private String query, fq, date, namePreferences = "NotificationActivity";
    private boolean stateSwichWidget;
    protected SharedPreferences mSharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.

        mSharedPreferences = context.getSharedPreferences(namePreferences, MODE_PRIVATE);
        // We read preferences for load alarm
        this.loadPreferences();
        // we configure notification alarm if it is activated
        if (stateSwichWidget)
            this.configureNotificationBroadcast(context);
    }

    private void configureNotificationBroadcast(Context context) {
        Log.d(TAG, "configureNotificationBroadcast: launch");
        // We set time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 01);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.AM_PM, Calendar.PM);

        // We create intent to notification receiver
        Intent receiver = new Intent(context, NotificationReceiver.class);
        receiver.putExtra(EXTRA_QUERY_TERM, query);
        // we construct fq of search service and send to intent
        receiver.putExtra(EXTRA_FQ, fq);
        receiver.putExtra(EXTRA_BEGIN_DATE, date);

        // We initialize pending intent to receiver
        PendingIntent pendingIntentReceiver = PendingIntent.getBroadcast(context, 100, receiver, PendingIntent.FLAG_UPDATE_CURRENT);

        // We create an Alarm Manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentReceiver);
        Log.d(TAG, "configureNotificationBroadcast: alarmManager enable");
    }

    private void loadPreferences() {
        query = mSharedPreferences.getString(EXTRA_QUERY_TERM, "null");
        fq = mSharedPreferences.getString(EXTRA_FQ, "null");
        date = mSharedPreferences.getString(EXTRA_BEGIN_DATE, "null");
        stateSwichWidget = mSharedPreferences.getBoolean(EXTRA_SWITCH_STATE, false);
    }
}
