package com.noumsi.christian.mynews.controller.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.noumsi.christian.mynews.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.noumsi.christian.mynews.utils.Constants.EXTRA_BEGIN_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_END_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_FQ;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_QUERY_TERM;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    @BindView(R.id.search_widget_edit_text_query) EditText mEditTextQuery;
    @BindView(R.id.search_widget_edit_text_date_start) EditText mEditTextDateStart;
    @BindView(R.id.search_widget_edit_text_date_end) EditText mEditTextDateEnd;
    @BindView(R.id.search_widget_arts_cat) CheckBox mCheckBoxArtsCat;
    @BindView(R.id.search_widget_business_cat) CheckBox mCheckBoxBusinessCat;
    @BindView(R.id.search_widget_entrepreneurs_cat) CheckBox mCheckBoxEntrepreneursCat;
    @BindView(R.id.search_widget_politics_cat) CheckBox mCheckBoxPoliticsCat;
    @BindView(R.id.search_widget_sports_cat) CheckBox mCheckBoxSportsCat;
    @BindView(R.id.search_widget_travel_cat) CheckBox mCheckBoxTravelCat;
    @BindView(R.id.activity_search_search_button) Button mButtonSearch;

    private DatePickerDialog.OnDateSetListener mDateSetListenerForBeginDate, mDateSetListenerForEndDate;
    private SimpleDateFormat simpleDateFormat;
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

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

        // Configure datapickerdialog for datasetlistener
        configureDataSetListener();

        // We set click listener on checkbox
        mCheckBoxArtsCat.setOnClickListener(this);
        mCheckBoxBusinessCat.setOnClickListener(this);
        mCheckBoxEntrepreneursCat.setOnClickListener(this);
        mCheckBoxPoliticsCat.setOnClickListener(this);
        mCheckBoxSportsCat.setOnClickListener(this);
        mCheckBoxTravelCat.setOnClickListener(this);

        // set on click listener on search button
        mButtonSearch.setOnClickListener(this);
    }

    // We configure listener for DatePickerDialog
    private void configureDataSetListener() {
        mDateSetListenerForBeginDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                mEditTextDateStart.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        mDateSetListenerForEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                mEditTextDateEnd.setText(simpleDateFormat.format(calendar.getTime()));
            }
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
                break;
            case R.id.search_widget_business_cat:
                configureSearchButton();
                break;
            case R.id.search_widget_entrepreneurs_cat:
                configureSearchButton();
                break;
            case R.id.search_widget_politics_cat:
                configureSearchButton();
                break;
            case R.id.search_widget_sports_cat:
                configureSearchButton();
                break;
            case R.id.search_widget_travel_cat:
                configureSearchButton();
                break;
        }
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

    private void configureDateEditText(boolean beginDate) {
        Calendar calendar = Calendar.getInstance();
        int days = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // We create object DatePickerDialog
        DatePickerDialog dialog = null;
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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
                mButtonSearch.setEnabled(true);
        } else mButtonSearch.setEnabled(false);
    }
}
