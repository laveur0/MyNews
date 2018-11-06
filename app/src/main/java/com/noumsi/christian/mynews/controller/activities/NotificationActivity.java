package com.noumsi.christian.mynews.controller.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.noumsi.christian.mynews.R;
import com.noumsi.christian.mynews.controller.receivers.NotificationReceiver;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.noumsi.christian.mynews.utils.Constants.EXTRA_BEGIN_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_FQ;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_QUERY_TERM;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_SWITCH_STATE;

public class NotificationActivity extends ParentSearch implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener{

    @BindView(R.id.search_widget_edit_text_date_start) EditText mEditTextDateStart;
    @BindView(R.id.search_widget_edit_text_date_end) EditText mEditTextDateEnd;
    @BindView(R.id.search_widget_begin_date_tv) TextView mTextViewBeginDate;
    @BindView(R.id.search_widget_end_date_tv) TextView mTextViewEndDate;
    @BindView(R.id.activity_notification_switch) Switch mSwitch;

    AlarmManager alarmManager;
    private static final String TAG = "NotificationActivity";
    private boolean mStatusSwitchWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        // We initialise sharePreferences
        super.initPreferences(TAG);

        // We create an Alarm Manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // we set text changed listener on query editText
        mEditTextQuery.addTextChangedListener(this);

        // First, we disable visibility of widget that we don't use (Date widget) from included layout
        mEditTextDateEnd.setVisibility(View.GONE);
        mEditTextDateStart.setVisibility(View.GONE);
        mTextViewBeginDate.setVisibility(View.GONE);
        mTextViewEndDate.setVisibility(View.GONE);

        // We disable switch widget
        mSwitch.setEnabled(false);

        // We set click listener on checkbox
        mCheckBoxArtsCat.setOnClickListener(this);
        mCheckBoxBusinessCat.setOnClickListener(this);
        mCheckBoxEntrepreneursCat.setOnClickListener(this);
        mCheckBoxPoliticsCat.setOnClickListener(this);
        mCheckBoxSportsCat.setOnClickListener(this);
        mCheckBoxTravelCat.setOnClickListener(this);

        // We listen changing of switch
        mSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // We load saved preferences
        this.loadSavedPreferences();

        // After loading preferences, we configure switch button
        this.configureSwitchWidget();
    }

    /**
     * In this method, we save in preferences state of all widgets
     */
    protected void loadSavedPreferences() {
        super.loadSavedPreferences();
        // state of switch widget
        mStatusSwitchWidget = mSharedPreferences.getBoolean(EXTRA_SWITCH_STATE, false);
        mSwitch.setChecked(mStatusSwitchWidget);
    }

    @Override
    protected void onStop() {
        // We save widgets state in preferences
        this.savePreferences();
        super.onStop();
    }

    /**
     * Method to save state of widgets in share preferences
     */
    protected void savePreferences() {
        super.savePreferences();
        // state of switch button
        mSharedPreferences.edit().putBoolean(EXTRA_SWITCH_STATE, mSwitch.isChecked()).apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_widget_arts_cat:
                // We configure search button depending of selection of checkbox and query in edit text
                configureSwitchWidget();
                break;
            case R.id.search_widget_business_cat:
                configureSwitchWidget();
                break;
            case R.id.search_widget_entrepreneurs_cat:
                configureSwitchWidget();
                break;
            case R.id.search_widget_politics_cat:
                configureSwitchWidget();
                break;
            case R.id.search_widget_sports_cat:
                configureSwitchWidget();
                break;
            case R.id.search_widget_travel_cat:
                configureSwitchWidget();
                break;
        }
    }

    /**
     * In this method we enable or disable switch widget
     * If no checkbox checked, we also uncheck switch widget
     */
    private void configureSwitchWidget() {
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
            this.removeAlarmManager();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            if (checkBoxAreChecked())
                mSwitch.setEnabled(true);
        } else {
            // we change state checked of switch to false
            mSwitch.setChecked(false);
            // We disable switch button
            mSwitch.setEnabled(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (!mStatusSwitchWidget) {
                // We configure broadcast receiver with alarm manager
                configureNotificationBroadcast();
            }
        } else {
            // We remove alarm manager
            this.removeAlarmManager();
        }
    }

    // Method to remove register alarm manager
    private void removeAlarmManager() {
        Intent receiver = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntentReceiver = PendingIntent.getBroadcast(getApplicationContext(), 100, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            alarmManager.cancel(pendingIntentReceiver);
            pendingIntentReceiver.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mStatusSwitchWidget = false;
        Log.d(TAG, "removeAlarmManager: alarmManager disable");
    }

    /**
     * We set an Alarm Manager and pending Intent to broadcast receiver
     */
    private void configureNotificationBroadcast() {
        // We set time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 01);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.AM_PM, Calendar.PM);
        Log.d(TAG, "configureNotificationBroadcast:"+calendar.getTime().toString());

        // We create intent to notification receiver
        Intent receiver = new Intent(getApplicationContext(), NotificationReceiver.class);
        receiver.putExtra(EXTRA_QUERY_TERM, mEditTextQuery.getText().toString());
        // we construct fq of search service and send to intent
        receiver.putExtra(EXTRA_FQ, getFQ());
        // We get date in dd/MM/yyyy format
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
        receiver.putExtra(EXTRA_BEGIN_DATE, date);

        // We initialize pending intent to receiver
        PendingIntent pendingIntentReceiver = PendingIntent.getBroadcast(getApplicationContext(), 100, receiver, PendingIntent.FLAG_UPDATE_CURRENT);

        // We configure Alarm Manager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentReceiver);
        Log.d(TAG, "configureNotificationBroadcast: alarmManager enable");

        // we save parameters for reload alarm
        this.saveExtratOfReceiverInPreferences(date, getFQ());
    }

    private void saveExtratOfReceiverInPreferences(String beginDate, String fq) {
        mSharedPreferences.edit().putString(EXTRA_BEGIN_DATE, beginDate).apply();
        mSharedPreferences.edit().putString(EXTRA_FQ, fq).apply();
    }
}
