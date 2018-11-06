package com.noumsi.christian.mynews.controller.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;

import com.noumsi.christian.mynews.R;

import butterknife.BindView;

import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_ART;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_BUSINESS;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_ENTREPRENEURS;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_POLITIC;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_SPORT;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_CHECKBOX_TRAVEL;
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
    protected SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
