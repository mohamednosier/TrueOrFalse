package amhsn.retrofitroom.trueorfalse.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import amhsn.retrofitroom.trueorfalse.config.TrueOrFalseConfig;
import amhsn.retrofitroom.trueorfalse.config.TrueOrFalseOS;


public class Actions {
	private static final String APP_PNAME_PAID = "com.egames.game.tnf";
	private static final String BB10_TOF_FREE_ID = "";
	private static final String BB10_TOF_PAID_ID = "";

	/**
	 * Action Review
	 */
	public static void giveUsReview(Context context) {
		if (TrueOrFalseOS.VERSION == TrueOrFalseOS.ANDROID_VERSION) {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("market://details?id=" + APP_PNAME_PAID)));
		} else if (TrueOrFalseOS.VERSION == TrueOrFalseOS.BB10_VERSION
				&& TrueOrFalseConfig.VERSION == TrueOrFalseConfig.FREE_VERSION) {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("http://appworld.blackberry.com/webstore/content/"
							+ BB10_TOF_FREE_ID)));
		} else if (TrueOrFalseOS.VERSION == TrueOrFalseOS.BB10_VERSION
				&& TrueOrFalseConfig.VERSION == TrueOrFalseConfig.PAID_VERSION) {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("http://appworld.blackberry.com/webstore/content/"
							+ BB10_TOF_PAID_ID)));
		}
	}

	public static void shareApp(Context context) {
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "\nVisit the links from your Android device.\n\nTrue or False (https://play.google.com/store/apps/details?id=com.egames.game.tnf)";
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
				" I recommend 'True or False'");
		sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
		context.startActivity(Intent.createChooser(sharingIntent, "Share App"));
	}

	// public static String getFacebookLinkUrl() {
	// String url =
	// "https://play.google.com/store/apps/details?id=com.hzed.hadith&hl=en";
	// String url =
	// "https://play.google.com/store/apps/details?id=net.xelnaga.exchanger&hl=en";
	// return url;
	// }
}
