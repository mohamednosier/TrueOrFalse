package amhsn.retrofitroom.trueorfalse.GameRoom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;

import amhsn.retrofitroom.trueorfalse.ClassSound;
import amhsn.retrofitroom.trueorfalse.Constant;
import amhsn.retrofitroom.trueorfalse.GlobalVar;
import amhsn.retrofitroom.trueorfalse.R;
import amhsn.retrofitroom.trueorfalse.helper.Utils;
import amhsn.retrofitroom.trueorfalse.room.entity.Question;
import amhsn.retrofitroom.trueorfalse.utils.Config;


public class PlayerActivity extends AppCompatActivity {

    private final static String TAG = "PlayerActivity";

    // var from Class
    private MyCount counter;

    // widgets
    private TextView txtQuestion, txtPlayerScore, txtLevel, txtPlayerLife, txtTime;
    private Button btnTrue, btnFalse, btnSkip;
    private Button btnShareFacebook;
    private AlertDialog quitAlertDialog;

    // ViewModel
    private List<Question> questionList = new ArrayList<>();
    private List<Question> questionListID = new ArrayList<>();
    private Question question;
    private int position;

    // vars Timer
    private final int REQUEST_INTERVAL = 5;
    private Timer requestIntervalTimer;
    private boolean running;


    // vars list
    public static Boolean virtual_play = false;
    private List<String> list;
    private boolean isPlayStarted = false, player1GameStatus, player2GameStatus;
    private final Handler mHandler = new Handler();
    private String roomKey = "";
    private ArrayList<String> aryID, aryLives, aryAnswer, arySkip, arySecond;
    public int questionIndex = 0, btnPosition = 0, correctQuestion = 0, inCorrectQuestion = 0,
            questionIndex_vplayer = 0, correctQuestion_vplayer = 0, inCorrectQuestion_vplayer = 0,
            click = 0, textSize, preScore = 0;
    private String userId1, userId2, Player1Name, Player2Name, Player1UserID, Player2UserID, gameId, winner, winnerMessage,
            player1Key = "", player2Key = "", battlePlayer, optionClicked = "false",
            tts = "tts", profilePlayer1, profilePlayer2, winDialogTitle, pauseCheck = "regular";
    private String numRandom = "";
    private String num_of_wins, num_of_loss;
    private String player = "";
    private String questionResponse = "true";
    private int timerOnGame;
    int randomTime = 0, randomAnswer = 0;
    private int p1_request =0, p2_request =0;

    // var font
    private Typeface tfQuestion, tfFromanBold;
    private long leftTime;
    private String p2_sel;
    private String qid;
    private String p1_sell, p2_sell;
    private int Player1NumOfWins, Player2NumOfWins;
    private int Player1NumOfLoss, Player2NumOfLoss;


    // vars firebase
    private DatabaseReference myGameRef;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private boolean player1Status, player2Status;
    private DatabaseReference myRef;
    private DatabaseReference database;



    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: onStarted.");
        requestWindowFeature(Window.FEATURE_NO_TITLE);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        try {
            setContentView(R.layout.activity_player);

            System.gc();
            // receive data from GetOpponentActivity
            if (!getIntent().getExtras().isEmpty()) {
                gameId = getIntent().getStringExtra("gameid");
                battlePlayer = getIntent().getStringExtra("battlePlayer");
            }
            roomKey = gameId;
            myGameRef = FirebaseDatabase.getInstance().getReference().child(Constant.DB_GAME_ROOM);
            player = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            database = FirebaseDatabase.getInstance().getReference();
            init();


            tfQuestion = Typeface.createFromAsset(
                    PlayerActivity.this.getAssets(), "HOBOSTD.OTF");

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
            int screenwidth = ((WindowManager) Objects.requireNonNull(PlayerActivity.this
                    .getSystemService(Context.WINDOW_SERVICE)))
                    .getDefaultDisplay().getWidth();
            @SuppressWarnings("deprecation")
            int screenheight = ((WindowManager) Objects.requireNonNull(PlayerActivity.this
                    .getSystemService(Context.WINDOW_SERVICE)))
                    .getDefaultDisplay().getHeight();


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



        Config.load(this);


        if (Utils.isNetworkAvailable(PlayerActivity.this)) {

            if (battlePlayer.equals(getString(R.string.robot))) {
                Log.d(TAG, "onCreate: battlePlayer.equals(getString(R.string.robot)");
                questionList = GetOpponentActivity.questionArrayList;
            } else {
                questionList = GetOpponentActivity.battleQuestionList;
                Log.d("TAGquestionList", "onCreate: questionList: " + questionList.size());
            }
        } else {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar
                    .make(parentLayout, getString(R.string.msg_no_internet), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
            snackbar.show();
        }

    }


    private final Runnable mUpdateUITimerTask = new Runnable() {
        public void run() {
            nextQuizQuestion();
        }
    };


    /*
     * A function use to get next question from list
     * */
    private void nextQuizQuestion() {
        optionClicked = "false";
        if (counter != null) {
            counter.cancel();
        }
        counter = new MyCount(Constant.TIME_PER_QUESTION, Constant.COUNT_DOWN_TIMER);
        if (counter != null) {
            counter.cancel();
            counter.start();
        } else {
            counter.start();
        }


        runOnUiThread(new Runnable() {
            //
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (questionIndex < questionListID.size()) {
                            question = questionListID.get(questionIndex);
                            txtQuestion.setText(question.getQuestion());
                            myGameRef.child(gameId).child(player1Key).child(Constant.SEL_ANS).setValue("");
                            myGameRef.child(gameId).child(player2Key).child(Constant.SEL_ANS).setValue("");
                            Log.d("TAG", "run: questionIndex: " + question.getQuestion());
                            randomTime = new Random().nextInt(26);
                            randomAnswer = getRandom(2);
                        }
                    }
                });
            }
        });


    }


    /*
     * A function responsible get data player and player2
     * and all data about game room
     * */
    private void init() {
        Player1UserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        if (getIntent().getExtras() != null) {
            Player2UserID = getIntent().getStringExtra("opponentId");
            userId1 = getIntent().getStringExtra("user_id1");
            userId2 = getIntent().getStringExtra("user_id2");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constant.DB_USER);
        databaseReference.child(Player1UserID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Player1Name = dataSnapshot.child(Constant.USER_NAME).getValue().toString();
                            player1Key = Player1UserID;
                            profilePlayer1 = dataSnapshot.child(Constant.PROFILE_PIC).getValue().toString();
                            Player1NumOfWins = Integer.parseInt(dataSnapshot.child("num_of_wins").getValue().toString());
                            Player1NumOfLoss = Integer.parseInt(dataSnapshot.child("num_of_loss").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        databaseReference.child(Player2UserID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Player2Name = dataSnapshot.child(Constant.USER_NAME).getValue().toString();
                            profilePlayer2 = dataSnapshot.child(Constant.PROFILE_PIC).getValue().toString();
                            player2Key = Player2UserID;
                            Player2NumOfWins = Integer.parseInt(dataSnapshot.child("num_of_wins").getValue().toString());
                            Player2NumOfLoss = Integer.parseInt(dataSnapshot.child("num_of_loss").getValue().toString());
                            virtual_play = false;

                        } else {
                            virtual_play = true;
                            Player2Name = getString(R.string.robot);
                            player2Key = Player2UserID;

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        myGameRef.child(gameId).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {

                        String queId = dataSnapshot.child(Constant.QUE_ID).getValue(String.class);

                        list = Arrays.asList(queId.split(","));

                        Log.d("TAGpque", "onDataChange: p1_que: " + list.size());


                        for (int i = 0; i < questionList.size(); i++) {
                            for (int j = 0; j < list.size(); j++) {
                                if (Integer.parseInt(list.get(j)) == questionList.get(i).getId()) {
                                    if (questionListID.size() < 10) {
                                        questionListID.add(new Question(questionList.get(i).getId(), questionList.get(i).getQuestion(), questionList.get(i).isCorrect()));
                                        Log.d("TAG", "onDataChange: questionListID: " + questionListID.size());
                                    }
                                }
                            }
                        }

                        if (questionListID.size() != 0) {
                            questionResponse = "false";
                        }

                        player1Key = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                        player2Key = getIntent().getStringExtra("opponentId");
                        Log.d("DDDDD", "onDataChange: player2Key: " + player2Key);

                        if (dataSnapshot.child(player1Key).getValue() != null)
                            player1GameStatus = (boolean) dataSnapshot.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("status").getValue();
                        if (dataSnapshot.child(player2Key).getValue() != null) {
                            player2GameStatus = (boolean) dataSnapshot.child(player2Key).child("status").getValue();
                        }
                        if (player1GameStatus && player2GameStatus) {
                            isPlayStarted = true;
                        }
                        if (isPlayStarted) {
                            if (!player1GameStatus || !player2GameStatus) {
                                showOtherUserQuitDialog();
                            } else {


                                int p1_que = dataSnapshot.child(player1Key).child(Constant.QUE_NO).getValue(Integer.class);
                                int p2_que = dataSnapshot.child(player2Key).child(Constant.QUE_NO).getValue(Integer.class);

                                 p1_request = dataSnapshot.child(player1Key).child("req_continue").getValue(Integer.class);
                                 p2_request = dataSnapshot.child(player2Key).child("req_continue").getValue(Integer.class);

                                p2_sell = dataSnapshot.child(player2Key).child(Constant.SEL_ANS).getValue(String.class);
                                p1_sell = dataSnapshot.child(player1Key).child(Constant.SEL_ANS).getValue(String.class);
                                Log.d("TAG", "onDataChange: p1_sell: " + p1_sell + "  p2_sell: " + p2_sell);
                                String p2_msg = dataSnapshot.child(player2Key).child(Constant.MESSAGE).getValue(String.class);


                                if (!p2_sell.equals("")) {
                                    Toast.makeText(PlayerActivity.this, Player2Name + " answered", Toast.LENGTH_SHORT).show();
                                }

                                if (!p2_msg.equals("")) {
                                    Toast.makeText(PlayerActivity.this, Player2Name + ",   msg: " + p2_msg, Toast.LENGTH_SHORT).show();
                                    p2_msg = "";
                                }


                                if (p1_que == p2_que) {
                                    final int r_2 = dataSnapshot.child(player2Key).child(Constant.RIGHT).getValue(Integer.class);
                                    final int r_1 = dataSnapshot.child(player1Key).child(Constant.RIGHT).getValue(Integer.class);

                                    String p1_sel = dataSnapshot.child(player1Key).child(Constant.SEL_ANS).getValue(String.class);
                                    String p2_sel = dataSnapshot.child(player2Key).child(Constant.SEL_ANS).getValue(String.class);

                                    if (!p2_sel.equals("")) {
                                        Toast.makeText(PlayerActivity.this, Player2Name + " choosed: " + p2_sel, Toast.LENGTH_SHORT).show();
                                    }

                                    Log.d("TAG", "onDataChange: p1_que : " + p1_que);
                                    if (p1_que == Constant.MAX_QUESTION_PER_BATTLE) {

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                if (r_1 > r_2) {
                                                    winnerMessage = Player1Name + getString(R.string.msg_win_battle);
                                                    winner = "you";
                                                    winDialogTitle = getString(R.string.congrats);
                                                    int numOfWins1 = Player1NumOfWins + 1;
                                                    int numOfLoss2 = Player2NumOfLoss + 1;
                                                    databaseReference.child(Player1UserID).child("num_of_wins").setValue(numOfWins1);
                                                    databaseReference.child(Player2UserID).child("num_of_loss").setValue(numOfLoss2);
                                                    showWinnerDialog();

                                                } else if (r_2 > r_1) {
                                                    winnerMessage = Player2Name + getString(R.string.msg_opponent_win_battle);
                                                    winner = Player2Name;
                                                    winDialogTitle = getString(R.string.next_time);
                                                    int numOfWins2 = Player2NumOfWins + 1;
                                                    int numOfLoss1 = Player1NumOfLoss + 1;
                                                    databaseReference.child(Player2UserID).child("num_of_wins").setValue(numOfWins2);
                                                    databaseReference.child(Player1UserID).child("num_of_loss").setValue(numOfLoss1);
                                                    showWinnerDialog();

                                                } else {
                                                    showResetGameAlert();
                                                }
                                            }
                                        }, 2000);

                                    } else {
                                        mHandler.postDelayed(mUpdateUITimerTask, 0);
                                    }

                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //----------------------------Player-----------------------------------------------------------


    /*
     * Onclick on button true
     * */
    public void btnTrueOnClickListener(View v) {
        try {
            if (GlobalVar.isSound) {
                ClassSound.PlaySound(getApplicationContext());
            }

            GlobalVar.k = GlobalVar.k + 1;
            if (optionClicked.equals("false")) {
                if (question.isCorrect() == 1) {
                    correctQuestion = correctQuestion + 1;
                    addScore("true");
                } else {
                    inCorrectQuestion = inCorrectQuestion + 1;
                    WrongQuestion("true");
                }
                optionClicked = "true";
                questionIndex++;
            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Try Again 1",
                    Toast.LENGTH_SHORT).show();
        }
    }



    /*
     * Onclick on button false
     * */
    @SuppressLint("NewApi")
    public void btnFalseOnClickListener(View v) {
        try {
            if (GlobalVar.isSound) {
                ClassSound.PlaySound(getApplicationContext());
            }

            GlobalVar.k = GlobalVar.k + 1;
            if (optionClicked.equals("false")) {
                if (question.isCorrect() == 0) {
                    correctQuestion = correctQuestion + 1;
                    addScore("false");
                } else {
                    inCorrectQuestion = inCorrectQuestion + 1;
                    WrongQuestion("false");
                }
                optionClicked = "true";
                questionIndex++;
            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Try Again 2",
                    Toast.LENGTH_SHORT).show();
        }
    }


    /*
     * A function use to update status player (online or offline)
     * */
    public void UpdateOnlineStatus() {
        final DatabaseReference databaseReference = myGameRef.child(gameId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.exists()) {
                        //do ur stuff
                        databaseReference.child(FirebaseAuth.getInstance().getUid()).child(Constant.STATUS).setValue(false);
                        finish();
                    } else {
                        //do something if not exists
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


    /*
     * A function use to send data to firebase when his answer right and update data in firebase
     * */
    private void addScore(final String sel_ans) {

        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put(Constant.RIGHT, correctQuestion);
        taskMap.put(Constant.QUE_NO, (questionIndex + 1));
        taskMap.put(Constant.SEL_ANS, sel_ans);

        myGameRef.child(gameId).child(Player1UserID).updateChildren(taskMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        System.out.println("success");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put(Constant.RIGHT, correctQuestion);
                        taskMap.put(Constant.QUE_NO, (questionIndex));
                        taskMap.put(Constant.SEL_ANS, sel_ans);
                        myGameRef.child(gameId).child(Player1UserID).updateChildren(taskMap);
                    }
                });
    }


    /*
     * A function use to send data to firebase when his answer wrong and update data in firebase
     * */
    private void WrongQuestion(String sel_ans) {
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put(Constant.WRONG, inCorrectQuestion);
        taskMap.put(Constant.QUE_NO, (questionIndex + 1));
        taskMap.put(Constant.SEL_ANS, sel_ans);

        myGameRef.child(gameId).child(Player1UserID).updateChildren(taskMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        System.out.println("success");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put(Constant.WRONG, inCorrectQuestion);
                        taskMap.put(Constant.QUE_NO, (questionIndex + 1));

                        myGameRef.child(gameId).child(Player1UserID).updateChildren(taskMap);
                    }
                });
    }

    //--------------------------------------------------------------------------------------------------


    //-----------------------Virtual player------------------------------------------------------------------
    /*
     * A function responsible for selecting a random time and random answers for playing the virtual player
     * */
    public void PerformVirtualClick() {


        Log.d(TAG, "PerformVirtualClick: randomTime: " + randomTime);
        Log.d(TAG, "PerformVirtualClick: randomAnswer: " + randomAnswer);


        if (randomTime == timerOnGame) {
            Log.d(TAG, "PerformVirtualClick: yes");
            if (randomAnswer == 1) {

                if (question.isCorrect() == 1) {
                    RightVirtualAnswer("true");
                } else {
                    WrongVirtualAnswer("true");
                }
            } else if (randomAnswer == 0) {

                if (question.isCorrect() == 0) {
                    RightVirtualAnswer("false");
                } else {
                    WrongVirtualAnswer("false");
                }
            }
        }
    }


    /*
     * A function use to send data to firebase when his answer right and update data in firebase
     * */
    public void RightVirtualAnswer(final String sel_ans) {
        questionIndex_vplayer++;
        correctQuestion_vplayer++;
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put(Constant.RIGHT, correctQuestion_vplayer);
        taskMap.put(Constant.QUE_NO, questionIndex_vplayer);
        taskMap.put(Constant.SEL_ANS, sel_ans);
        myGameRef.child(gameId).child(Player2UserID).updateChildren(taskMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        System.out.println("success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put(Constant.RIGHT, correctQuestion_vplayer);
                        taskMap.put(Constant.QUE_NO, questionIndex_vplayer);
                        taskMap.put(Constant.SEL_ANS, sel_ans);
                        myGameRef.child(gameId).child(Player2UserID).updateChildren(taskMap);
                    }
                });
    }


    /*
     * A function use to send data to firebase when his answer wrong and update data in firebase
     * */
    public void WrongVirtualAnswer(final String sel_ans) {

        inCorrectQuestion_vplayer++;
        questionIndex_vplayer++;
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put(Constant.WRONG, inCorrectQuestion_vplayer);
        taskMap.put(Constant.QUE_NO, questionIndex_vplayer);
        taskMap.put(Constant.SEL_ANS, sel_ans);

        myGameRef.child(gameId).child(Player2UserID).updateChildren(taskMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        System.out.println("success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put(Constant.WRONG, inCorrectQuestion_vplayer);
                        taskMap.put(Constant.QUE_NO, questionIndex_vplayer);
                        taskMap.put(Constant.SEL_ANS, sel_ans);
                        myGameRef.child(gameId).child(Player2UserID).updateChildren(taskMap);
                    }
                });


    }

    //----------------------------------------------------------------------------------------------------------


    //====================================CLass MyCount======================================================

    public class MyCount extends CountDownTimer {

        private MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            leftTime = millisUntilFinished;
            int progress = (int) (millisUntilFinished / 1000);
            timerOnGame = progress;
            txtTime.setText(String.valueOf(progress));
            Log.d(TAG, "onTick: leftTime: " + progress);
            if (virtual_play) {
                Log.d(TAG, "onTick: virtual_play");
                PerformVirtualClick();
            }
        }

        @Override
        public void onFinish() {
            if (questionIndex >= Constant.MAX_QUESTION_PER_BATTLE) {

            } else {
                inCorrectQuestion = inCorrectQuestion + 1;

                if (virtual_play) {
                    if (p2_sell.equals("")) {
                        WrongVirtualAnswer("wrong");
                    }
                }
                if (p1_sell.equals("")) {
                    WrongQuestion("wrong");
                }
                questionIndex++;
            }
        }

    }

    //======================================================================================================


    //-------------------------------All dialogs------------------------------------------------------------


    /*
     * A function use to show dialog on screen when finished game and scores equaled
     * */
    private void showResetGameAlert() {
        DatabaseReference databaseReference = myGameRef.child(gameId);
        if (databaseReference != null) {
            databaseReference.removeValue();
        }
        try {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_reset_game);
            dialog.setCancelable(false);
            TextView tvMessage = dialog.findViewById(R.id.tv_message);
            TextView btnok = dialog.findViewById(R.id.btn_ok);
            tvMessage.setText(getString(R.string.msg_draw_game));
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    dialog.dismiss();
                }
            });
            dialog.show();
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*
     * A function use to show dialog on screen when other player quited from game
     * */
    @SuppressLint("SetTextI18n")
    private void showOtherUserQuitDialog() {
        if (counter != null) {
            counter.cancel();
        }
        DatabaseReference databaseReference = myGameRef.child(gameId);
        if (databaseReference != null) {
            databaseReference.removeValue();
        }
        try {

            final AlertDialog.Builder dialog = new AlertDialog.Builder(PlayerActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.dialog_reset_game, null);
            dialog.setView(dialogView);
            dialog.setCancelable(false);
            quitAlertDialog = dialog.create();

            TextView tvMessage = dialogView.findViewById(R.id.tv_message);
            TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
            tvTitle.setText(Player1Name);
            TextView btnok = dialogView.findViewById(R.id.btn_ok);

            tvMessage.setText("You Win!! \n" + Player2Name + getString(R.string.leave_battle_txt));
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    quitAlertDialog.dismiss();
                    if (GetOpponentActivity.battleQuestionList != null)
                        GetOpponentActivity.battleQuestionList.clear();

                    if (GetOpponentActivity.questionArrayList != null)
                        GetOpponentActivity.questionArrayList.clear();
                }
            });


            Objects.requireNonNull(quitAlertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            quitAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    System.out.println("connected");
                } else {
                    System.out.println("not connected");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }


    /*
     * A function use to show dialog on screen when decided out of game or when pressed on back button
     * */
    private void showQuitGameAlertDialog() {
        try {
            if (counter != null) {
                counter.cancel();
            }
            final AlertDialog.Builder dialog1 = new AlertDialog.Builder(PlayerActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.dialog_leave_battle, null);
            dialog1.setView(dialogView);
            dialog1.setCancelable(true);

            final AlertDialog alertDialog = dialog1.create();
            TextView tvMessage = dialogView.findViewById(R.id.tv_message);
            TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
            tvTitle.setText(Player1Name);
            TextView btnok = dialogView.findViewById(R.id.btn_ok);
            TextView btnNo = dialogView.findViewById(R.id.btnNo);
            tvMessage.setText(getString(R.string.msg_alert_leave));
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    myGameRef.child(gameId).child(FirebaseAuth.getInstance().getUid()).child(Constant.STATUS).setValue(false);
                    finish();

                    if (GetOpponentActivity.battleQuestionList != null)
                        GetOpponentActivity.battleQuestionList.clear();

                    if (GetOpponentActivity.questionArrayList != null)
                        GetOpponentActivity.questionArrayList.clear();
                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    counter = new MyCount(leftTime, 1000);
                    counter.start();
                    alertDialog.dismiss();
                }
            });
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setCancelable(false);
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     * A function use to show dialog on screen when ended game
     * */
    private void showWinnerDialog() {
        try {


            if (counter != null) {
                counter.cancel();
            }

            DatabaseReference databaseReference = myGameRef.child(gameId);
            if (databaseReference != null) {
                databaseReference.removeValue();
            }

            final AlertDialog.Builder dialog1 = new AlertDialog.Builder(PlayerActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.winner_dialog, null);
            dialog1.setView(dialogView);
            dialog1.setCancelable(false);
            final AlertDialog alertDialog = dialog1.create();
            TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
            TextView tvMessage = dialogView.findViewById(R.id.tv_message);
            Button btnok = dialogView.findViewById(R.id.btn_ok);
            Button btnReBattle = dialogView.findViewById(R.id.btnReBattle);
            NetworkImageView winnerImg = dialogView.findViewById(R.id.winnerImg);
            if (winner.equals("you")) {
                tvTitle.setText(getString(R.string.congrats));
                tvMessage.setText(winnerMessage);
            } else {
                tvTitle.setText(getString(R.string.next_time));
                tvMessage.setText(winnerMessage);
            }

            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference databaseReference = myGameRef.child(gameId);
                    if (databaseReference != null) {
                        databaseReference.removeValue();
                    }
                    finish();
                    alertDialog.dismiss();
                }
            });

            btnReBattle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentReBattle = new Intent(PlayerActivity.this, GetOpponentActivity.class);
                    if (Player2UserID.length() > 8) {
                        Log.d(TAG, "onClick: length > 8");
                        intentReBattle.putExtra("player2UserId", Player2UserID);
                    }
                    startActivity(intentReBattle);
                    finish();
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     * A function use to show dialog on screen when ordered request rematch
     * */
    @SuppressLint("SetTextI18n")
    private void showReqRe_Match() {
        try {
            if (counter != null) {
                counter.cancel();
            }
            final AlertDialog.Builder dialog1 = new AlertDialog.Builder(PlayerActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.dialog_req_re_match, null);
            dialog1.setView(dialogView);
            dialog1.setCancelable(true);

            final AlertDialog alertDialog = dialog1.create();
            TextView tvMessage = dialogView.findViewById(R.id.tv_message);
            TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
            tvTitle.setText(Player1Name);
            TextView btnok = dialogView.findViewById(R.id.btn_ok);
            TextView btnNo = dialogView.findViewById(R.id.btnNo);
            tvMessage.setText("Do you want play again with "+Player2Name+"?");
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    myGameRef.child(gameId).child(FirebaseAuth.getInstance().getUid()).child(Constant.STATUS).setValue(false);
                    finish();

                    if (GetOpponentActivity.battleQuestionList != null)
                        GetOpponentActivity.battleQuestionList.clear();
                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    counter = new MyCount(leftTime, 1000);
                    counter.start();
                    alertDialog.dismiss();
                }
            });
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setCancelable(false);
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     //----------------------------------------------------------------------------------------------------------


    /*
     * A function use to generate random number
     * */
    public int getRandom(int max) {
        return (int) (Math.random() * max);
    }


    @Override
    public void onBackPressed() {
        showQuitGameAlertDialog();
    }


    @Override
    protected void onStop() {
        super.onStop();
        UpdateOnlineStatus();
    }


    @Override
    protected void onDestroy() {

        if (counter != null) {
            counter.cancel();
        }

        if (quitAlertDialog != null) {
            if (quitAlertDialog.isShowing()) {
                quitAlertDialog.dismiss();
            }
        }
        UpdateOnlineStatus();
        super.onDestroy();


    }


}
