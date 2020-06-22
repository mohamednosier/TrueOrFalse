package amhsn.retrofitroom.trueorfalse;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SplashActivity extends Activity {

	ImageView school, text, imgtf;
	Button btnSinglePlayer, btnMoreApps, btnMultiPlayer, btnAboutUs,
			btnSetting;

	Animation AnimBtnSinglePlayer, AnimBtnMoreApps, AnimBtnMultiPlayer,
			AnimBtnAboutUs, AnimBtnSetting;

	SharedPreferences pref;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();

		final Dialog d = new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.setContentView(R.layout.exit_layout);
		DisplayMetrics metrics = getBaseContext().getResources()
				.getDisplayMetrics();
		int h = metrics.widthPixels;
		int width = (h * 90) / 100;

		LinearLayout layout = (LinearLayout) d.findViewById(R.id.dialog_layout);
		layout.getLayoutParams().width = width;

		Button no = (Button) d.findViewById(R.id.no);
		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				d.dismiss();
			}
		});

		Button yes = (Button) d.findViewById(R.id.yes);
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				d.dismiss();
				finish();
			}
		});

		d.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		try {

			System.gc();

			setContentView(R.layout.activity_splash);

			pref = getApplicationContext().getSharedPreferences("prefname",
					MODE_PRIVATE);

			imgtf = (ImageView) findViewById(R.id.imgtf);

			btnSinglePlayer = (Button) findViewById(R.id.btnSinglePlayer);
			btnMoreApps = (Button) findViewById(R.id.btnMoreApps);
			btnMultiPlayer = (Button) findViewById(R.id.btnMultiPlayer);
			btnAboutUs = (Button) findViewById(R.id.btnAboutUs);
			btnSetting = (Button) findViewById(R.id.btnSetting);

			btnMoreApps.setVisibility(View.VISIBLE);
			btnMultiPlayer.setVisibility(View.VISIBLE);
			btnAboutUs.setVisibility(View.VISIBLE);
			btnSetting.setVisibility(View.VISIBLE);

			// animButtons();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Please Try Again",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void btnSinglePlayerOnClickListener(View view) {
		/*
		 * if (pref.getBoolean("isSound", false)) {
		 * ClassSound.PlaySound(getApplicationContext()); }
		 */
		try {
			if (GlobalVar.isSound) {
				ClassSound.PlaySound(getApplicationContext());
			}

			Intent intent = new Intent(getApplicationContext(),
					LevelActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.enter, R.anim.exit);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Please Try Again",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void btnMultiPlayerOnClickListener(View view) {
		/*
		 * if (pref.getBoolean("isSound", false)) {
		 * ClassSound.PlaySound(getApplicationContext()); }
		 */
		try {
			if (GlobalVar.isSound) {
				ClassSound.PlaySound(getApplicationContext());
			}
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.enter, R.anim.exit);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Please Try Again",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void btnAboutUsOnClickListener(View view) {
		/*
		 * if (pref.getBoolean("isSound", false)) {
		 * ClassSound.PlaySound(getApplicationContext()); }
		 */
		try {
			if (GlobalVar.isSound) {
				ClassSound.PlaySound(getApplicationContext());
			}
			Intent intent = new Intent(SplashActivity.this,
					AboutUsActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.enter, R.anim.exit);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Please Try Again",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void btnSettinOnClickListener(View view) {
		/*
		 * if (pref.getBoolean("isSound", false)) {
		 * ClassSound.PlaySound(getApplicationContext()); }
		 */
		try {
			if (GlobalVar.isSound) {
				ClassSound.PlaySound(getApplicationContext());
			}
			Intent intent = new Intent(SplashActivity.this,
					SettingsActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.enter, R.anim.exit);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Please Try Again",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void btnMoreAppsOnClickListener(View view) {
		/*
		 * if (pref.getBoolean("isSound", false)) {
		 * ClassSound.PlaySound(getApplicationContext()); }
		 */
		try {
			if (GlobalVar.isSound) {
				ClassSound.PlaySound(getApplicationContext());
			}
			/*
			 * Intent intent = new Intent(SplashActivity.this, setting.class);
			 * startActivity(intent); overridePendingTransition(R.anim.enter,
			 * R.anim.exit);
			 */

			// String str = "https://play.google.com/store/apps/developer?id=";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
			Intent i = new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://search?q=pub:ExpertGames"));
			startActivity(i);
			overridePendingTransition(R.anim.enter, R.anim.exit);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Please Try Again",
					Toast.LENGTH_SHORT).show();
		}

	}

	private void animButtons() {

		// -- Button 1
		AnimBtnSinglePlayer = AnimationUtils.loadAnimation(this,
				R.anim.move_right);

		AnimBtnSinglePlayer.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				btnMoreApps.setVisibility(View.VISIBLE);
				btnMoreApps.clearAnimation();
				btnMoreApps.startAnimation(AnimBtnMoreApps);
			}
		});

		btnSinglePlayer.clearAnimation();
		btnSinglePlayer.startAnimation(AnimBtnSinglePlayer);
		// -- Button 1

		// -- Button 2
		AnimBtnMoreApps = AnimationUtils.loadAnimation(this, R.anim.move_left);

		AnimBtnMoreApps.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				btnMultiPlayer.setVisibility(View.VISIBLE);
				btnMultiPlayer.clearAnimation();
				btnMultiPlayer.startAnimation(AnimBtnMultiPlayer);
			}
		});
		// -- Button 2

		// -- Button 3
		AnimBtnMultiPlayer = AnimationUtils.loadAnimation(this,
				R.anim.move_right);

		AnimBtnMultiPlayer.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				btnAboutUs.setVisibility(View.VISIBLE);
				btnAboutUs.clearAnimation();
				btnAboutUs.startAnimation(AnimBtnAboutUs);
			}
		});
		// -- Button 3

		// -- Button 4
		AnimBtnAboutUs = AnimationUtils.loadAnimation(this, R.anim.move_left);

		AnimBtnAboutUs.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				btnSetting.setVisibility(View.VISIBLE);
				btnSetting.clearAnimation();
				btnSetting.startAnimation(AnimBtnSetting);
			}
		});
		// -- Button 4

		// -- Button 5
		AnimBtnSetting = AnimationUtils.loadAnimation(this, R.anim.move_right);
		// -- Button 5
	}

}
