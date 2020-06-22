package amhsn.retrofitroom.trueorfalse;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import amhsn.retrofitroom.trueorfalse.utils.Actions;

public class SettingsActivity extends Activity {
	Button btnfacebook, btngoogle, btnshare, btnrate, btnsound;

	Typeface tfQuestion, tfFromanBold;

	TextView lblSettings;

	SharedPreferences pref;
	Editor editor;

	@Override
	public void onBackPressed() {
		try {
			Intent intent = new Intent(SettingsActivity.this, SplashActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			startActivity(intent);

			overridePendingTransition(R.anim.enter, R.anim.exit);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Please Try Again",	Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		try {

			setContentView(R.layout.setting);

			tfQuestion = Typeface.createFromAsset(SettingsActivity.this.getAssets(),
					"HOBOSTD.OTF");

			tfFromanBold = Typeface.create(tfQuestion, Typeface.BOLD);

			lblSettings = (TextView) findViewById(R.id.lblSettings);
			lblSettings.setTypeface(tfFromanBold);

			pref = getApplicationContext().getSharedPreferences("prefname",
					MODE_PRIVATE);
			editor = pref.edit();

			btnfacebook = (Button) findViewById(R.id.btnfacebook);
			btngoogle = (Button) findViewById(R.id.btngoogle);
			btnshare = (Button) findViewById(R.id.btnshare);
			btnrate = (Button) findViewById(R.id.btnrate);
			btnsound = (Button) findViewById(R.id.btnsound);
			
			if (GlobalVar.isSound) {
				btnsound.setBackgroundResource(R.drawable.btn_sound_on_selector);
			} else {
				btnsound.setBackgroundResource(R.drawable.btn_sound_off_selector);
			}

			
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Please Try Again",
					Toast.LENGTH_SHORT).show();
		}

	}
	

}