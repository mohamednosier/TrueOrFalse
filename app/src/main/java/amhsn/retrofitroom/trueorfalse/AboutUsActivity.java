package amhsn.retrofitroom.trueorfalse;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AboutUsActivity extends Activity {

	TextView txtAboutUs, lblAboutUs;

	@Override
	public void onBackPressed() {
		try {
			Intent intent = new Intent(AboutUsActivity.this,
					SplashActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			startActivity(intent);

			overridePendingTransition(R.anim.enter, R.anim.exit);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Please Try Again",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		try {
			setContentView(R.layout.activity_about_us);

			Typeface tfQuestion = Typeface.createFromAsset(
					AboutUsActivity.this.getAssets(), "HOBOSTD.OTF");

			Typeface tfFromanBold = Typeface.create(tfQuestion, Typeface.BOLD);

			/*txtAboutUs = (TextView) findViewById(R.id.txtAboutUs);
			txtAboutUs.setTypeface(tfQuestion);
			txtAboutUs.setTextColor(Color.BLACK);

			lblAboutUs = (TextView) findViewById(R.id.lblLevel);
			lblAboutUs.setTypeface(tfFromanBold);
			lblAboutUs.setTextColor(Color.WHITE);*/
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Please Try Again",
					Toast.LENGTH_SHORT).show();
		}
	}

}
