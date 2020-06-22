package amhsn.retrofitroom.trueorfalse;


import android.content.Context;
import android.media.MediaPlayer;


public class ClassSound {

	public static void PlaySound(Context context) {

		MediaPlayer mp = MediaPlayer.create(context, R.raw.sound);
		mp.start();
	}
	
}
