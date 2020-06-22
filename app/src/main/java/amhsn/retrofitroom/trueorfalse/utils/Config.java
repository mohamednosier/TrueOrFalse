package amhsn.retrofitroom.trueorfalse.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {

	public static String GENERAL_CONFIG = "mp3_config";
	
	public static long LAST_TIME;
	
	public static void load(Context context) {
		
		SharedPreferences settings = context.getSharedPreferences(GENERAL_CONFIG, Context.MODE_PRIVATE);
		LAST_TIME = settings.getLong("last_time", 0);
		
		save(context);
	}
	
	public static void save(Context context) {
		SharedPreferences settings = context.getSharedPreferences(GENERAL_CONFIG, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putLong("last_time", LAST_TIME);
		
		editor.commit();
	}
}
