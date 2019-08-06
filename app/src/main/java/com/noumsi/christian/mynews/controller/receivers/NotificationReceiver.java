package com.noumsi.christian.mynews.controller.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.noumsi.christian.mynews.R;
import com.noumsi.christian.mynews.webservices.searcharticle.Search;
import com.noumsi.christian.mynews.webservices.searcharticle.SearchArticleCall;
import com.noumsi.christian.mynews.webservices.searcharticle.SearchArticleResponse;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static com.noumsi.christian.mynews.utils.Constants.EXTRA_BEGIN_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_FQ;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_NAME_ACTIVITY;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_QUERY_TERM;

public class NotificationReceiver extends BroadcastReceiver implements SearchArticleCall.Callbacks{

    String mQueryTerm, mBeginDate, mEndDate, mFQ;
    private SimpleDateFormat mSimpleDateFormat;
    private static final String TAG = "NotificationReceiver";
    private String MY_CHANNEL = "my_news", mNamePreferences;
    WeakReference<Context> mContextWeakReference;
    Intent mIntent;
    protected SharedPreferences mSharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: start");
        mContextWeakReference = new WeakReference<>(context);
        mIntent = intent;

        // We set date format
        mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);

        if (intent != null) {
            // We get extra in intent
            this.loadIntentExtras(intent);
            mSharedPreferences = context.getSharedPreferences(mNamePreferences, Context.MODE_PRIVATE);
            // We execute http request
            this.executeHTTPRequestWithRetrofit();
        }
    }

    private void loadIntentExtras(Intent intent) {
        if (intent.getExtras() != null) {
            mQueryTerm = intent.getExtras().getString(EXTRA_QUERY_TERM);
            mBeginDate = intent.getExtras().getString(EXTRA_BEGIN_DATE);
            mFQ = intent.getExtras().getString(EXTRA_FQ);
            mNamePreferences = intent.getExtras().getString(EXTRA_NAME_ACTIVITY);

            Log.d(TAG, "loadIntentExtras: " +
                    "Query : "+mQueryTerm+
                    ", Categories : "+mFQ+
                    ", Begin date : "+mBeginDate+
                    ", preference file name : "+mNamePreferences);
            return;
        }
        Log.i(TAG, "loadIntentExtras: extras intent is null");
    }

    private void executeHTTPRequestWithRetrofit() {
        /* We convert format date for research */
        String beginDate = null;

        try {
            beginDate = mSimpleDateFormat.format(Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(mBeginDate)));
            Log.d(TAG, "executeHTTPRequestWithRetrofit: Begin date : "+beginDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SearchArticleCall.fetchSearchArticle(this, mQueryTerm, mFQ, beginDate, null, mContextWeakReference.get().getString(R.string.api_key_nyt));
    }

    @Override
    public void onResponse(@Nullable Search search) {
        Log.i(TAG, "onResponse: Success");
        if (search != null) {
            SearchArticleResponse response = search.getResponse();
            if (response.getDocs().size() > 0) {
                Log.d(TAG, "onResponse: There is "+response.getMeta().getHits()+" article(s)");
                showNotification(response.getMeta().getHits());
                // We set new begin date for research
                this.modifyDateOfResearch();
                return;
            }
            Log.i(TAG, "onResponse: There is no article");
        } else
            Log.d(TAG, "onResponse: result is null");
    }

    /**
     * We modify last begin date to plus one days
     */
    private void modifyDateOfResearch() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
            PendingIntent pendingIntent;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Objects.requireNonNull(sdf.parse(mBeginDate)));
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 00);
            calendar.set(Calendar.AM_PM, Calendar.PM);
            calendar.add(Calendar.DATE, 1);
            String dateToString = sdf.format(calendar.getTime());

            Log.d(TAG, "modifyDateOfResearch: New Begin date : "+dateToString);

            // modify the pending intent
            mIntent.putExtra(EXTRA_BEGIN_DATE, dateToString);
            pendingIntent = PendingIntent.getBroadcast(mContextWeakReference.get(), 100, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // We save new date in preference for new alarm when we restart phone
            mSharedPreferences.edit().putString(EXTRA_BEGIN_DATE, dateToString).apply();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create notification to show number of articles found and
     * create a PendingIntent to open result in result activity
     * @param numberOfArticle to show in notification
     */
    private void showNotification(int numberOfArticle) {
        NotificationManager notificationManager = (NotificationManager) mContextWeakReference.get().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // We create channel
            assert notificationManager != null;
            createChannel(notificationManager);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContextWeakReference.get(), MY_CHANNEL)
                .setSmallIcon(R.drawable.ic_arrow_drop_down_black_24dp)
                .setContentTitle(mContextWeakReference.get().getString(R.string.notification_title))
                .setContentText(numberOfArticle + " " + mContextWeakReference.get().getString(R.string.notification_content_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        assert notificationManager != null;
        notificationManager.notify(100, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel(NotificationManager notificationManager) {
        NotificationChannel myChannel = new NotificationChannel(
                        MY_CHANNEL,
                        mContextWeakReference.get().getString(R.string.title_activity_main),
                        NotificationManager.IMPORTANCE_DEFAULT);

        notificationManager.createNotificationChannel(myChannel);
    }

    @Override
    public void onFailure() {
    }
}
