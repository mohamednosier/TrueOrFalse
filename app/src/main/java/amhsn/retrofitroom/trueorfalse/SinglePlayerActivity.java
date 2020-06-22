package amhsn.retrofitroom.trueorfalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import amhsn.retrofitroom.trueorfalse.room.entity.Question;
import amhsn.retrofitroom.trueorfalse.utils.Config;
import amhsn.retrofitroom.trueorfalse.viewModel.QuestionsListViewModel;

public class SinglePlayerActivity extends FragmentActivity {

    // var from Class
    private MyCount counter;

    // widgets
    private TextView txtQuestion, txtPlayerScore, txtLevel, txtPlayerLife, txtTime;
    private Button btnTrue, btnFalse, btnSkip;
    private Button btnShareFacebook;

    // ViewModel
    private QuestionsListViewModel retroViewModel;
    private List<Question> questionList = new ArrayList<>();
    private Question question;
    private int qCounter = 0;
    static int qid = 0;
    int position;

    // vars Timer
    private final int REQUEST_INTERVAL = 5000;
    private Timer requestIntervalTimer;
    private boolean running;


    // vars list
    private ArrayList<String> aryID, aryLives, aryAnswer, arySkip, arySecond;

    // var font
    protected Typeface tfQuestion, tfFromanBold;


    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
            counter.cancel();
            GlobalVar.playerLife = 0;
            GlobalVar.playerScore = 0;
            GlobalVar.levelSkip = 0;
            qid=0;

            Intent intent = new Intent(SinglePlayerActivity.this,
                    LevelActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);

            overridePendingTransition(R.anim.enter, R.anim.exit);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Try Again 4",
                    Toast.LENGTH_SHORT).show();
        }

    }



    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {
            setContentView(R.layout.activity_single_player);

            System.gc();

            position = GlobalVar.level - 1;

//            fetchQuestions();

            tfQuestion = Typeface.createFromAsset(
                    SinglePlayerActivity.this.getAssets(), "HOBOSTD.OTF");

            tfFromanBold = Typeface.create(tfQuestion, Typeface.BOLD);

            txtQuestion = findViewById(R.id.txtQuestion);
            txtPlayerScore = findViewById(R.id.txtPlayerScore);
            txtLevel = findViewById(R.id.txtLevel);
            txtPlayerLife = findViewById(R.id.txtPlayerLife);
            txtTime = findViewById(R.id.txtTime);

            btnTrue = findViewById(R.id.btnTrue);
            btnFalse = findViewById(R.id.btnFalse);
            btnSkip = findViewById(R.id.btnSkip);

            txtQuestion.setTypeface(tfFromanBold);

            @SuppressWarnings("unused")
            float density = 1.0F;
            WindowManager wm = ((WindowManager) getApplicationContext()
                    .getSystemService(Context.WINDOW_SERVICE));
            Display display = Objects.requireNonNull(wm).getDefaultDisplay();
            display = ((WindowManager) Objects.requireNonNull(getSystemService("window")))
                    .getDefaultDisplay();
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            display.getMetrics(localDisplayMetrics);
            density = localDisplayMetrics.density;

            @SuppressWarnings("deprecation")
            int screenwidth = ((WindowManager) Objects.requireNonNull(SinglePlayerActivity.this
                    .getSystemService(Context.WINDOW_SERVICE)))
                    .getDefaultDisplay().getWidth();
            @SuppressWarnings("deprecation")
            int screenheight = ((WindowManager) Objects.requireNonNull(SinglePlayerActivity.this
                    .getSystemService(Context.WINDOW_SERVICE)))
                    .getDefaultDisplay().getHeight();


            GlobalVar.levelLives = 5;

            GlobalVar.levelAnswer = 5;

            GlobalVar.levelSkip = 2;

            GlobalVar.levelsSecond = 20;

//            try {
//
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                        if (qid < 10) {
//                            question = questionList.get(qid);
//                            txtQuestion.setText(question.getQuestion());
//                            qid++;
//                        }
////                        setQuestionView();
////                        qCounter = questionList.size();
//
//                        Log.d("TAGGGGGGGGGGG", "position: " + position);
//
////                        for (int i = position * 10; i < (position + 1) * 10; i++) {
////                            question = questionList.get(i);
////                            Log.d("question", "questions: " + question);
////                            txtQuestion.setText(question.getQuestion());
//////                            setQuestionView();
////                        }
//
////                        qCounter = questionList.size();
//
//                    }
//                }, 100);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


            txtPlayerScore.setTypeface(tfQuestion);
            txtPlayerScore.setTextSize(20);
            txtPlayerScore.setText(GlobalVar.playerScore + "/" + GlobalVar.levelAnswer);

            txtPlayerLife.setTypeface(tfQuestion);
            txtPlayerLife.setTextSize(20);
            txtPlayerLife.setText(GlobalVar.playerLife + "/" + GlobalVar.levelLives);

            txtLevel.setTypeface(tfFromanBold);
            txtLevel.setText("Level-" + GlobalVar.level);

            btnSkip.setTypeface(tfQuestion);
            btnSkip.setTextSize(18);
            btnSkip.setTextColor(Color.WHITE);
            btnSkip.setText("\n" + "Skip" + "\n" + GlobalVar.skipCounter + "/" + GlobalVar.levelSkip);

            if (GlobalVar.skipCounter >= GlobalVar.levelSkip) {
                btnSkip.setEnabled(false);
            } else {
                btnSkip.setEnabled(true);
            }

            counter = new MyCount((10 + 1) * 1000, 1000);

            // counter = new MyCount(21000, 1000);
            counter.start();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Try Again 5",
                    Toast.LENGTH_SHORT).show();
        }

        Config.load(this);
        long currentTime = System.currentTimeMillis();
        if ((currentTime - Config.LAST_TIME) > 10 * 60 * 1000) {
            Config.LAST_TIME = currentTime;
            Config.save(this);
        }

    }



    public void btnTrueOnClickListener(View v) {
        try {
            if (GlobalVar.isSound) {
                ClassSound.PlaySound(getApplicationContext());
            }

            counter.cancel();
            GlobalVar.k = GlobalVar.k + 1;

            // finish();

            Intent intent = new Intent(getApplicationContext(),
                    SinglePlayerResultActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            if (question.isCorrect() == 1) {
                intent.putExtra("Answer", "Correct");
                intent.putExtra("Desc", question.getDescription());
                GlobalVar.playerScore++;
            } else {
                intent.putExtra("Answer", "Wrong");
                intent.putExtra("Desc", question.getDescription());
                GlobalVar.playerLife++;
            }

            startActivity(intent);


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Try Again 1",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("NewApi")
    public void btnFalseOnClickListener(View v) {
        try {
            if (GlobalVar.isSound) {
                ClassSound.PlaySound(getApplicationContext());
            }

            counter.cancel();
            GlobalVar.k = GlobalVar.k + 1;

            // finish();
            Intent intent = new Intent(getApplicationContext(),
                    SinglePlayerResultActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            if (question.isCorrect() == 0) {
                intent.putExtra("Answer", "Correct");
                intent.putExtra("Desc", question.getDescription());
                GlobalVar.playerScore++;
            } else {
                intent.putExtra("Answer", "Wrong");
                intent.putExtra("Desc", question.getDescription());
                GlobalVar.playerLife++;
            }


            startActivity(intent);


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Try Again 2",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void btnSkipOnClickListener(View v) {
        try {
            if (GlobalVar.isSound) {
                ClassSound.PlaySound(getApplicationContext());
            }

            counter.cancel();
            GlobalVar.k = GlobalVar.k + 1;
            finish();

            GlobalVar.skipCounter++;

            Intent intent = new Intent(getApplicationContext(),
                    SinglePlayerActivity.class);

            startActivity(intent);

            overridePendingTransition(R.anim.enter, R.anim.exit);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Try Again 3",
                    Toast.LENGTH_SHORT).show();
        }
    }


    // get questions
    private void fetchQuestions() {

        // declare view model
        retroViewModel = ViewModelProviders.of(this).get(QuestionsListViewModel.class);
        retroViewModel.getAllQuestions().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
//                questionList = questions;
//                Log.d("fetchQuestions", String.valueOf(questionList.size()));

                if (questionList.size() <10 ) {
                    for (int i = position * 10; i < (position + 1) * 10; i++) {
                        questionList.add(questions.get(i));
                        Collections.shuffle(questionList,new Random());
                    }
                }

                for (Question question : questionList) {
                    Log.d("fetchQuestions", question.getQuestion());
                    Log.d("fetchQuestions", String.valueOf(question.getId()));
                }
                Log.d("SinglefetchQuestions", String.valueOf(questionList.size()));

//                Collections.shuffle(questionList, new Random());

            }
        });

    }

    private void setQuestionView() {

        txtQuestion.setText(question.getQuestion());
        qid++;
    }

    // Counter
    private class MyCount extends CountDownTimer {

        MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @SuppressLint("NewApi")
        @Override
        public void onFinish() {
            counter.cancel();

            GlobalVar.k = GlobalVar.k + 1;

            Intent intent = new Intent(getApplicationContext(),
                    SinglePlayerResultActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent.putExtra("Answer", "Wrong");
            intent.putExtra("Desc", question.getDescription());
            GlobalVar.playerLife++;

            startActivity(intent);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            txtTime.setTypeface(tfFromanBold);
            txtTime.setText(Long.toString(millisUntilFinished / 1000));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchQuestions();
    }

    /**
     * Responsible for refreshing data in screen every 5 sec
     */
    public void timerStart() {
        running = true;
        if (requestIntervalTimer == null) {
            requestIntervalTimer = new Timer();
            requestIntervalTimer.schedule(new TimerTask() {
                public void run() {

                    if (questionList.size() > 0){
                        timerStop();
                    }

                    if (qid < 10) {
                        question = questionList.get(qid);
                        txtQuestion.setText(question.getQuestion());
                        qid++;
                    }
                    Log.d("sssss", "setDataOnView: " + "Refresh");
                }
            }, 0, REQUEST_INTERVAL);
        }
    }


    /*
     * Responsible for refreshing data in screen every 5 sec
     * */
    void timerStop() {
        running = false;
        try {
            if (requestIntervalTimer != null) {
                requestIntervalTimer.cancel();
                requestIntervalTimer.purge();
                requestIntervalTimer = null;
            }
        } catch (NullPointerException e) {
            running = true;
        }
    }

    public void onResume() {
        super.onResume();
        if (!running) {
            timerStart();
        }
    }


    public void onPause() {
        super.onPause();
        if (running) {
            timerStop();
        }
    }


}