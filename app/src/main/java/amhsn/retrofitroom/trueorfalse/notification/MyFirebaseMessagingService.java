package amhsn.retrofitroom.trueorfalse.notification;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import amhsn.retrofitroom.trueorfalse.R;
import amhsn.retrofitroom.trueorfalse.login.LoginActivity;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    static String timeNotification = "";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i(getString(R.string.DEBUG_TAG), "New token: " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        if (isPackageInstalled("com.amhsn.olgor")) {
        Log.i(getString(R.string.DEBUG_TAG), "onMessageReceived: called");
        Log.i(getString(R.string.DEBUG_TAG), "onMessageReceived: Message received from: " + remoteMessage.getFrom());
        Long time = remoteMessage.getSentTime();
//        Log.d(TAG, "onMessageReceived: time: " + getDate(time));
        if (remoteMessage.getData().size() > 0) {
            Log.i(getString(R.string.DEBUG_TAG), "onMessageReceived: Data size: " + remoteMessage.getData().size());

            for (String key : remoteMessage.getData().keySet()) {
                Log.i(getString(R.string.DEBUG_TAG), "onMessageReceived: " + key + " Data: " + remoteMessage.getData().toString());

                Map<String, String> extraData = remoteMessage.getData();

                String brandId = extraData.get("key1");
                timeNotification = extraData.get("time");
                Log.i(getString(R.string.DEBUG_TAG), "onMessageReceived: brandId: " + brandId);
                Log.i(getString(R.string.DEBUG_TAG), "onMessageReceived: timeNotification: " + timeNotification);
            }

//            Log.i(getString(R.string.DEBUG_TAG), "onMessageReceived: Data: " + remoteMessage.getData().toString());

        }

        if (remoteMessage.getNotification() != null) {

            Log.d(TAG, "onMessageReceived: timeNotification : "+timeNotification+"   ,"+getDate());
                if (timeNotification.equals("17")) {
                    MyApplication application = (MyApplication) getApplicationContext();

                    application.triggerNotificationWithBackStack(LoginActivity.class,
                            getString(R.string.NEWS_CHANNEL_ID),
                            Objects.requireNonNull(remoteMessage.getNotification()).getTitle(),
                            remoteMessage.getNotification().getBody(),
                            remoteMessage.getNotification().getBody(),
                            NotificationCompat.PRIORITY_HIGH,
                            true,
                            getResources().getInteger(R.integer.notificationId),
                            PendingIntent.FLAG_UPDATE_CURRENT);
                }
        }
//        }
    }


    public boolean isPackageInstalled(String packageName) {
        final PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return false;
        }
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

//    private String getDate(long time) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(time * 1000);
//        String date = DateFormat.format("h:mm a", cal).toString();
//        return date;
//    }

    private String getDate() {
        Calendar calander = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        String time1 = simpleDateFormat.format(calander.getTime());
        return time1;
    }

    private static boolean isForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : tasks) {
            if (ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == appProcess.importance && packageName.equals(appProcess.processName)) {
                return true;
            }
        }
        return false;
    }

    private void notificationDialog() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Tutorialspoint")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("sample notification")
                .setContentText("This is sample notification")
                .setContentInfo("Information");
        notificationManager.notify(1, notificationBuilder.build());
    }
}
