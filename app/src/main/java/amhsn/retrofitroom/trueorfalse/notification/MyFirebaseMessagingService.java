package amhsn.retrofitroom.trueorfalse.notification;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.List;
import java.util.Map;
import java.util.Objects;

import amhsn.retrofitroom.trueorfalse.R;
import amhsn.retrofitroom.trueorfalse.login.LoginActivity;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i(getString(R.string.DEBUG_TAG), "New token: " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (isPackageInstalled("com.amhsn.olgor")) {
            Log.i(getString(R.string.DEBUG_TAG), "onMessageReceived: called");
            Log.i(getString(R.string.DEBUG_TAG), "onMessageReceived: Message received from: " + remoteMessage.getFrom());

            if (remoteMessage.getData().size() > 0) {
                Log.i(getString(R.string.DEBUG_TAG), "onMessageReceived: Data size: " + remoteMessage.getData().size());

                for (String key : remoteMessage.getData().keySet()) {
                    Log.i(getString(R.string.DEBUG_TAG), "onMessageReceived: " + key + " Data: " + remoteMessage.getData().toString());

                    Map<String, String> extraData = remoteMessage.getData();

                    String brandId = extraData.get("key1");
                    Log.i(getString(R.string.DEBUG_TAG), "onMessageReceived: brandId: " + brandId);
                }

//            Log.i(getString(R.string.DEBUG_TAG), "onMessageReceived: Data: " + remoteMessage.getData().toString());


            }

            if (remoteMessage.getNotification() != null) {
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

}
