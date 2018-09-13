package com.noumsi.christian.mynews.controller.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.noumsi.christian.mynews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

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
        } else mSwitch.setEnabled(false);
    }
}
