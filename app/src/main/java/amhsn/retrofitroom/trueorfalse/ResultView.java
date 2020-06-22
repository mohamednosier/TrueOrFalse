package amhsn.retrofitroom.trueorfalse;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

//@SuppressLint("NewApi")
public class ResultView extends View {
	
	Bitmap mBitmapSad= BitmapFactory.decodeResource(getResources(),R.drawable.emoticon_sad_back);
	Bitmap mBitmapHappy= BitmapFactory.decodeResource(getResources(),R.drawable.emoticon_happy_back);
	
	Bitmap mBitmapSadRotate= BitmapFactory.decodeResource(getResources(),R.drawable.emoticon_sad_back_rotate);
	Bitmap mBitmapHappyRotate= BitmapFactory.decodeResource(getResources(),R.drawable.emoticon_happy_back_rotate);

	private Rect mDisplaySize = new Rect();
	Dialog dialog;

	public ResultView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ResultView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ResultView(Context context, AttributeSet attrs) {
		super(context, attrs);

		/*WindowManager wm = (WindowManager) this.getContext().getSystemService(
				Context.WINDOW_SERVICE);

		Display display = wm.getDefaultDisplay();
		display.getRectSize(mDisplaySize);*/
	}

	@SuppressLint({"DrawAllocation", "LongLogTag"})
	@Override
	protected void onDraw(Canvas canvas) {

		Paint p = new Paint();
		/* p.setColor(Color.parseColor("#5c8037")); */
		p.setColor(Color.BLACK);

		Typeface tfQuestion = Typeface.createFromAsset(getContext().getAssets(), "HOBOSTD.OTF");

		Typeface tfFromanBold = Typeface.create(tfQuestion, Typeface.BOLD);
		p.setTypeface(tfFromanBold);
		
		float density = 1.0F;
		WindowManager wm = (WindowManager) this.getContext().getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		display.getMetrics(localDisplayMetrics);
		density = localDisplayMetrics.density;

		int screenwidth = canvas.getWidth();
		int screenheight = canvas.getHeight();

		if (screenwidth < 320 || screenheight < 480) {
			p.setTextSize(16);
		} else {
			p.setTextSize(22);
		}

		switch (localDisplayMetrics.densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			p.setTextSize(15);
			Log.i("TextSize -- DENSITY_LOW", "15");
			break;

		case DisplayMetrics.DENSITY_MEDIUM:
			p.setTextSize(38);
			Log.i("TextSize -- DENSITY_MEDIUM", "28");
			break;

		case DisplayMetrics.DENSITY_HIGH:
			p.setTextSize(40);
			Log.i("TextSize -- DENSITY_HIGH", "30");
			break;

		case DisplayMetrics.DENSITY_XHIGH:
			p.setTextSize(50);
			Log.i("TextSize -- DENSITY_XHIGH", "40");
			break;

		case DisplayMetrics.DENSITY_XXHIGH:
			p.setTextSize(60);
			Log.i("TextSize -- DENSITY_XXHIGH", "50");
			break;
		default:
			p.setTextSize(22);
			Log.i("TextSize -- default", "22");
			break;

		}

		canvas.save();
		
		if (GlobalVar.player2Score > GlobalVar.player1Score)
		{
			canvas.drawBitmap(mBitmapHappy, canvas.getWidth()/2 - mBitmapHappy.getWidth()/2, canvas.getHeight() / 2 + canvas.getHeight()/16, p);
			canvas.drawBitmap(mBitmapSadRotate,canvas.getWidth()/2 - mBitmapSadRotate.getWidth()/2, canvas.getHeight() / 2 - canvas.getHeight()/16f-mBitmapSadRotate.getHeight(), p);
		}
		else
		{
			canvas.drawBitmap(mBitmapSad,canvas.getWidth()/2 - mBitmapSad.getWidth()/2, canvas.getHeight() / 2 + canvas.getHeight()/16, p);
			canvas.drawBitmap(mBitmapHappyRotate, canvas.getWidth()/2 - mBitmapHappyRotate.getWidth()/2, canvas.getHeight() / 2 - canvas.getHeight()/16f-mBitmapHappyRotate.getHeight(), p);
		}

		
		canvas.save();
		
		
		TextPaint tpWL1 = new TextPaint(p);

		canvas.translate(0,
				canvas.getHeight() / 2 + canvas.getHeight() / 5);
		StaticLayout slWL1 = new StaticLayout(
				GlobalVar.player2Score > GlobalVar.player1Score ? "You Won By"
						: "You Lost By", tpWL1, canvas.getWidth(),
				Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
		slWL1.draw(canvas);

		canvas.restore();
		canvas.save();

		TextPaint tp = new TextPaint(p);

		canvas.translate(0,
				canvas.getHeight() / 2 + canvas.getHeight() / 10);
		StaticLayout sl = new StaticLayout(
				GlobalVar.player2Score > GlobalVar.player1Score ? "Congratulations!!!"
						: "Ooops!!!", tp, canvas.getWidth(),
				Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
		sl.draw(canvas);

		canvas.restore();
		canvas.save();
		

		TextPaint tpTap1 = new TextPaint(p);
		canvas.translate(0,
				canvas.getHeight() / 2 + canvas.getHeight() / 2.5f);
		
		StaticLayout s1tap = new StaticLayout(
				"Tap To Continue", tpTap1, canvas.getWidth(),
				Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
		s1tap.draw(canvas);

		canvas.restore();
		canvas.save();
		
		canvas.drawText(Integer.toString(GlobalVar.player2Score) + ":"
				+ Integer.toString(GlobalVar.player1Score),
				canvas.getWidth() / 2.2f,
				canvas.getHeight() / 2 + canvas.getHeight() / 3.5f, p);//20

		// Draw Line
		canvas.drawLine(0, canvas.getHeight() / 2, canvas.getWidth(),
				canvas.getHeight() / 2, p);

		canvas.save();

		// Player 1......................

		p.setColor(Color.WHITE);
		
		TextPaint tpWL2 = new TextPaint(p);
		
		canvas.rotate(180, (canvas.getWidth() + mDisplaySize.width()) / 2,
				canvas.getHeight() / 2 - canvas.getHeight() / 5);
		
		canvas.translate(0,
				canvas.getHeight() / 2 - canvas.getHeight() / 5);
		StaticLayout slWL2 = new StaticLayout(
				GlobalVar.player1Score > GlobalVar.player2Score ? "You Won By"
						: "You Lost By", tpWL2, canvas.getWidth(),
				Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
		slWL2.draw(canvas);

		
		canvas.restore();
		canvas.save();
		

		canvas.rotate(180, canvas.getWidth() / 1.8f, canvas.getHeight() / 2
				- canvas.getHeight() / 3.5f);

		canvas.drawText(Integer.toString(GlobalVar.player1Score) + ":"
				+ Integer.toString(GlobalVar.player2Score),

		canvas.getWidth() / 1.8f, canvas.getHeight() / 2 - canvas.getHeight()
				/ 3.5f, p);

		canvas.restore();
		canvas.save();
		
		TextPaint tpTap = new TextPaint(p);
		canvas.rotate(180, (canvas.getWidth() + mDisplaySize.width()) / 2,
				canvas.getHeight() / 2 - canvas.getHeight() / 2.5f);

		canvas.translate(0,
				canvas.getHeight() / 2 - canvas.getHeight() / 2.5f);

		StaticLayout s2Tap = new StaticLayout(
				"Tap To Continue", tpTap, canvas.getWidth(),
				Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
		s2Tap.draw(canvas);

		canvas.restore();
		canvas.save();
		
		

		TextPaint tp1 = new TextPaint(p);

		canvas.rotate(180, (canvas.getWidth() + mDisplaySize.width()) / 2,
				canvas.getHeight() / 2 - canvas.getHeight() / 10);

		canvas.translate(0,
				canvas.getHeight() / 2 - canvas.getHeight() / 10);

		StaticLayout s2 = new StaticLayout(
				GlobalVar.player1Score > GlobalVar.player2Score ? "Congratulations!!!"
						: "Ooops!!!", tp1, canvas.getWidth(),
				Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
		s2.draw(canvas);

		canvas.restore();
		
		
	}

}
