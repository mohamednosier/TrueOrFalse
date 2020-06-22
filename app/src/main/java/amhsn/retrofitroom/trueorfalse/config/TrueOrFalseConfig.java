package amhsn.retrofitroom.trueorfalse.config;

import android.content.Context;
import android.content.SharedPreferences;

public class TrueOrFalseConfig {

    public static final int PAID_VERSION = 0;
    public static final int FREE_VERSION = 1;
    public static int VERSION = FREE_VERSION;

	public static String KIDS_ALPHABET_CONFIG = "true_or_false_config";
	
	public static int FONT_SIZE = 12;
	public static int LIMIT_NUMBER = 10;
	
	public static void load(Context context) {
		SharedPreferences settings = context.getSharedPreferences(KIDS_ALPHABET_CONFIG, Context.MODE_PRIVATE);
		FONT_SIZE = settings.getInt("font_size", 12);
		LIMIT_NUMBER = settings.getInt("limit_number", 10);
		
		save(context);
	}
	
	public static void save(Context context) {
		SharedPreferences settings = context.getSharedPreferences(KIDS_ALPHABET_CONFIG, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("font_size", FONT_SIZE);
		editor.putInt("limit_number", LIMIT_NUMBER);
		editor.commit();
	}
	
}

