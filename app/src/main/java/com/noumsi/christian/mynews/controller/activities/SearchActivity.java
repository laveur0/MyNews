package com.noumsi.christian.mynews.controller.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.noumsi.christian.mynews.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.noumsi.christian.mynews.utils.Constants.EXTRA_BEGIN_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_END_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_FQ;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_NOTIFICATION_STATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_QUERY_TERM;

public class SearchActivity extends ParentSearch implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.search_widget_edit_text_date_start) EditText mEditTextDateStart;
    @BindView(R.id.search_widget_edit_text_date_end) EditText mEditTextDateEnd;
    @BindView(R.id.activity_search_search_button) Button mButtonSearch;

    private DatePickerDialog.OnDateSetListener mDateSetListenerForBeginDate, mDateSetListenerForEndDate;
    private SimpleDateFormat simpleDateFormat;
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        // We initialise sharePreferences
        super.initPreferences(TAG);

        // we set title of toolbar
        setTitle(getString(R.string.title_search_activity));

        // We define date format
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        // we set text changed listener on search editText
        mEditTextQuery.addTextChangedListener(this);

        // set on touch listener on dates edit text view
        mEditTextDateStart.setOnClickListener(this);
        mEditTextDateEnd.setOnClickListener(this);

        // disable edition of edit text view
        mEditTextDateStart.setKeyListener(null);
        mEditTextDateEnd.setKeyListener(null);

        // We disable switch widget
        mSwitch.setEnabled(false);

        // Configure datapickerdialog for datasetlistener
        configureDataSetListener();

        // We set click listener on checkbox
        mCheckBoxArtsCat.setOnClickListener(this);
        mCheckBoxBusinessCat.setOnClickListener(this);
        mCheckBoxEntrepreneursCat.setOnClickListener(this);
        mCheckBoxPoliticsCat.setOnClickListener(this);
        mCheckBoxSportsCat.setOnClickListener(this);
        mCheckBoxTravelCat.setOnClickListener(this);

        // We listen changing of switch
        mSwitch.setOnCheckedChangeListener(this);

        // set on click listener on search button
        mButtonSearch.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // We load saved preferences
        this.loadSavedPreferences();

        // After loading preferences, we configure search and switch button
        this.configureSearchButton();
        configureSwitchWidget();
    }

    @Override
    protected void onStop() {
        // We save preferences of user
        this.savePreferences();
        super.onStop();
    }

    /**
     * Method to save state of widgets in share preferences
     */
    protected void savePreferences() {
        super.savePreferences();
        // dates
        mSharedPreferences.edit().putString(EXTRA_BEGIN_DATE, mEditTextDateStart.getText().toString()).apply();
        mSharedPreferences.edit().putString(EXTRA_END_DATE, mEditTextDateEnd.getText().toString()).apply();
    }

    /**
     * Method to load widgets state of share preferences
     */
    protected void loadSavedPreferences() {
        super.loadSavedPreferences();
        // dates
        mEditTextDateStart.setText(mSharedPreferences.getString(EXTRA_BEGIN_DATE, null));
        mEditTextDateEnd.setText(mSharedPreferences.getString(EXTRA_END_DATE, null));
    }

    // We configure listener for DatePickerDialog
    private void configureDataSetListener() {
        mDateSetListenerForBeginDate = (view, year, month, dayOfMonth) -> {
            Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
            mEditTextDateStart.setText(simpleDateFormat.format(calendar.getTime()));
        };

        mDateSetListenerForEndDate = (view, year, month, dayOfMonth) -> {
            Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
            mEditTextDateEnd.setText(simpleDateFormat.format(calendar.getTime()));
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_widget_edit_text_date_start:
                // We configure editText
                this.configureDateEditText(true);
                break;
            case R.id.search_widget_edit_text_date_end:
                // We configure editText
                this.configureDateEditText(false);
                break;
            case R.id.activity_search_search_button:
                Intent searchResultIntent = new Intent(this, SearchResultActivity.class);
                searchResultIntent.putExtra(EXTRA_QUERY_TERM, mEditTextQuery.getText().toString());
                // we construct fq of search service and send to intent
                searchResultIntent.putExtra(EXTRA_FQ, getFQ());
                searchResultIntent.putExtra(EXTRA_BEGIN_DATE, mEditTextDateStart.getText() != null ? mEditTextDateStart.getText().toString(): "");
                searchResultIntent.putExtra(EXTRA_END_DATE, mEditTextDateEnd.getText() != null ? mEditTextDateEnd.getText().toString(): "");
                startActivity(searchResultIntent);
                break;
            case R.id.search_widget_arts_cat:
                // We configure search button depending of selection of checkbox and query in edit text
                configureSearchButton();
                configureSwitchWidget();
                break;
            case R.id.search_widget_business_cat:
                configureSearchButton();
                configureSwitchWidget();
                break;
            case R.id.search_widget_entrepreneurs_cat:
                configureSearchButton();
                configureSwitchWidget();
                break;
            case R.id.search_widget_politics_cat:
                configureSearchButton();
                configureSwitchWidget();
                break;
            case R.id.search_widget_sports_cat:
                configureSearchButton();
                configureSwitchWidget();
                break;
            case R.id.search_widget_travel_cat:
                configureSearchButton();
                configureSwitchWidget();
                break;
        }
    }

    /**
     * Method to change state of search button (enable or disable
     */
    private void configureSearchButton() {
        if (checkBoxAreChecked()) {
            Editable editable = mEditTextQuery.getText();
            if (editable != null) {
                String text = editable.toString();
                if (!text.isEmpty()) {
                    mButtonSearch.setHasTransientState(true);
                    mButtonSearch.setEnabled(true);
                }
            }
        } else {
            mButtonSearch.setHasTransientState(false);
            mButtonSearch.setEnabled(false);
        }
    }

    /**
     * Method to listening click on edit text and show date picker
     * @param beginDate First date to show in date picker
     */
    private void configureDateEditText(boolean beginDate) {
        Calendar calendar = Calendar.getInstance();
        int days = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // We create object DatePickerDialog
        DatePickerDialog dialog;
        if (beginDate) {
            // We create an instance of DatePickerDialog
            dialog = new DatePickerDialog(
                    this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListenerForBeginDate,
                    year, month, days
            );
        } else {
            // We create an instance of DatePickerDialog
            dialog = new DatePickerDialog(
                    this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListenerForEndDate,
                    year, month, days
            );
        }
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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
            if (checkBoxAreChecked()) {
                mButtonSearch.setEnabled(true);
                mSwitch.setEnabled(true);
            }
        } else {
            mButtonSearch.setEnabled(false);
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
                if (this.notificationStateInNotificationActivity()) {
                    this.removeAlarmManager(100);
                    // we disable state of notification in NotificationActivity
                    this.disableNotificationInNotificationActivity();
                }
                // We configure broadcast receiver with alarm manager
                this.configureNotificationBroadcast(1, 0, 0, 100);
            }
        } else {
            // We remove alarm manager
            this.removeAlarmManager(100);
            mNotificationState = false;
        }
    }

    private boolean notificationStateInNotificationActivity() {
        SharedPreferences preferences = getSharedPreferences("NotificationActivity", MODE_PRIVATE);
        return preferences.getBoolean(EXTRA_NOTIFICATION_STATE, false);
    }

    private void disableNotificationInNotificationActivity() {
        SharedPreferences preferences = getSharedPreferences("NotificationActivity", MODE_PRIVATE);
        preferences.edit().putBoolean(EXTRA_NOTIFICATION_STATE, false).apply();
    }
}
