package com.noumsi.christian.mynews.controller.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_ART;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_BUSINESS;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_ENTREPRENEURS;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_POLITIC;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_SPORT;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_TRAVEL;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_END_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_FQ;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_QUERY_TERM;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_SWITCH_STATE;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener{

    @BindView(R.id.search_widget_edit_text_query) EditText mEditTextQuery;
    @BindView(R.id.search_widget_edit_text_date_start) EditText mEditTextDateStart;
    @BindView(R.id.search_widget_edit_text_date_end) EditText mEditTextDateEnd;
    @BindView(R.id.search_widget_begin_date_tv) TextView mTextViewBeginDate;
    @BindView(R.id.search_widget_end_date_tv) TextView mTextViewEndDate;
    @BindView(R.id.search_widget_arts_cat) CheckBox mCheckBoxArtsCat;
    @BindView(R.id.search_widget_business_cat) CheckBox mCheckBoxBusinessCat;
    @BindView(R.id.search_widget_entrepreneurs_cat) CheckBox mCheckBoxEntrepreneursCat;
    @BindView(R.id.search_widget_politics_cat) CheckBox mCheckBoxPoliticsCat;
    @BindView(R.id.search_widget_sports_cat) CheckBox mCheckBoxSportsCat;
    @BindView(R.id.search_widget_travel_cat) CheckBox mCheckBoxTravelCat;
    @BindView(R.id.activity_notification_switch) Switch mSwitch;

    AlarmManager alarmManager;
    PendingIntent pendingIntentReceiver;
    private SharedPreferences mSharedPreferences;
    private static final String TAG = "NotificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        // We initialise sharePreferences
        mSharedPreferences = getPreferences(MODE_PRIVATE);

        // we set text changed listener on query editText
        mEditTextQuery.addTextChangedListener(this);

        // First, we disable visibility of widget that we don't use (Date widget) from included layout
        mEditTextDateEnd.setVisibility(View.GONE);
        mEditTextDateStart.setVisibility(View.GONE);
        mTextViewBeginDate.setVisibility(View.GONE);
        mTextViewEndDate.setVisibility(View.GONE);

        // We disable switch widget
        mSwitch.setEnabled(false);

        // We listen changing of switch
        mSwitch.setOnCheckedChangeListener(this);

        // We set click listener on checkbox
        mCheckBoxArtsCat.setOnClickListener(this);
        mCheckBoxBusinessCat.setOnClickListener(this);
        mCheckBoxEntrepreneursCat.setOnClickListener(this);
        mCheckBoxPoliticsCat.setOnClickListener(this);
        mCheckBoxSportsCat.setOnClickListener(this);
        mCheckBoxTravelCat.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // We load saved preferences
        this.loadSavedPreferences();

        // After loading preferences, we configure search button
        this.configureSwitchWidget();
    }

    /**
     * In this method, we save in preferences state of all widgets
     */
    private void loadSavedPreferences() {
        // query term
        mEditTextQuery.setText(mSharedPreferences.getString(EXTRA_QUERY_TERM, null));
        // dates
        mEditTextDateStart.setText(mSharedPreferences.getString(EXTRA_BEGIN_DATE, null));
        mEditTextDateEnd.setText(mSharedPreferences.getString(EXTRA_END_DATE, null));
        // state of checkbox
        mCheckBoxArtsCat.setChecked(mSharedPreferences.getBoolean(EXTRA_CHECKBOX_ART, false));
        mCheckBoxBusinessCat.setChecked(mSharedPreferences.getBoolean(EXTRA_CHECKBOX_BUSINESS, false));
        mCheckBoxEntrepreneursCat.setChecked(mSharedPreferences.getBoolean(EXTRA_CHECKBOX_ENTREPRENEURS, false));
        mCheckBoxPoliticsCat.setChecked(mSharedPreferences.getBoolean(EXTRA_CHECKBOX_POLITIC, false));
        mCheckBoxSportsCat.setChecked(mSharedPreferences.getBoolean(EXTRA_CHECKBOX_SPORT, false));
        mCheckBoxTravelCat.setChecked(mSharedPreferences.getBoolean(EXTRA_CHECKBOX_TRAVEL, false));
        // state of switch widget
        mSwitch.setChecked(mSharedPreferences.getBoolean(EXTRA_SWITCH_STATE, false));
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
    private void savePreferences() {
        // query term
        mSharedPreferences.edit().putString(EXTRA_QUERY_TERM, mEditTextQuery.getText().toString()).apply();
        // dates
        mSharedPreferences.edit().putString(EXTRA_BEGIN_DATE, mEditTextDateStart.getText().toString()).apply();
        mSharedPreferences.edit().putString(EXTRA_END_DATE, mEditTextDateEnd.getText().toString()).apply();
        // state of checkbox
        mSharedPreferences.edit().putBoolean(EXTRA_CHECKBOX_BUSINESS, mCheckBoxBusinessCat.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(EXTRA_CHECKBOX_ENTREPRENEURS, mCheckBoxEntrepreneursCat.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(EXTRA_CHECKBOX_POLITIC, mCheckBoxPoliticsCat.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(EXTRA_CHECKBOX_SPORT, mCheckBoxSportsCat.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(EXTRA_CHECKBOX_TRAVEL, mCheckBoxTravelCat.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(EXTRA_CHECKBOX_ART, mCheckBoxArtsCat.isChecked()).apply();
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
        }
    }

    private boolean checkBoxAreChecked() {
        if (mCheckBoxTravelCat.isChecked()) return true;
        else if (mCheckBoxSportsCat.isChecked()) return true;
        else if (mCheckBoxPoliticsCat.isChecked()) return true;
        else if (mCheckBoxEntrepreneursCat.isChecked()) return true;
        else if (mCheckBoxBusinessCat.isChecked()) return true;
        else if (mCheckBoxArtsCat.isChecked()) return true;
        else return false;
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
        Log.d(TAG, "onCheckedChanged: Ok");
        if (isChecked) {
            // We configure broadcast receiver with alarm manager
            configureNotificationBroadcast();
        } else {
            // We remove alarm manager
            alarmManager.cancel(pendingIntentReceiver);
        }
    }

    /**
     * We set an Alarm Manager and pending Intent to broadcast receiver
     */
    private void configureNotificationBroadcast() {
        // We set time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 11);
        calendar.set(Calendar.SECOND, 30);

        // We create intent to notification receiver
        Intent receiver = new Intent(getApplicationContext(), NotificationReceiver.class);
        receiver.putExtra(EXTRA_QUERY_TERM, mEditTextQuery.getText().toString());
        // we construct fq of search service and send to intent
        receiver.putExtra(EXTRA_FQ, getFQ());
        // We get date in dd/MM/yyyy format
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
        receiver.putExtra(EXTRA_BEGIN_DATE, date);

        // We initialize pending intent to receiver
        pendingIntentReceiver = PendingIntent.getBroadcast(getApplicationContext(), 100, receiver, PendingIntent.FLAG_UPDATE_CURRENT);

        // We create an Alarm Manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentReceiver);
    }

    // We get text of all checked Checkbox who fq parameter in Search API on NYT
    private String getFQ() {
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
}
