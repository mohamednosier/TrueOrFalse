package amhsn.retrofitroom.trueorfalse.notification.notification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

@SuppressLint({"Registered", "MissingFirebaseInstanceTokenRefresh"})
public class CloudMessagingService extends FirebaseMessagingService {

    private static final String TAG = "CloudMessagingService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        if (isPackageInstalled("com.facebook.katana")) {
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                String title = remoteMessage.getNotification().getTitle();
                String body = remoteMessage.getNotification().getBody();

                NotificationHelper.sendNotification(remoteMessage,getApplicationContext(), title,body);
                Log.d(TAG, "onMessageReceived: ");
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
}
