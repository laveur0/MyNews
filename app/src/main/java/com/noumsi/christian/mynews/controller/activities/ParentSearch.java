package com.noumsi.christian.mynews.controller.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import com.noumsi.christian.mynews.R;
import com.noumsi.christian.mynews.controller.receivers.NotificationReceiver;

import java.util.Calendar;

import butterknife.BindView;

import static com.noumsi.christian.mynews.utils.Constants.EXTRA_BEGIN_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_ART;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_BUSINESS;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_ENTREPRENEURS;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_POLITIC;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_SPORT;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_TRAVEL;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_FQ;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_NAME_ACTIVITY;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_NOTIFICATION_STATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_QUERY_TERM;

/**
 * Created by christian-noumsi on 25/10/2018.
 */
public class ParentSearch extends AppCompatActivity {

    @BindView(R.id.search_widget_edit_text_query) EditText mEditTextQuery;
    @BindView(R.id.search_widget_arts_cat) CheckBox mCheckBoxArtsCat;
    @BindView(R.id.search_widget_business_cat) CheckBox mCheckBoxBusinessCat;
    @BindView(R.id.search_widget_entrepreneurs_cat) CheckBox mCheckBoxEntrepreneursCat;
    @BindView(R.id.search_widget_politics_cat) CheckBox mCheckBoxPoliticsCat;
    @BindView(R.id.search_widget_sports_cat) CheckBox mCheckBoxSportsCat;
    @BindView(R.id.search_widget_travel_cat) CheckBox mCheckBoxTravelCat;
    @BindView(R.id.search_widget_switch) Switch mSwitch;

    protected SharedPreferences mSharedPreferences;
    protected AlarmManager mAlarmManager;
    private static final String TAG = "ParentSearch";
    private String mNameActivity;
    protected boolean mNotificationState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * We initialize preferences
     * @param name of preferences
     */
    protected void initPreferences(String name) {
        mSharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        mNameActivity = name;
    }

    /**
     * Method to save state of widgets in share preferences
     */
    protected void savePreferences() {
        // query term
        mSharedPreferences.edit().putString(EXTRA_QUERY_TERM, mEditTextQuery.getText().toString()).apply();
        // state of checkbox
        mSharedPreferences.edit().putBoolean(EXTRA_CHECKBOX_BUSINESS, mCheckBoxBusinessCat.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(EXTRA_CHECKBOX_ENTREPRENEURS, mCheckBoxEntrepreneursCat.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(EXTRA_CHECKBOX_POLITIC, mCheckBoxPoliticsCat.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(EXTRA_CHECKBOX_SPORT, mCheckBoxSportsCat.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(EXTRA_CHECKBOX_TRAVEL, mCheckBoxTravelCat.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(EXTRA_CHECKBOX_ART, mCheckBoxArtsCat.isChecked()).apply();
        // state of switch button
        mSharedPreferences.edit().putBoolean(EXTRA_NOTIFICATION_STATE, mSwitch.isChecked()).apply();
    }

    /**
     * Method to load widgets state of share preferences
     */
    protected void loadSavedPreferences() {
        // query term
        mEditTextQuery.setText(mSharedPreferences.getString(EXTRA_QUERY_TERM, null));
        // state of checkbox
        mCheckBoxArtsCat.setChecked(mSharedPreferences.getBoolean(EXTRA_CHECKBOX_ART, false));
        mCheckBoxBusinessCat.setChecked(mSharedPreferences.getBoolean(EXTRA_CHECKBOX_BUSINESS, false));
        mCheckBoxEntrepreneursCat.setChecked(mSharedPreferences.getBoolean(EXTRA_CHECKBOX_ENTREPRENEURS, false));
        mCheckBoxPoliticsCat.setChecked(mSharedPreferences.getBoolean(EXTRA_CHECKBOX_POLITIC, false));
        mCheckBoxSportsCat.setChecked(mSharedPreferences.getBoolean(EXTRA_CHECKBOX_SPORT, false));
        mCheckBoxTravelCat.setChecked(mSharedPreferences.getBoolean(EXTRA_CHECKBOX_TRAVEL, false));
        // state of switch widget
        mNotificationState = mSharedPreferences.getBoolean(EXTRA_NOTIFICATION_STATE, false);
        mSwitch.setChecked(mNotificationState);
    }

    // We get text of all checked Checkbox who fq parameter in Search API on NYT
    protected String getFQ() {
        String fq = "news_desk:(";
        if (mCheckBoxTravelCat.isChecked())
            fq += "\"Travel\" ";
        if (mCheckBoxSportsCat.isChecked())
            fq += "\"Sports\" ";
        if (mCheckBoxPoliticsCat.isChecked())
            fq += "\"Politics\" ";
        if (mCheckBoxEntrepreneursCat.isChecked())
            fq += "\"Entrepreneurs\" ";
        if (mCheckBoxBusinessCat.isChecked())
            fq += "\"Business\" ";
        if (mCheckBoxArtsCat.isChecked())
            fq += "\"Arts\"";
        fq += ")";
        return fq;
    }

    /**
     * To check if a checkbox are selected
     * @return
     */
    protected boolean checkBoxAreChecked() {
        if (mCheckBoxTravelCat.isChecked()) return true;
        else if (mCheckBoxSportsCat.isChecked()) return true;
        else if (mCheckBoxPoliticsCat.isChecked()) return true;
        else if (mCheckBoxEntrepreneursCat.isChecked()) return true;
        else if (mCheckBoxBusinessCat.isChecked()) return true;
        else if (mCheckBoxArtsCat.isChecked()) return true;
        else return false;
    }

    /**
     * In this method we enable or disable switch widget
     * If no checkbox checked, we also uncheck switch widget
     */
    protected void configureSwitchWidget() {
        if (checkBoxAreChecked()) {
            Editable editable = mEditTextQuery.getText();
            if (editable != null) {
                String text = editable.toString();
                if (!text.isEmpty()) {
                    mSwitch.setEnabled(true);
                }
            }
        } else {
            // we change state checked of switch to false
            mSwitch.setChecked(false);
            // We disable switch button
            mSwitch.setEnabled(false);
            // We remove alarm manager
            this.removeAlarmManager(100);
            mNotificationState = false;
        }
    }

    /**
     * We set an Alarm Manager and pending Intent to broadcast receiver
     */
    protected void configureNotificationBroadcast(int hour, int minute, int second, int requestCode) {
        // We set time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.AM_PM, Calendar.PM);

        // We create intent to notification receiver
        Intent receiver = new Intent(getApplicationContext(), NotificationReceiver.class);
        receiver.putExtra(EXTRA_QUERY_TERM, mEditTextQuery.getText().toString());
        // we construct fq of search service and send to intent
        receiver.putExtra(EXTRA_FQ, getFQ());
        // We get date in dd/MM/yyyy format
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
        receiver.putExtra(EXTRA_BEGIN_DATE, date);
        receiver.putExtra(EXTRA_NAME_ACTIVITY, mNameActivity);

        // We initialize pending intent to receiver
        PendingIntent pendingIntentReceiver = PendingIntent.getBroadcast(getApplicationContext(), requestCode, receiver, PendingIntent.FLAG_UPDATE_CURRENT);

        // We configure Alarm Manager
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentReceiver);

        // we save parameters for reload alarm in boot complete receiver
        this.saveExtratOfReceiverInPreferences(date, getFQ());

        Log.d(TAG, "configureNotificationBroadcast: Alarm manager enable");
    }

    private void saveExtratOfReceiverInPreferences(String beginDate, String fq) {
        mSharedPreferences.edit().putString(EXTRA_BEGIN_DATE, beginDate).apply();
        mSharedPreferences.edit().putString(EXTRA_FQ, fq).apply();
    }

    // Method to remove register alarm manager
    protected void removeAlarmManager(int requestCode) {
        Intent receiver = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntentReceiver = PendingIntent.getBroadcast(getApplicationContext(), requestCode, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            mAlarmManager.cancel(pendingIntentReceiver);
            pendingIntentReceiver.cancel();
            Log.d(TAG, "removeAlarmManager: Alarm manager disable");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
