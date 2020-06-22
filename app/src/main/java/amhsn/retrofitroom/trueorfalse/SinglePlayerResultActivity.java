package amhsn.retrofitroom.trueorfalse;

import java.util.Arrays;
import java.util.Collections;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class SinglePlayerResultActivity extends Activity {

	ImageView imgAnswerState;
	TextView txtAnsDesc, txtAnswerState, txtContinue;

	RelativeLayout rlResultActivity;

	SharedPreferences pref;
	Editor editor;

	Typeface tfQuestion, tfFromanBold;

	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_single_player_result);
		/*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
		try {
			

			tfQuestion = Typeface.createFromAsset(
					SinglePlayerResultActivity.this.getAssets(), "HOBOSTD.OTF");

			tfFromanBold = Typeface.create(tfQuestion, Typeface.BOLD);

			pref = getApplicationContext().getSharedPreferences("prefname",
					MODE_PRIVATE);
			editor = pref.edit();

			Intent i = getIntent();

			imgAnswerState = (ImageView) findViewById(R.id.imgAnswerState);

			txtAnsDesc = (TextView) findViewById(R.id.txtAnsDesc);
			txtAnsDesc.setTypeface(tfQuestion);
			txtAnswerState = (TextView) findViewById(R.id.txtAnswerState);
			txtAnswerState.setTypeface(tfFromanBold);
			txtContinue = (TextView) findViewById(R.id.txtContinue);
			txtContinue.setTypeface(tfQuestion);
			rlResultActivity = (RelativeLayout) findViewById(R.id.rlResultActivity);

			if (i.getStringExtra("Desc").equals("NULL")) {
				txtAnsDesc.setText(" ");
			} else {
				txtAnsDesc.setText(i.getStringExtra("Desc"));
			}

			if (i.getStringExtra("Answer").equalsIgnoreCase("wrong")) {
				imgAnswerState
						.setImageResource(R.drawable.player2_answer_wrong);
				txtAnswerState.setTextColor(Color.RED);
				txtAnswerState.setText("Incorrect");
			} else {
				imgAnswerState
						.setImageResource(R.drawable.player2_answer_correct);
				txtAnswerState.setTextColor(Color.parseColor("#7ba04d"));
				txtAnswerState.setText("Correct");
			}

			/*rlResultActivity.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						txtContinue.setVisibility(View.INVISIBLE);
						if (GlobalVar.isSound) {
							ClassSound.PlaySound(getApplicationContext());
						}
					
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),
								"Please Try Again", Toast.LENGTH_SHORT).show();
					}
				}
			});*/
			
			rlResultActivity.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					try {
						txtContinue.setVisibility(View.INVISIBLE);
						if (GlobalVar.isSound) {
							ClassSound.PlaySound(getApplicationContext());
						}
						//txtContinue.setText("Please wait..");
						temp();
					
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),
								"Please Try Again", Toast.LENGTH_SHORT).show();
					}
					return false;
				}
			});

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Please Try Again",
					Toast.LENGTH_SHORT).show();
		}
	}

	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
		
			if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
				
			}
			return true;
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Please Try Again",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}
*/
	private void temp() {
		if (GlobalVar.k < GlobalVar.levelAnswer + GlobalVar.levelLives
				+ GlobalVar.levelSkip) {
			if (GlobalVar.playerScore >= GlobalVar.levelAnswer) {
				// --- Storing Score in array
				GlobalVar.aryScoreList[GlobalVar.level - 1] = Integer
						.toString(GlobalVar.playerScore);

				/*
				 * for (int i = 0; i < GlobalVar.level; i++) { Toast.makeText(
				 * getApplicationContext(), "level:" + (i + 1) + "-" +
				 * GlobalVar.aryScoreList[i], Toast.LENGTH_SHORT).show(); }
				 */

				/*editor.putStringSet(
						"aryScoreList",
						new HashSet<String>(Arrays
								.asList(GlobalVar.aryScoreList)));*/

				if (pref.getInt("clearedLevels", 0) <= GlobalVar.level) {
					editor.putInt("clearedLevels", GlobalVar.level);
				}

				editor.commit();
				/*
				 * Toast.makeText( getApplicationContext(), "pref" +
				 * Integer.toString(pref.getInt("clearedLevels", 0)),
				 * Toast.LENGTH_SHORT).show();
				 */
				// --- Storing Score in array

				// --- Level Increase
				if (GlobalVar.level <= 60) {
					GlobalVar.level += 1;

					Toast.makeText(getApplicationContext(),
							"Congratulations!! You cleared the Level",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Congratulations!! You cleared all the Level",
							Toast.LENGTH_SHORT).show();
				}

				/*
				 * int t = (GlobalVar.level - 1)
				 * GlobalVar.QUESTION_FOR_SINGLE_LEVEL;
				 * 
				 * GlobalVar.aryLevel1 = null;
				 * 
				 * GlobalVar.aryLevel1 = new
				 * Integer[GlobalVar.QUESTION_FOR_SINGLE_LEVEL];
				 * 
				 * for (int j = 0; j < GlobalVar.aryLevel1.length; j++) {
				 * GlobalVar.aryLevel1[j] = j + t; }
				 * 
				 * Collections.shuffle(Arrays.asList(GlobalVar.aryLevel1));
				 */
				// --- Level Increase

				GlobalVar.k = 0;
				GlobalVar.playerScore = 0;
				GlobalVar.playerLife = 0;
				GlobalVar.skipCounter = 0;

				 finish(); 

				Intent intent = new Intent(SinglePlayerResultActivity.this,
						LevelActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(intent);
			
			//	overridePendingTransition(R.anim.enter, R.anim.exit);

			} else if (GlobalVar.playerLife >= GlobalVar.levelLives) {
				Collections.shuffle(Arrays.asList(GlobalVar.aryLevel1));

				Toast.makeText(getApplicationContext(),
						"Level Not Cleared, Try Again", Toast.LENGTH_SHORT)
						.show();
				SinglePlayerActivity.qid=0;
				GlobalVar.k = 0;
				GlobalVar.playerScore = 0;
				GlobalVar.playerLife = 0;
				GlobalVar.skipCounter = 0;

				 finish();
				Intent intent = new Intent(SinglePlayerResultActivity.this,
						LevelActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
		
				//overridePendingTransition(R.anim.enter, R.anim.exit);

			} else {
				 finish();
				Intent intent = new Intent(SinglePlayerResultActivity.this,
						SinglePlayerActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			
			//	overridePendingTransition(R.anim.enter, R.anim.exit);

			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
