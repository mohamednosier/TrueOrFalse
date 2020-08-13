package amhsn.retrofitroom.trueorfalse;

import java.util.Timer;
import java.util.TimerTask;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import amhsn.retrofitroom.trueorfalse.login.LoginActivity;

public class SplashScreen extends Activity {

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.splashscreen);
	    
	    Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
	    ImageView mt_ImView = findViewById(R.id.mind_teasers_img);
	    
	    mt_ImView.startAnimation(anim);
	    
	    
	}
	
	public void onResume()
	{
		super.onResume();
		
		new Timer().schedule(new TimerTask() {
			public void run()
			{
				handler.sendEmptyMessage(0);
			}
		}, 3000);
	}
	
	public void onConfigurationChanged(Configuration newConfig)
    {
    	super.onConfigurationChanged(newConfig);
    }
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message message)
		{
			if (message.what == 0)
			{
				Intent mainIntent = new Intent(getBaseContext(), SplashActivity.class);
				startActivity(mainIntent);
			//	overridePendingTransition(R.anim.enter, R.anim.exit);
				finish();
			}
		}
	};

}
