package amhsn.retrofitroom.trueorfalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import amhsn.retrofitroom.trueorfalse.login.LoginActivity;
import amhsn.retrofitroom.trueorfalse.room.entity.Question;
import amhsn.retrofitroom.trueorfalse.utils.Config;
import amhsn.retrofitroom.trueorfalse.viewModel.QuestionsListViewModel;

public class MainActivity extends FragmentActivity {

    public final int MAX_QUE = 13;

    Button btnPlayer1True, btnPlayer1False, btnPlayer2True, btnPlayer2False;
    ImageView imgPlayer1AnswerState, imgPlayer2AnswerState;

    public static String value = "";

    // ViewModel
    private QuestionsListViewModel retroViewModel;
    private List<Question> questionList = new ArrayList<>();
    private Question question;
    private int qCounter = 0;
    static int qid = 0;

    TextClass objTextClass;

    MyCount counter;

    // vars Timer
    private final int REQUEST_INTERVAL = 10;
    private Timer requestIntervalTimer;
    private boolean running;
    public String type;

//	private InterstitialAd interstitial;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        try {
        // --- Shuffle array
        GlobalVar.array = new Integer[GlobalVar.TOTAL_QUESTION];

        for (int j = 0; j < GlobalVar.array.length; j++) {
            GlobalVar.array[j] = j;
        }

        Collections.shuffle(Arrays.asList(GlobalVar.array));

        // --- Shuffle array

        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        GlobalVar.i = 0;
        GlobalVar.player1Score = 0;
        GlobalVar.player2Score = 0;

        startActivity(intent);

        overridePendingTransition(R.anim.enter, R.anim.exit);
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "Please Try Again1",
//                    Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        try {
        setContentView(R.layout.activity_main_screen);



        Log.d("API:", "response.code(): ");

        System.gc();
        fetchQuestions();
        if (!running) {
            timerStart();
        }


        btnPlayer1True = findViewById(R.id.btnPlayer1True);
        btnPlayer1False = findViewById(R.id.btnPlayer1False);
        btnPlayer2True = findViewById(R.id.btnPlayer2True);
        btnPlayer2False = findViewById(R.id.btnPlayer2False);

        imgPlayer1AnswerState = findViewById(R.id.imgPlayer1AnswerState);
        imgPlayer2AnswerState = findViewById(R.id.imgPlayer2AnswerState);

        setImageViewVisibility(false);


        counter = new MyCount(1000, 1000);


        Config.load(this);
        long currentTime = System.currentTimeMillis();
        if ((currentTime - Config.LAST_TIME) > 10 * 60 * 1000) {
            Config.LAST_TIME = currentTime;
            Config.save(this);
        }
    }


    public void btnPlayer1TrueOnClickListener(View v) {
//        try {
        if (GlobalVar.isSound) {
            ClassSound.PlaySound(getApplicationContext());
        }

        setButtonVisiblity(false);

        if (question.isCorrect() == 1) {

            imgPlayer1AnswerState
                    .setImageResource(R.drawable.player1_answer_correct);
            imgPlayer2AnswerState
                    .setImageResource(R.drawable.player2_answer_wrong);

            GlobalVar.player1Score += 1;

        } else {

            imgPlayer1AnswerState
                    .setImageResource(R.drawable.player1_answer_wrong);
            imgPlayer2AnswerState
                    .setImageResource(R.drawable.player2_answer_correct);

            GlobalVar.player2Score += 1;
        }
        setImageViewVisibility(true);
        GlobalVar.i = GlobalVar.i + 1;
        counter.start();
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "Please Try Again3",
//                    Toast.LENGTH_SHORT).show();
//        }
    }

    public void btnPlayer1FalseOnClickListener(View v) {
//        try {
        if (GlobalVar.isSound) {
            ClassSound.PlaySound(getApplicationContext());
        }

        setButtonVisiblity(false);
        setImageViewVisibility(true);
        if (question.isCorrect() == 1) {

            imgPlayer1AnswerState
                    .setImageResource(R.drawable.player1_answer_correct);
            imgPlayer2AnswerState
                    .setImageResource(R.drawable.player2_answer_wrong);

            GlobalVar.player1Score += 1;

        } else {

            imgPlayer1AnswerState.setImageResource(R.drawable.player1_answer_wrong);
            imgPlayer2AnswerState.setImageResource(R.drawable.player2_answer_correct);

            GlobalVar.player2Score += 1;

        }
        GlobalVar.i = GlobalVar.i + 1;
        counter.start();
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "Please Try Again4",
//                    Toast.LENGTH_SHORT).show();
//        }
    }

    public void btnPlayer2TrueOnClickListener(View v) {
//        try {
        if (GlobalVar.isSound) {
            ClassSound.PlaySound(getApplicationContext());
        }

        setButtonVisiblity(false);
        setImageViewVisibility(true);
        if (question.isCorrect() == 1) {

            imgPlayer1AnswerState
                    .setImageResource(R.drawable.player1_answer_wrong);
            imgPlayer2AnswerState
                    .setImageResource(R.drawable.player2_answer_correct);

            GlobalVar.player2Score += 1;

        } else {

            imgPlayer1AnswerState
                    .setImageResource(R.drawable.player1_answer_correct);
            imgPlayer2AnswerState
                    .setImageResource(R.drawable.player2_answer_wrong);

            GlobalVar.player1Score += 1;

        }
        GlobalVar.i = GlobalVar.i + 1;
        counter.start();
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "Please Try Again5",
//                    Toast.LENGTH_SHORT).show();
//        }
    }

    public void btnPlayer2FalseOnClickListener(View v) {
//        try {
        if (GlobalVar.isSound) {
            ClassSound.PlaySound(getApplicationContext());
        }

        setButtonVisiblity(false);
        setImageViewVisibility(true);
        if (question.isCorrect() == 1) {

            imgPlayer1AnswerState
                    .setImageResource(R.drawable.player1_answer_wrong);
            imgPlayer2AnswerState
                    .setImageResource(R.drawable.player2_answer_correct);

            GlobalVar.player2Score += 1;

        } else {

            imgPlayer1AnswerState
                    .setImageResource(R.drawable.player1_answer_correct);
            imgPlayer2AnswerState
                    .setImageResource(R.drawable.player2_answer_wrong);

            GlobalVar.player1Score += 1;
        }
        GlobalVar.i = GlobalVar.i + 1;
        counter.start();
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "Please Try Again6",
//                    Toast.LENGTH_SHORT).show();
//        }
    }

    private void setButtonVisiblity(boolean state) {
        if (state) {
            btnPlayer1True.setVisibility(View.VISIBLE);
            btnPlayer1False.setVisibility(View.VISIBLE);
            btnPlayer2True.setVisibility(View.VISIBLE);
            btnPlayer2False.setVisibility(View.VISIBLE);
        } else {
            btnPlayer1True.setVisibility(View.INVISIBLE);
            btnPlayer1False.setVisibility(View.INVISIBLE);
            btnPlayer2True.setVisibility(View.INVISIBLE);
            btnPlayer2False.setVisibility(View.INVISIBLE);
        }

    }

    private void setImageViewVisibility(boolean state) {
        /*
         * Animation myFadeInAnimation = AnimationUtils.loadAnimation(
         * MainActivity.this, R.anim.tween);
         */
        if (state) {
            imgPlayer1AnswerState.setVisibility(View.VISIBLE);
            imgPlayer2AnswerState.setVisibility(View.VISIBLE);
            /*
             * imgPlayer1AnswerState.startAnimation(myFadeInAnimation);
             * imgPlayer2AnswerState.startAnimation(myFadeInAnimation);
             */
        } else {
            imgPlayer1AnswerState.setVisibility(View.INVISIBLE);
            imgPlayer2AnswerState.setVisibility(View.INVISIBLE);
        }
    }

    // Counter

    private class MyCount extends CountDownTimer {

        MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            finish();
            Intent intent;
            if (GlobalVar.i < MAX_QUE) {
                intent = new Intent(getApplicationContext(), MainActivity.class);

            } else {
                intent = new Intent(getApplicationContext(),
                        ResultActivity.class);

            }
            startActivity(intent);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }

    // get questions
    private void fetchQuestions() {

        // declare view model
        retroViewModel = ViewModelProviders.of(this).get(QuestionsListViewModel.class);
        retroViewModel.getAllQuestions().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                questionList = questions;
                Collections.shuffle(questionList);
            }


//                Collections.shuffle(questionList, new Random());


        });

    }

    @Override
    protected void onStart() {
        super.onStart();
//        fetchQuestions();
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

                    qCounter = questionList.size();

                    if (qid < qCounter) {
                        question = questionList.get(qid);
                        value = question.getQuestion();
                        qid++;
                    }
                    Log.d("runnnnnn", "run: " + qCounter);
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
//        if (!running) {
//            timerStart();
//        }
    }


    public void onPause() {
        super.onPause();
        if (running) {
            timerStop();
        }
    }


}
