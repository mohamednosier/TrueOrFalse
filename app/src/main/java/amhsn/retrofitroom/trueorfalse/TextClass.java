package amhsn.retrofitroom.trueorfalse;


import android.annotation.SuppressLint;
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

public class TextClass extends View {

	int x = 400;
	int y = 100;
	int angle = 20, deg = 3, cnt = 0;
	boolean flag = true;
//	float density = 0.0f;

	Bitmap mBitmapWhiteLine = BitmapFactory.decodeResource(getResources(),
			R.drawable.white_divider);
	Bitmap mBitmapGreenLine = BitmapFactory.decodeResource(getResources(),
			R.drawable.green_divider);

	Typeface tfScore = Typeface.createFromAsset(getContext().getAssets(),
			"HOBOSTD.OTF");
	Typeface tfQuestion = Typeface.createFromAsset(getContext().getAssets(),
			"HOBOSTD.OTF");

	Typeface tfFromanBold = Typeface.create(tfQuestion, Typeface.BOLD);

	private Rect mDisplaySize = new Rect();
	
	private Context context;

	public TextClass(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TextClass(Context context, AttributeSet attrs) {
		super(context, attrs);

		/*WindowManager wm = (WindowManager) this.getContext().getSystemService(
				Context.WINDOW_SERVICE);*/

		/*Display display = wm.getDefaultDisplay();
		display.getRectSize(mDisplaySize);*/

		/*DisplayMetrics metrics = new DisplayMetrics();
		density = metrics.density;*/
//		display.getMetrics(metrics);

	}

	public TextClass(Context context) {
		super(context);
		this.context = context;

	}

	@SuppressLint({"DrawAllocation", "LongLogTag"})
	@Override
	protected void onDraw(Canvas canvas) {

		Paint p = new Paint();
		p.setColor(Color.WHITE);

		@SuppressWarnings("unused")
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
			p.setTextSize(100);
		} else if (screenwidth < 640 || screenheight < 960){
			p.setTextSize(100);
		} else {
			p.setTextSize(100);
		}

		switch (localDisplayMetrics.densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			p.setTextSize(100);
			Log.i("TextSize -- DENSITY_LOW", "15");
			break;

		case DisplayMetrics.DENSITY_MEDIUM:
			p.setTextSize(100);
			Log.i("TextSize -- DENSITY_MEDIUM", "28");
			break;

		case DisplayMetrics.DENSITY_HIGH:
			p.setTextSize(100);
			Log.i("TextSize -- DENSITY_HIGH", "30");
			break;

		case DisplayMetrics.DENSITY_XHIGH:
			p.setTextSize(100);
			Log.i("TextSize -- DENSITY_XHIGH", "40");
			break;

		case DisplayMetrics.DENSITY_XXHIGH:
			p.setTextSize(100);
			Log.i("TextSize -- DENSITY_XXHIGH", "50");
			break;
		default:
			p.setTextSize(50);
			Log.i("TextSize -- default", "22");
			break;

		}
		
		if (this.getResources().getString(R.string.tablet_string).equals("sw600dp")) {
			p.setTextSize(30);
		} else if (this.getResources().getString(R.string.tablet_string).equals("sw720dp")) {
			p.setTextSize(30);
		}

		// canvas for Player 2 start
		canvas.save();

		// p.setColor(Color.parseColor("#5c8037"));
		p.setColor(Color.BLACK);
		p.setTypeface(tfFromanBold);

		TextPaint tp = new TextPaint(p);

		/*canvas.translate((canvas.getWidth() - mDisplaySize.width()) / 2,
				canvas.getHeight() / 2 + canvas.getHeight() / 10.0f);*/
		
		canvas.translate(0,
				canvas.getHeight() / 2 + canvas.getHeight() / 10.0f);

		StaticLayout sl = new StaticLayout(MainActivity.value, tp,
				canvas.getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f,
				false);

		sl.draw(canvas);

		canvas.restore();
		// canvas for Player 2 end

		// p.setColor(Color.parseColor("#5c8037"));
		p.setColor(Color.BLACK);
		p.setTypeface(tfScore);
		// p.setTextSize(40);

		// Player 2 Score
		canvas.drawText(Integer.toString(GlobalVar.player2Score) + ":"
				+ Integer.toString(GlobalVar.player1Score),
				canvas.getWidth() / 16,
				canvas.getHeight() / 2 + canvas.getHeight() / 20, p);

		// Player 2 Score End

		canvas.drawBitmap(mBitmapGreenLine, 0,
				canvas.getHeight() / 2 + canvas.getHeight() / 16, p);

		canvas.drawBitmap(mBitmapWhiteLine, 0,
				canvas.getHeight() / 2 - canvas.getHeight() / 16
						- mBitmapWhiteLine.getHeight(), p);

		// Player 1 Score
		canvas.save();

		p.setColor(Color.WHITE);

		canvas.rotate(180, canvas.getWidth() / 1.07f, canvas.getHeight() / 2
				- canvas.getHeight() / 20);

		canvas.drawText(Integer.toString(GlobalVar.player1Score) + ":"
				+ Integer.toString(GlobalVar.player2Score),

		canvas.getWidth() / 1.07f, canvas.getHeight() / 2 - canvas.getHeight()
				/ 20, p);

		canvas.restore();

		// Player 1 Score End

		// canvas for Player 1 start
		canvas.save();

		p.setColor(Color.WHITE);
		p.setTypeface(tfFromanBold);

		TextPaint tp1 = new TextPaint(p);

		canvas.rotate(180, (canvas.getWidth() + mDisplaySize.width()) / 2,
				canvas.getHeight() / 2 - canvas.getHeight() / 10.0f);

		canvas.translate(0,
				canvas.getHeight() / 2 - canvas.getHeight() / 10.0f);

		StaticLayout s2 = new StaticLayout(MainActivity.value, tp1,
				canvas.getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f,
				false);
		s2.draw(canvas);

		canvas.restore();
		// canvas for Player 1 end

//		if (context.getResources().getBoolean(R.bool.is_tablet)) {
//			p.setTextSize(40);
//		}
	}

}
