package amhsn.retrofitroom.trueorfalse;


import android.content.Context;
import android.media.MediaPlayer;


public class ClassSound {

    static MediaPlayer mp;

    public static void PlaySound(Context context) {

        mp = MediaPlayer.create(context, R.raw.sound);
        mp.start();
    }

    public static void PlayCorrect(Context context) {

        mp = MediaPlayer.create(context, R.raw.correct_sound);
        mp.start();
    }

    public static void PlayWrong(Context context) {

        mp = MediaPlayer.create(context, R.raw.wrong_sound);
        mp.start();
    }

    public static void PlayCountdown(Context context) {

        mp = MediaPlayer.create(context, R.raw.countdown_sound);
        mp.start();
    }

	public static void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

}
