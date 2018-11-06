package com.noumsi.christian.mynews.controller.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.noumsi.christian.mynews.R;
import com.noumsi.christian.mynews.controller.activities.SearchResultActivity;
import com.noumsi.christian.mynews.webservices.searcharticle.Search;
import com.noumsi.christian.mynews.webservices.searcharticle.SearchArticleCall;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.noumsi.christian.mynews.utils.Constants.EXTRA_BEGIN_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_END_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_FQ;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_QUERY_TERM;

public class NotificationReceiver extends BroadcastReceiver implements SearchArticleCall.Callbacks{

    String mQueryTerm, mBeginDate, mEndDate, mFQ;
    private SimpleDateFormat mSimpleDateFormat;
    private static final String TAG = "NotificationReceiver";
    private String MY_CHANNEL = "my_news", namePreferences = "NotificationActivity";
    Context mContext;
    Intent mIntent;
    protected SharedPreferences mSharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        mSharedPreferences = context.getSharedPreferences(namePreferences, Context.MODE_PRIVATE);
        mContext = context;
        mIntent = intent;

        // We set date format
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        if (intent != null) {
            // We get extra in intent
            this.loadIntentExtras(intent);
            // We execute http request
            this.executeHTTPRequestWithRetrofit();
        }
    }

    private void loadIntentExtras(Intent intent) {
        mQueryTerm = intent.getExtras().getString(EXTRA_QUERY_TERM);
        mBeginDate = intent.getExtras().getString(EXTRA_BEGIN_DATE);
        mFQ = intent.getExtras().getString(EXTRA_FQ);
    }

    private void executeHTTPRequestWithRetrofit() {
        /* We convert format date for research */
        String beginDate = null;

        try {
            beginDate = mSimpleDateFormat.format(new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(mBeginDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SearchArticleCall.fetchSearchArticle(this, mQueryTerm, mFQ, beginDate, null, mContext.getString(R.string.api_key_nyt));
    }

    @Override
    public void onResponse(@Nullable Search search) {
        Log.d(TAG, "Success");
        if (search != null) {
            if (search.getResponse().getDocs().size() > 0) {
                showNotification(search.getResponse().getDocs().size());
                // We set new begin date for research
                this.modifyDateOfResearch();
            }
        }
    }

    /**
     * We modify last begin date to plus one days
     */
    private void modifyDateOfResearch() {
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(mBeginDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 01);
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 00);
            calendar.set(Calendar.AM_PM, Calendar.PM);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            String dateToString = calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR);

            // modify the pending intent
            mIntent.putExtra(EXTRA_BEGIN_DATE, dateToString);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 100, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // We save new date in preference for new alarm when we restart phone
            mSharedPreferences.edit().putString(EXTRA_BEGIN_DATE, dateToString).apply();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create notification to show number of articles found and
     * create a PendingIntent to open result in result activity
     * @param numberOfArticle
     */
    private void showNotification(int numberOfArticle) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        /*
        Intent searchResult = new Intent(mContext, SearchResultActivity.class);
        searchResult.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        searchResult.putExtra(EXTRA_QUERY_TERM, mQueryTerm);
        searchResult.putExtra(EXTRA_BEGIN_DATE, mBeginDate);
        searchResult.putExtra(EXTRA_END_DATE, "");
        searchResult.putExtra(EXTRA_FQ, mFQ);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, searchResult, PendingIntent.FLAG_UPDATE_CURRENT);
        */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // We create channel
            assert notificationManager != null;
            createChannel(notificationManager);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, MY_CHANNEL)
                //.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_arrow_drop_down_black_24dp)
                .setContentTitle("My News")
                .setContentText(numberOfArticle + " items found")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        assert notificationManager != null;
        notificationManager.notify(100, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel(NotificationManager notificationManager) {
        NotificationChannel myChannel = new NotificationChannel(
                        MY_CHANNEL,
                        mContext.getString(R.string.title_activity_main),
                        NotificationManager.IMPORTANCE_DEFAULT);

        notificationManager.createNotificationChannel(myChannel);
    }

    @Override
    public void onFailure() {
    }
}
