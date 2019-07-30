package com.noumsi.christian.mynews.controller.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;
import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_BEGIN_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_FQ;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_NOTIFICATION_STATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_QUERY_TERM;

public class BootCompleteReceiver extends BroadcastReceiver {

    private static final String TAG = "BootCompleteReceiver";
    private String query, fq, date;
    private boolean mNotificationState;
    protected SharedPreferences mSharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast for bootComplete.

        // We ensure of this by verify action
        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            //We configure notification activity alarm
            this.configureNotification(context, "NotificationActivity", 1, 0, 0, 100);

            // We configure search activity alarm
            this.configureNotification(context, "SearchActivity", 1, 0, 0, 100);
        }
    }

    private void configureNotification(Context context, String name, int hour, int minute, int second, int requestCode) {
        mSharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE);
        // We read preferences for load alarm
        this.loadPreferences();
        // we configure notification alarm if it is activated
        if (mNotificationState)
            this.configureNotificationBroadcast(context, hour, minute, second, requestCode);
        else Log.d(TAG, "configureNotification: Notification state = false");
    }

    private void configureNotificationBroadcast(Context context, int hour, int minute, int second, int requestCode) {
        // We set time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.AM_PM, Calendar.PM);

        // We create intent to notification receiver
        Intent receiver = new Intent(context, NotificationReceiver.class);
        receiver.putExtra(EXTRA_QUERY_TERM, query);
        // we construct fq of search service and send to intent
        receiver.putExtra(EXTRA_FQ, fq);
        receiver.putExtra(EXTRA_BEGIN_DATE, date);

        // We initialize pending intent to receiver
        PendingIntent pendingIntentReceiver = PendingIntent.getBroadcast(context, requestCode, receiver, PendingIntent.FLAG_UPDATE_CURRENT);

        // We create an Alarm Manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Objects.requireNonNull(alarmManager).setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentReceiver);
        Log.d(TAG, "configureNotificationBroadcast: alarmManager enable");
    }

    private void loadPreferences() {
        query = mSharedPreferences.getString(EXTRA_QUERY_TERM, "null");
        fq = mSharedPreferences.getString(EXTRA_FQ, "null");
        date = mSharedPreferences.getString(EXTRA_BEGIN_DATE, "null");
        mNotificationState = mSharedPreferences.getBoolean(EXTRA_NOTIFICATION_STATE, false);
    }
}
