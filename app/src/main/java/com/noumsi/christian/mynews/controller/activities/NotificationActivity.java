package com.noumsi.christian.mynews.controller.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.noumsi.christian.mynews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.noumsi.christian.mynews.utils.Constants.EXTRA_NOTIFICATION_STATE;

public class NotificationActivity extends ParentSearch implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener{

    @BindView(R.id.search_widget_edit_text_date_start) EditText mEditTextDateStart;
    @BindView(R.id.search_widget_edit_text_date_end) EditText mEditTextDateEnd;
    @BindView(R.id.search_widget_begin_date_tv) TextView mTextViewBeginDate;
    @BindView(R.id.search_widget_end_date_tv) TextView mTextViewEndDate;

    private static final String TAG = "NotificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        // We initialise sharePreferences
        super.initPreferences(TAG);

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
        loadSavedPreferences();

        // After loading preferences, we configure switch button
        configureSwitchWidget();
    }


    @Override
    protected void onStop() {
        // We save widgets state in preferences
        this.savePreferences();
        super.onStop();
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

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
            if (!mNotificationState) {
                if (this.notificationStateInSearchActivity()) {
                    this.removeAlarmManager(100);
                    // we disable state of notification in SearchActivity
                    this.disableNotificationInSearchActivity();
                }
                // We configure broadcast receiver with alarm manager
                this.configureNotificationBroadcast(9, 00, 00, 100);
            }
        } else {
            // We remove alarm manager
            this.removeAlarmManager(100);
            mNotificationState = false;
        }
    }

    private boolean notificationStateInSearchActivity() {
        SharedPreferences preferences = getSharedPreferences("SearchActivity", MODE_PRIVATE);
        return preferences.getBoolean(EXTRA_NOTIFICATION_STATE, false);
    }

    private void disableNotificationInSearchActivity() {
        SharedPreferences preferences = getSharedPreferences("SearchActivity", MODE_PRIVATE);
        preferences.edit().putBoolean(EXTRA_NOTIFICATION_STATE, false).apply();
    }
}
