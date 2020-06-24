//package amhsn.retrofitroom.trueorfalse.notification.notification;
//
//import android.content.Intent;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.ResolveInfo;
//import android.net.Uri;
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//
//import java.util.List;
//
//import amhsn.retrofitroom.trueorfalse.R;
//
//public class NotificationActivity extends AppCompatActivity {
//
//    //1. Notification Channel
//    //2. Notification Builder
//    //3. Notification Manager
//
//    private static final String TAG = "NotificationActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notification);
//
//
////        if(isPackageInstalled("com.facebook.orca")) {
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                // Create channel to show notifications.
////                String channelId = getString(R.string.default_notification_channel_id);
////                String channelName = getString(R.string.default_notification_channel_name);
////                NotificationManager notificationManager = getSystemService(NotificationManager.class);
////                notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
////            }
//
//    }
//
//    public boolean isPackageExisted(String targetPackage){
//        List<ApplicationInfo> packages;
//        PackageManager pm;
//
//        pm = getPackageManager();
//        packages = pm.getInstalledApplications(0);
//        for (ApplicationInfo packageInfo : packages) {
//            if(packageInfo.packageName.equals(targetPackage))
//                return true;
//        }
//        return false;
//    }
//
//    public boolean isPackageInstalled(String packageName) {
//        final PackageManager packageManager = getPackageManager();
//        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
//        if (intent == null) {
//            return false;
//        }
//        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//        return list.size() > 0;
//    }
//
//    public void rateMe() {
////        try {
////            startActivity(new Intent(Intent.ACTION_VIEW,
////                    Uri.parse("market://details?id=" + getPackageName())));
////        } catch (ActivityNotFoundException e) {
//            startActivity(new Intent(Intent.ACTION_VIEW,
//                    Uri.parse("https://play.google.com/store")));
////        }
//    }
//
//}
