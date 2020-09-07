package amhsn.retrofitroom.trueorfalse.notification;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import amhsn.retrofitroom.trueorfalse.R;

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();

    MyAppsNotificationManager  myAppsNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        myAppsNotificationManager = MyAppsNotificationManager.getInstance(this);

        myAppsNotificationManager.registerNotificationChannelChannel(
                getString(R.string.NEWS_CHANNEL_ID),
                getString(R.string.CHANNEL_NEWS),
                getString(R.string.CHANNEL_DESCRIPTION));

        FirebaseMessaging.getInstance().isAutoInitEnabled();

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat")
        DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
        Log.i(TAG, "TimeZone: "+localTime);


        switch (localTime) {
            case "+0000":
            case "+0100":
            case "+0200":

                FirebaseMessaging.getInstance().subscribeToTopic("1")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Log.i(TAG, "Topic Subscribed 1");
                                } else {
                                    Log.i(TAG, "Subscription 1 failed");
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("2")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 2", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Subscription 2 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("3")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 3", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 3 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("4")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 4", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 4 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("5")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 5", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 5 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                break;
            case "+0300":
            case "+0400":
            case "+0500":
                FirebaseMessaging.getInstance().subscribeToTopic("2")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Log.i(TAG, "Topic Subscribed 2");
                                } else {
                                    Log.i(TAG, "Subscription 2 failed");
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("1")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 1", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Subscription 1 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("3")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 3", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 3 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("4")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 4", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 4 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("5")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 5", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 5 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                break;
            case "+0600":
            case "+0700":
            case "+0800":
                FirebaseMessaging.getInstance().subscribeToTopic("3")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic subscribed 2", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Action failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("1")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 1", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Subscription 1 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("2")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 2", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 2 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("4")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 4", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 4 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("5")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 5", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 5 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                break;
            case "+0900":
            case "+1000":
            case "+1100":
                FirebaseMessaging.getInstance().subscribeToTopic("4")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic subscribed 2", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Action failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("1")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 1", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Subscription 1 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("2")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 2", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 2 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("3")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 3", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 3 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("5")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 5", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 5 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                break;
            case "+1200":
            case "+1300":
            case "+1400":
                FirebaseMessaging.getInstance().subscribeToTopic("5")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic subscribed 2", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Action failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                FirebaseMessaging.getInstance().unsubscribeFromTopic("1")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 1", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Subscription 1 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("2")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 2", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 2 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("3")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 3", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 3 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("4")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Topic UnSubscribed 4", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "UnSubscription 4 failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                break;
        }

//        if (!isPackageInstalled("com.amhsn.olgor")) {
//
//                FirebaseMessaging.getInstance().subscribeToTopic("pro")
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(getApplicationContext(), "Topic Subscribed", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(getApplicationContext(), "Subscription failed", Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
//                        });
//
//        }else {
//            FirebaseMessaging.getInstance().unsubscribeFromTopic("pro")
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            if(task.isSuccessful()){
//                                Toast.makeText(getApplicationContext(), "Topic Unsubscribed", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(getApplicationContext(), "Action failed", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    });
//        }

        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String mToken = instanceIdResult.getToken();
                    Log.d(TAG, "onSuccess: mToken: "+mToken);
                }
            });
        }catch (Exception ex){
            Log.d(TAG, "catch: message error: "+ex.getMessage());
        }
    }

    public void triggerNotification(Class targetNotificationActivity, String channelId, String title, String text, String bigText, int priority, boolean autoCancel, int notificationId, int pendingIntentFlag){
        myAppsNotificationManager.triggerNotification(targetNotificationActivity,channelId,title,text, bigText, priority, autoCancel,notificationId, pendingIntentFlag);
    }

    public void triggerNotification(Class targetNotificationActivity, String channelId, String title, String text, String bigText, int priority, boolean autoCancel, int notificationId){
        myAppsNotificationManager.triggerNotification(targetNotificationActivity,channelId,title,text, bigText, priority, autoCancel,notificationId);
    }

    public void triggerNotificationWithBackStack(Class targetNotificationActivity, String channelId, String title, String text, String bigText, int priority, boolean autoCancel, int notificationId, int pendingIntentFlag){
        myAppsNotificationManager.triggerNotificationWithBackStack(targetNotificationActivity,channelId,title,text, bigText, priority, autoCancel,notificationId, pendingIntentFlag);
    }

    public void updateNotification(Class targetNotificationActivity,String title,String text, String channelId, int notificationId, String bigpictureString, int pendingIntentflag){
        myAppsNotificationManager.updateWithPicture(targetNotificationActivity, title, text, channelId, notificationId, bigpictureString, pendingIntentflag);
    }

    public void cancelNotification(int notificaitonId){
        myAppsNotificationManager.cancelNotification(notificaitonId);
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

    private void getDeviceToken(){
        // Get token
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }


                        // Get new Instance ID token
                        String token = Objects.requireNonNull(task.getResult()).getToken();

                        // Log and toast
                        Log.w(TAG, "getInstanceId sucess: "+token, task.getException());

//						String msg = getString(R.string.msg_token_fmt, token);
//						Log.d(TAG, msg);
//						Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
        // [END retrieve_current_token]
    }




}
