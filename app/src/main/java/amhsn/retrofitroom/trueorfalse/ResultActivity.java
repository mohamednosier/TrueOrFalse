package amhsn.retrofitroom.trueorfalse;

import java.util.Arrays;
import java.util.Collections;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {

            setContentView(R.layout.activity_result);

            overridePendingTransition(R.anim.enter, R.anim.exit);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Try AgainA",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            // --- Shuffle array
            GlobalVar.array = new Integer[GlobalVar.TOTAL_QUESTION];

            for (int j = 0; j < GlobalVar.array.length; j++) {
                GlobalVar.array[j] = j;
            }

            Collections.shuffle(Arrays.asList(GlobalVar.array));

            // --- Shuffle array

            Intent intent = new Intent(ResultActivity.this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            GlobalVar.i = 0;
            GlobalVar.player1Score = 0;
            GlobalVar.player2Score = 0;

            startActivity(intent);

            overridePendingTransition(R.anim.enter, R.anim.exit);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Try AgainB",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            if (GlobalVar.isSound) {
                ClassSound.PlaySound(getApplicationContext());
            }

            GlobalVar.array = new Integer[GlobalVar.TOTAL_QUESTION];

            for (int j = 0; j < GlobalVar.array.length; j++) {
                GlobalVar.array[j] = j;
            }

            Collections.shuffle(Arrays.asList(GlobalVar.array));

            // --- Shuffle array

            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            GlobalVar.i = 0;
            GlobalVar.player1Score = 0;
            GlobalVar.player2Score = 0;

            startActivity(intent);

            overridePendingTransition(R.anim.enter, R.anim.exit);
            return true;

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Try AgainC",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
