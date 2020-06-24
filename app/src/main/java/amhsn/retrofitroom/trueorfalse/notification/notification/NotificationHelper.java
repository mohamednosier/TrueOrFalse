package amhsn.retrofitroom.trueorfalse.notification.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;


import com.google.firebase.messaging.RemoteMessage;

import amhsn.retrofitroom.trueorfalse.R;

class NotificationHelper {

    private static final String TAG = "NotificationHelper";
    private static int count = 0;

    static void sendNotification(RemoteMessage remoteMessage, Context context, String title, String messageBody) {

//        Intent intent = new Intent(context, Main2Activity.class);
//        String click_action = remoteMessage.getNotification().getClickAction();
        Intent intent = new Intent("com.google.firebase.MESSAGING_EVENT");
        // Set the Activity to start in a new, empty task
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = context.getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.player2_answer_correct)
                        .setContentTitle(context.getString(R.string.fcm_message))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setWhen(System.currentTimeMillis());
//                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


//    static void sendNotification(Context context,String msg) {
//        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        long notificatioId = System.currentTimeMillis();
//
//        Intent intent = new Intent(context, Main2Activity.class); // Here pass your activity where you want to redirect.
//
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, (int) (Math.random() * 100), intent, 0);
//
//        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
//            currentapiVersion = R.mipmap.ic_launcher_round;
//        } else{
//            currentapiVersion = R.mipmap.ic_launcher;
//        }
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
//                .setSmallIcon(currentapiVersion)
//                .setContentTitle(context.getResources().getString(R.string.app_name))
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//                .setContentText(msg)
//                .setAutoCancel(true)
//                .setPriority(Notification.PRIORITY_HIGH)
//                .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
//                .setContentIntent(contentIntent);
//        mNotificationManager.notify((int) notificatioId, notificationBuilder.build());
//    }


}
