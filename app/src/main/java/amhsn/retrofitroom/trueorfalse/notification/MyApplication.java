package amhsn.retrofitroom.trueorfalse.notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.toolbox.NetworkImageView;
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

import amhsn.retrofitroom.trueorfalse.Constant;
import amhsn.retrofitroom.trueorfalse.GameRoom.GetOpponentActivity;
import amhsn.retrofitroom.trueorfalse.GameRoom.PlayerActivity;
import amhsn.retrofitroom.trueorfalse.MainActivity;
import amhsn.retrofitroom.trueorfalse.R;
import amhsn.retrofitroom.trueorfalse.SplashActivity;
import amhsn.retrofitroom.trueorfalse.helper.Utils;

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();

    MyAppsNotificationManager myAppsNotificationManager;

    // Gloabl declaration of variable to use in whole app

    public static boolean activityVisible; // Variable that will check the
    // current activity state

    public static boolean isActivityVisible() {
        return activityVisible; // return true or false
    }

    public static void activityResumed() {
        activityVisible = true;// this will set true when activity resumed

    }

    public static void activityPaused() {
        activityVisible = false;// this will set false when activity paused

    }

    public static boolean isConnected = false;
    private Activity mCurrentActivity = null;
    /**
     * To receive change in network state
     */
    private BroadcastReceiver NetworkStatusReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Internet Connection", "Internet Connection use1");
            Activity currentActivity = ((MyApplication) context.getApplicationContext()).getCurrentActivity();
            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();


            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


            if (!mWifi.isConnected()) {
                if (mCurrentActivity != null) {
                    Toast noInternetToast = Toast.makeText(getApplicationContext(),
                            " not available ", Toast.LENGTH_LONG);
                    noInternetToast.show();
                    showWinnerDialog();
//                    mCurrentActivity.finish();
                }
            }


            if (!isConnected) {
                if (mCurrentActivity != null) {
                    Toast noInternetToast = Toast.makeText(getApplicationContext(),
                            "not available1 ", Toast.LENGTH_LONG);
                    noInternetToast.show();
//                    mCurrentActivity.finish();
                }
            }
        }
    };


    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myAppsNotificationManager = MyAppsNotificationManager.getInstance(this);


        registerReceiver(NetworkStatusReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));


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
        Log.i(TAG, "TimeZone: " + localTime);


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
                    Log.d(TAG, "onSuccess: mToken: " + mToken);
                }
            });
        } catch (Exception ex) {
            Log.d(TAG, "catch: message error: " + ex.getMessage());
        }
    }

    public void triggerNotification(Class targetNotificationActivity, String channelId, String title, String text, String bigText, int priority, boolean autoCancel, int notificationId, int pendingIntentFlag) {
        myAppsNotificationManager.triggerNotification(targetNotificationActivity, channelId, title, text, bigText, priority, autoCancel, notificationId, pendingIntentFlag);
    }

    public void triggerNotification(Class targetNotificationActivity, String channelId, String title, String text, String bigText, int priority, boolean autoCancel, int notificationId) {
        myAppsNotificationManager.triggerNotification(targetNotificationActivity, channelId, title, text, bigText, priority, autoCancel, notificationId);
    }

    public void triggerNotificationWithBackStack(Class targetNotificationActivity, String channelId, String title, String text, String bigText, int priority, boolean autoCancel, int notificationId, int pendingIntentFlag) {
        myAppsNotificationManager.triggerNotificationWithBackStack(targetNotificationActivity, channelId, title, text, bigText, priority, autoCancel, notificationId, pendingIntentFlag);
    }

    public void updateNotification(Class targetNotificationActivity, String title, String text, String channelId, int notificationId, String bigpictureString, int pendingIntentflag) {
        myAppsNotificationManager.updateWithPicture(targetNotificationActivity, title, text, channelId, notificationId, bigpictureString, pendingIntentflag);
    }

    public void cancelNotification(int notificaitonId) {
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

    private void getDeviceToken() {
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
                        Log.w(TAG, "getInstanceId sucess: " + token, task.getException());

//						String msg = getString(R.string.msg_token_fmt, token);
//						Log.d(TAG, msg);
//						Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
        // [END retrieve_current_token]
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(NetworkStatusReceiver);
    }

    private void showWinnerDialog() {
//        try {



            final AlertDialog.Builder dialog1 = new AlertDialog.Builder(mCurrentActivity);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.winner_dialog, null);
            dialog1.setView(dialogView);
            dialog1.setCancelable(false);
            final AlertDialog alertDialog = dialog1.create();
            TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
            TextView tvMessage = dialogView.findViewById(R.id.tv_message);
            Button btnok = dialogView.findViewById(R.id.btn_ok);
            Button btnReBattle = dialogView.findViewById(R.id.btnReBattle);
            NetworkImageView winnerImg = dialogView.findViewById(R.id.winnerImg);
            String winner="";
            if (winner.equals("you")) {
                tvTitle.setText(getString(R.string.congrats));
//                tvMessage.setText(winnerMessage);
            } else {
                tvTitle.setText(getString(R.string.next_time));
                tvMessage.setText(R.string.next_time);
            }

            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    DatabaseReference databaseReference = myGameRef.child(gameId);
//                    if (databaseReference != null) {
//                        databaseReference.removeValue();
//                    }
                    mCurrentActivity.finish();
                    alertDialog.dismiss();
                }
            });



            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
