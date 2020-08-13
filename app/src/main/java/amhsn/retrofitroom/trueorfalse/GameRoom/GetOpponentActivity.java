package amhsn.retrofitroom.trueorfalse.GameRoom;

import android.annotation.SuppressLint;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import amhsn.retrofitroom.trueorfalse.Constant;
import amhsn.retrofitroom.trueorfalse.R;
import amhsn.retrofitroom.trueorfalse.data.responseJson.jsonquestion.ResponseAPI;
import amhsn.retrofitroom.trueorfalse.helper.ApiClient;
import amhsn.retrofitroom.trueorfalse.helper.ApiInterface;
import amhsn.retrofitroom.trueorfalse.helper.AppController;
import amhsn.retrofitroom.trueorfalse.helper.CircleImageView;
import amhsn.retrofitroom.trueorfalse.helper.Session;
import amhsn.retrofitroom.trueorfalse.helper.Utils;
import amhsn.retrofitroom.trueorfalse.model.User;
import amhsn.retrofitroom.trueorfalse.room.entity.Question;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetOpponentActivity extends AppCompatActivity {

    // vars
    private final static String TAG = "GetOpponentActivity";
    private static final String FORMAT = "%02d";
    private static String roomKey = "";
    private boolean exist = false;
    private Context mContext;
    private String battleStart = "false", pauseCheck = "regular", questionResponse = "true", profilePlayer2, player1Name, player2Name,
            userId1, userId2, fcm1, fcm2, player, opponentId = "", player2UserId = "";
    private boolean isPlayStarted;
    private ArrayList<User> battleList;
    private boolean isRunning = false;
    private boolean player1Status, player2Status;
    private String numRandom = "";
    public static List<Question> battleQuestionList, questionArrayList;
    private static CountDownTimer countDownTimer;


    // widgets
    private Toolbar toolbar;
    private TextView tvPlayer1, tvPlayer2, tvTimeLeft, tvSecond, tvSearch, tvSearchPlayer, tvStateTitle;
    private NetworkImageView imgPlayer1, imgPlayer2;
    private DatabaseReference database, myRef;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private LinearLayout alertLayout, contentLayout;
    private RelativeLayout timerLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ProgressDialog mProgressDialog;
    private AlertDialog quiteDialog;
    private AlertDialog leaveDialog, timeAlertDialog, battleDialog;

    // listener
    private ValueEventListener valueEventListener, valueEventListener2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: onStarted.");
        setContentView(R.layout.activity_get_opponent);
        generateRandomNumber();

        // initialize mContext
        mContext = GetOpponentActivity.this;

        // Create and initialize Toolbar
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.matching_opponent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // receive data from Player activity
        if (getIntent().getExtras() != null) {
            player2UserId = getIntent().getStringExtra("player2UserId");
            Log.d(TAG, "onCreate: player2UserId: " + player2UserId);
        }

        // initialize reference to view
        contentLayout = findViewById(R.id.contentLayout);
        timerLayout = findViewById(R.id.timerLayout);
        alertLayout = findViewById(R.id.alertLayout);
        tvPlayer1 = findViewById(R.id.tv_player1_name);
        imgPlayer1 = findViewById(R.id.imgPlayer1);
        tvPlayer2 = findViewById(R.id.tv_player2_name);
        imgPlayer2 = findViewById(R.id.imgPlayer2);
        tvTimeLeft = findViewById(R.id.tv_time_left);
        tvStateTitle = findViewById(R.id.tvStateTitle);
        tvSearchPlayer = findViewById(R.id.tvSearchPlayer);
        imgPlayer1.setDefaultImageResId(R.drawable.ic_profile);
        imgPlayer2.setDefaultImageResId(R.drawable.ic_profile);
        imgPlayer2.setDefaultImageResId(R.drawable.ic_profile);
        progressBar = findViewById(R.id.progressBar);
        tvPlayer1.setText(getString(R.string.player_1));
        tvPlayer2.setText(getString(R.string.player_2));
        tvSecond = findViewById(R.id.tvSec);
        tvSearch = findViewById(R.id.tvSearch);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);
        Utils.GetSystemConfig(getApplicationContext());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        /*
         *  this interface can be used to receive events about data changes at a location
         */
        valueEventListener = new ValueEventListener() {
            /*
            This method will be called with a snapshot of the data at this location.
            */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.exists() && ds.child(Constant.AVAILABILITY).getValue() != null) {
                            if (ds.child(Constant.AVAILABILITY).getValue() != null) {

                                if (ds.child(Constant.AVAILABILITY).getValue().toString().equalsIgnoreCase("1")) {
                                    if (ds.child(player).exists()) {
                                        roomKey = ds.getKey();
                                        Constant.GameRoomKey = ds.getKey();
                                    }
                                }

                                if (ds.child(Constant.AVAILABILITY).getValue().toString().equalsIgnoreCase("2")) {
                                    if (ds.child(player).exists()) {
                                        roomKey = ds.getKey();
                                        Constant.GameRoomKey = ds.getKey();
                                        for (DataSnapshot data : ds.getChildren()) {

                                            if (!data.getKey().equalsIgnoreCase(player)) {
                                                if (!data.getKey().equalsIgnoreCase(Constant.AVAILABILITY)) {
                                                    if (!data.getKey().equalsIgnoreCase(Constant.LANGUAGE_ID)) {
                                                        if (!data.getKey().equalsIgnoreCase(Constant.QUE_ID)) {
                                                            if (!data.getKey().equalsIgnoreCase(Constant.USER_ID_1)) {
                                                                if (!data.getKey().equalsIgnoreCase(Constant.USER_ID_2)) {
                                                                    opponentId = data.getKey();
                                                                    Log.d(TAG, "onDataChange: opponentId: " + opponentId);
                                                                    setSecondPlayerData();

                                                                    try {

                                                                        if (ds.child(player).getValue() != null)
                                                                            player1Status = (boolean) (ds.child(player).child(Constant.STATUS).getValue());
                                                                        if (ds.child(opponentId).getValue() != null)
                                                                            player2Status = (boolean) ds.child(opponentId).child(Constant.STATUS).getValue();

                                                                        if (player1Status && player2Status) {
                                                                            isPlayStarted = true;
                                                                        }
                                                                        if (isPlayStarted) {
                                                                            if (!player1Status || !player2Status) {
                                                                                showOtherUserQuitDialog();
                                                                            }


                                                                        }

                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }


                                    }
                                }
                            }
                        }
                    }

                }
            }

            /*
             *  This method will be triggered in the event that this listener either failed at the server
             */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        /*
         *  this interface can be used to receive events about data changes at a location
         */
        valueEventListener2 = new ValueEventListener() {
            /*
            This method will be called with a snapshot of the data at this location.
            */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.exists() && ds.child(Constant.AVAILABILITY).getValue() != null) {
                            if (ds.child(Constant.AVAILABILITY).getValue() != null) {

                                if (ds.child(Constant.AVAILABILITY).getValue().toString().equalsIgnoreCase("1")) {
                                    if (ds.child(player).exists()) {
                                        roomKey = ds.getKey();
                                        Constant.GameRoomKey = ds.getKey();
                                    }
                                }

                                if (ds.child(Constant.AVAILABILITY).getValue().toString().equalsIgnoreCase("2")) {
                                    if (ds.child(player).exists()) {
                                        roomKey = ds.getKey();
                                        Constant.GameRoomKey = ds.getKey();
                                        for (DataSnapshot data : ds.getChildren()) {

                                            if (!data.getKey().equalsIgnoreCase(player)) {
                                                if (!data.getKey().equalsIgnoreCase(Constant.AVAILABILITY)) {
                                                    if (!data.getKey().equalsIgnoreCase(Constant.LANGUAGE_ID)) {
                                                        if (!data.getKey().equalsIgnoreCase(Constant.QUE_ID)) {
                                                            if (!data.getKey().equalsIgnoreCase(Constant.USER_ID_1)) {
                                                                if (!data.getKey().equalsIgnoreCase(Constant.USER_ID_2)) {
                                                                    opponentId = data.getKey();
                                                                    Log.d(TAG, "onDataChange: opponentId: " + opponentId + ",  player2UserId: " + player2UserId);
                                                                    if (opponentId.equals(player2UserId)) {
                                                                        setSecondPlayerData();

                                                                        try {

                                                                            if (ds.child(player).getValue() != null)
                                                                                player1Status = (boolean) (ds.child(player).child(Constant.STATUS).getValue());
                                                                            if (ds.child(opponentId).getValue() != null)
                                                                                player2Status = (boolean) ds.child(opponentId).child(Constant.STATUS).getValue();

                                                                            if (player1Status && player2Status) {
                                                                                isPlayStarted = true;
                                                                            }
                                                                            if (isPlayStarted) {
                                                                                if (!player1Status || !player2Status) {
                                                                                    showOtherUserQuitDialog();
                                                                                }


                                                                            }

                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }


                                    }
                                }
                            }
                        }
                    }

                }
            }

            /*
             *  This method will be triggered in the event that this listener either failed at the server
             */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        getData();
    }


    /*
     * A function to check internet
     * and get information current user from firebase
     * */
    public void getData() {

        if (Utils.isNetworkAvailable(GetOpponentActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            player = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            database = FirebaseDatabase.getInstance().getReference();
            alertLayout.setVisibility(View.GONE);
            contentLayout.setVisibility(View.VISIBLE);
            timerLayout.setVisibility(View.GONE);
            setFirstPlayerData();
            tvSearchPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: search player");
                    SearchPlayerClickMethod();
                }
            });
        } else {
            alertLayout.setVisibility(View.VISIBLE);
            contentLayout.setVisibility(View.GONE);
            setSnackBar();

        }

    }


    /*
     * A function to create gameRoom in firebase
     * */
    public void SearchPlayerClickMethod() {
        exist = true;
        timerLayout.setVisibility(View.VISIBLE);
        tvSearchPlayer.setVisibility(View.GONE);
        myRef = FirebaseDatabase.getInstance().getReference(Constant.DB_GAME_ROOM);
        getQuestionForComputer();
        callGetRoomFunction();
        startTimer();
        if (player2UserId.isEmpty()) {
            Log.d(TAG, "SearchPlayerClickMethod: player2UserId is empty: " + player2UserId);
            myRef.addValueEventListener(valueEventListener);
        } else {
            Log.d(TAG, "SearchPlayerClickMethod: player2UserId is not empty: " + player2UserId);
            myRef.addValueEventListener(valueEventListener2);
        }
    }


    //-------------------------------------Set Data Players On View-------------------------------------------
    /*
     * A function use to get data first player from firebase
     * and put data in view
     * */
    private void setFirstPlayerData() {

        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        database.child(Constant.DB_USER)
                .child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            player1Name = (String) dataSnapshot.child(Constant.USER_NAME).getValue();
                            userId1 = (String) dataSnapshot.child(Constant.USER_ID).getValue();
                            fcm1 = (String) dataSnapshot.child(Constant.FCM_ID).getValue();
                            tvPlayer1.setText(player1Name);
                            imgPlayer1.setImageUrl(dataSnapshot.child(Constant.PROFILE_PIC).getValue().toString(), imageLoader);
                            progressBar.setVisibility(View.GONE);
                            tvTimeLeft.setVisibility(View.VISIBLE);
                            tvSecond.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        database.child(Constant.DB_USER).child(userID).child(Constant.ONLINE_STATUS).setValue(true);


    }


    /*
     * A function use to get data second player from firebase
     * and put data in view
     * */
    private void setSecondPlayerData() {

        database.child(Constant.DB_USER).child(opponentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    player2Name = (String) dataSnapshot.child(Constant.USER_NAME).getValue();
                    userId2 = (String) dataSnapshot.child(Constant.USER_ID).getValue();
                    fcm2 = (String) dataSnapshot.child(Constant.FCM_ID).getValue();
                    profilePlayer2 = dataSnapshot.child(Constant.PROFILE_PIC).getValue().toString();
                    tvPlayer2.setText(player2Name);
                    imgPlayer2.setImageUrl(profilePlayer2, imageLoader);
                    tvSearch.setText(getString(R.string.battle_start_message));
                    if (questionResponse.equals("true")) {
                        getQuestionsFromJson();
                    }


                } else {
                    player2Name = getString(R.string.robot);
                    tvPlayer2.setText(getString(R.string.robot));
                    showTimeUpAlert(tvPlayer2.getText().toString());
                    imgPlayer2.setDefaultImageResId(R.drawable.ic_android);
                    imgPlayer2.setColorFilter(Color.WHITE);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //------------------------------------------------------------------------------------------------------------------


    //---------------------------------All Dialogs-------------------------------------------------------------------

    /*
     * show alert dialog when current user not available for some reason
     */
    @SuppressLint("SetTextI18n")
    private void showTimeUpAlert(final String playWith) {

        try {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            hideProgressDialog();
            final AlertDialog.Builder dialog = new AlertDialog.Builder(GetOpponentActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.dialog_time_up, null);
            dialog.setView(dialogView);
            TextView tvExit = dialogView.findViewById(R.id.tvExit);

            CircleImageView playerImg = dialogView.findViewById(R.id.imgPlayer);
            LinearLayout tryLayout = dialogView.findViewById(R.id.tryLayout);
            TextView btnRobot = dialogView.findViewById(R.id.btnRobot);
            TextView btnTryAgain = dialogView.findViewById(R.id.btnTryAgain);


            tryLayout.setVisibility(View.VISIBLE);
            playerImg.setErrorImageResId(R.drawable.ic_android);
            playerImg.setDefaultImageResId(R.drawable.ic_android);
            //  }

            timeAlertDialog = dialog.create();
            tvExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myRef.child(roomKey).child(player).child(Constant.STATUS).setValue(false);
                    if (!roomKey.equalsIgnoreCase("")) {
                        myRef.child(roomKey).removeValue();
                    }

                    if (valueEventListener != null)
                        myRef.removeEventListener(valueEventListener);

                    if (valueEventListener2 != null)
                        myRef.removeEventListener(valueEventListener2);

                    finish();
                    timeAlertDialog.dismiss();
                }
            });

            btnRobot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (playWith.equals(getString(R.string.player_2)) || playWith.equals(getString(R.string.robot))) {

                        if (questionArrayList.size() != 0) {
                            callGamePlayActivity();
                        } else {
                            Toast.makeText(GetOpponentActivity.this, getString(R.string.question_not_available), Toast.LENGTH_SHORT).show();
                        }
                    }
                    timeAlertDialog.dismiss();
                }
            });
            btnTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeAlertDialog.dismiss();
                    if (!roomKey.equalsIgnoreCase("")) {
                        myRef.child(roomKey).removeValue();
                    }
                    myRef.removeEventListener(valueEventListener);
                    ReloadUserForBattle();
                }
            });
            timeAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            timeAlertDialog.setCancelable(false);
            timeAlertDialog.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     * show alert dialog when current user go out from the game
     */
    @SuppressLint("SetTextI18n")
    private void showOtherUserQuitDialog() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        DatabaseReference databaseReference = myRef.child(roomKey);
        if (databaseReference != null) {
            databaseReference.removeValue();
        }
        try {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(GetOpponentActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.dialog_re_search, null);
            dialog.setView(dialogView);
            dialog.setCancelable(false);
            quiteDialog = dialog.create();
            TextView tvMessage = dialogView.findViewById(R.id.tv_message);
            TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
            TextView tvSearch = dialogView.findViewById(R.id.tvSearch);
            tvTitle.setText(tvPlayer1.getText().toString());
            TextView btnok = dialogView.findViewById(R.id.btnExit);

            tvMessage.setText(player2Name + getString(R.string.leave_battle_txt));

            tvSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ReloadUserForBattle();
                    quiteDialog.dismiss();
                }
            });
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myRef.removeEventListener(valueEventListener);
                    finish();
                    quiteDialog.dismiss();
                }
            });

            Objects.requireNonNull(quiteDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            if (timeAlertDialog != null)
                if (timeAlertDialog.isShowing()) {
                    timeAlertDialog.dismiss();
                }
            quiteDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*
     * show alert dialog when current user back to another activity
     */
    public void BackDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(GetOpponentActivity.this);

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.back_message));
        alertDialog.setCancelable(false);
        leaveDialog = alertDialog.create();
        // Setting OK Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (Utils.isNetworkAvailable(GetOpponentActivity.this)) {
                    if (countDownTimer != null)
                        countDownTimer.cancel();
                    if (myRef != null) {
                        myRef.child(roomKey).child(player).child(Constant.STATUS).setValue(false);
                        if (!roomKey.equalsIgnoreCase("")) {
                            myRef.child(roomKey).removeValue();
                        }

                        if (valueEventListener != null)
                            myRef.removeEventListener(valueEventListener);

                        if (valueEventListener2 != null)
                            myRef.removeEventListener(valueEventListener2);
                    }
                }
                leaveDialog.dismiss();
                finish();
                // onBackPressed();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                leaveDialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


    /*
     * A function use to reload user fro battle
     * */
    public void ReloadUserForBattle() {
        tvPlayer1.setText(getString(R.string.player_1));
        tvPlayer2.setText(getString(R.string.player_2));
        player = "";
        opponentId = "";
        questionResponse = "true";

        imgPlayer1.setImageUrl("removed", imageLoader);
        imgPlayer2.setImageUrl("removed", imageLoader);
        imgPlayer1.setDefaultImageResId(R.drawable.ic_profile);
        imgPlayer2.setDefaultImageResId(R.drawable.ic_profile);
        imgPlayer1.setErrorImageResId(R.drawable.ic_profile);
        imgPlayer2.setErrorImageResId(R.drawable.ic_profile);
        roomKey = "";
        if (battleQuestionList != null)
            battleQuestionList.clear();
        getData();
        SearchPlayerClickMethod();
    }


    /*
     * show alert dialog when create player virtual in game room
     */
    public void BattleDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(GetOpponentActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.progress_dialog_lyt, null);
        dialog.setView(dialogView);

        TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
        Button btnok = dialogView.findViewById(R.id.btnExit);
        battleDialog = dialog.create();
        tvMessage.setText(getString(R.string.battle_start_message));
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do ur stuff
                myRef.child(roomKey).child(player).child(Constant.STATUS).setValue(false);
                if (!roomKey.equalsIgnoreCase("")) {
                    myRef.child(roomKey).removeValue();
                }

                if (valueEventListener != null)
                    myRef.removeEventListener(valueEventListener);

                if (valueEventListener2 != null)
                    myRef.removeEventListener(valueEventListener2);

                finish();
            }
        });

        battleDialog.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!questionResponse.equals("false")) {
                    if (battleDialog != null)
                        if (battleDialog.isShowing())
                            battleDialog.dismiss();
                    callGetRoomFunction_Virtual();
                    //  showTimeUpAlert(tvPlayer2.getText().toString());

                } else {
                    if (battleStart.equals("false")) {
                        callGamePlayActivity();
                        battleStart = "true";
                    }
                }

            }

        }, 5000);

    }


    /*
     * show ProgressDialog to current user
     * */
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }


    /*
     * hide ProgressDialog to current user
     * */
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    //-----------------------------------------------------------------------------------------------------------------

    /*
     * A function use to navigating to PlayerActivity
     * */
    private void callGamePlayActivity() {
        Log.d(TAG, "callGamePlayActivity: Navigating to PlayerActivity");
        if (!opponentId.equalsIgnoreCase("")) {
            exist = false;
            startActivityForResult(new Intent(mContext, PlayerActivity.class)
                    .putExtra("gameid", roomKey)
                    .putExtra("opponentId", opponentId)
                    .putExtra("battlePlayer", tvPlayer2.getText().toString())
                    .putExtra("user_id1", userId1)
                    .putExtra("user_id2", userId2)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 1);

            if (valueEventListener != null)
                myRef.removeEventListener(valueEventListener);

            if (valueEventListener2 != null)
                myRef.removeEventListener(valueEventListener2);

            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            questionResponse = "true";
            battleStart = "true";
            finish();


        }
    }


    //-------------------------------------Get Data (Questions) from server-----------------------------------

    /*
     * A function use to get questions from server
     * */
    public void getQuestionsFromJson() {
        try {
            Call<ResponseAPI> call = amhsn.retrofitroom.trueorfalse.data.ApiClient
                    .getINSTANCE()
                    .getAPI()
                    .getAllPosts();

            //  response = service.makeRequest().execute().body();
            call.enqueue(new Callback<ResponseAPI>() {
                @Override
                public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {

                    Log.d("Repository", "Response::::" + response.body());

                    ResponseAPI responseAPI = response.body();

                    assert responseAPI != null;
                    List<Question> questionList = responseAPI.getServerres();

                    battleQuestionList = questionList;
                    questionArrayList = questionList;

                    if (battleQuestionList.size() > 0 && questionArrayList.size() > 0) {
                        questionResponse = "false";
                    }
                    Log.i("TAG", "onResponse: battleQuestionList: " + battleQuestionList.size());

                }

                @Override
                public void onFailure(Call<ResponseAPI> call, Throwable t) {
                    Log.e("TAG", "onFailure: message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * A function use to get questions from server to play with computer
     * */
    public void getQuestionForComputer() {
        try {
            Call<ResponseAPI> call = amhsn.retrofitroom.trueorfalse.data.ApiClient
                    .getINSTANCE()
                    .getAPI()
                    .getAllPosts();

            //  response = service.makeRequest().execute().body();
            call.enqueue(new Callback<ResponseAPI>() {
                @Override
                public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {

                    Log.d("Repository", "Response::::" + response.body());

                    ResponseAPI responseAPI = response.body();

                    assert responseAPI != null;
                    List<Question> questionList = responseAPI.getServerres();

                    questionArrayList = questionList;

//                    Log.i("TAG", "onResponse: battleQuestionList: " + battleQuestionList.size());

                }

                @Override
                public void onFailure(Call<ResponseAPI> call, Throwable t) {
                    Log.e("TAG", "onFailure: message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------------------


    //=======================================CAll CLOUD FUNCTION FIREBASE==========================================

    /*
     * Create Room Function
     * */
    void callGetRoomFunction() {
        Map<String, String> registerMap = new HashMap<>();
        registerMap.put(Constant.USER_ID, player);
        registerMap.put(Constant.LANGUAGE_ID, Session.getCurrentLanguage(getApplicationContext()));
        registerMap.put(Constant.QUE_ID, numRandom);
        registerMap.put(Constant.USER_ID_1, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        registerMap.put(Constant.USER_ID_2, opponentId);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Map> call = apiService.create(registerMap);
        call.enqueue(new retrofit2.Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                try {
                    if (response.body() != null) {
                        Log.e("game room response****", response.body().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {

            }
        });
    }


    /*
     * Create Room Function Virtual
     * */
    private void callGetRoomFunction_Virtual() {
        Map<String, String> registerMap = new HashMap<>();
        registerMap.put(Constant.USER_ID, randomAlphaNumeric(8));
        registerMap.put(Constant.LANGUAGE_ID, Session.getCurrentLanguage(getApplicationContext()));
        registerMap.put(Constant.QUE_ID, numRandom);
        registerMap.put(Constant.USER_ID_1, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        registerMap.put(Constant.USER_ID_2, opponentId);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Map> call = apiService.create(registerMap);
        call.enqueue(new retrofit2.Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                try {

                    if (response.body() != null) {
                        assert response.body() != null;
                    }
                    showProgressDialog();

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
            }
        });
    }

    //==============================================================================================================


    /*
     * A function create custom snackBar
     * */
    public void setSnackBar() {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), getString(R.string.msg_no_internet), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getData();
                    }
                });

        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }


    /*
     * A function generate random alpha numeric
     * */
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * Constant.ALPHA_NUMERIC_STRING.length());
            builder.append(Constant.ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


    /*
     * use timer toh get opposite player in specific time
     * */
    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(Constant.OPPONENT_SEARCH_TIME, Constant.COUNT_DOWN_TIMER) {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {
                isRunning = true;
                int progress = (int) (millisUntilFinished / 1000);
                tvTimeLeft.setText("" + String.format(FORMAT, progress));

                if (questionResponse.equals("false")) {

                    if (battleStart.equals("false")) {
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        callGamePlayActivity();
                        battleStart = "true";
                    }
                }
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                isRunning = false;
                tvTimeLeft.setText("00");
                if (tvPlayer1.getText().toString().equals(getString(R.string.player_1))) {
                    showTimeUpAlert(tvPlayer2.getText().toString());
                } else {

                    if (battleQuestionList != null) {

                        if (questionResponse.equals("false")) {
                            if (battleStart.equals("false")) {
                                callGamePlayActivity();
                                battleStart = "true";
                            }
                        } else {
                            BattleDialog();
                        }

                    } else {
                        callGetRoomFunction_Virtual();
                    }

                }
            }
        }.start();
    }


    /*
     * A function use to sort user
     * */
    private void sortDataUser() {

        Query sortUserOrder = database.child("user").orderByChild("num_of_wins");


        ValueEventListener _valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    List<User> users = new ArrayList<>();
                    for (DataSnapshot _data : dataSnapshot.getChildren()) {

                        if (_data.exists() && _data.child(Constant.USER_NAME).getValue() != null) {
                            String playerName = String.valueOf(_data.child(Constant.USER_NAME).getValue());
                            int numWins = Integer.parseInt(String.valueOf(_data.child("num_of_wins").getValue()));
                            users.add(new User(playerName, numWins));
                            Collections.sort(users, Collections.reverseOrder());
                        }
                    }

                    for (User user : users) {
                        Log.d("TAG", "onDataChange: playerName: " + user.getName() + "  " + user.getNum_of_wins());
                    }

                } catch (Exception e) {
                    e.toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        sortUserOrder.addValueEventListener(_valueEventListener);
    }


    /*
     * A function use to generate random number
     * */
    private void generateRandomNumber() {
        for (int i = 0; i < 10; i++) {
            int nxt = (new Random().nextInt(310) + 1);
            if (i != 9) {
                numRandom = numRandom + nxt + ",";
            } else {
                numRandom = numRandom + nxt;
            }
        }

        Log.d(TAG, "generateRandomNumber: Number random: " + numRandom);
    }

    //--------------------------------------------Life Cycle Activity------------------------------------------

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString("request") != null) {
                SearchPlayerClickMethod();
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

    }


    @Override
    public void onBackPressed() {
        if (countDownTimer != null)
            BackDialog();
        else
            super.onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (Utils.isNetworkAvailable(GetOpponentActivity.this)) {
            if (exist) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                myRef.child(roomKey).child(player).child(Constant.STATUS).setValue(false);
                if (!roomKey.equalsIgnoreCase("")) {
                    myRef.child(roomKey).removeValue();
                }

                if (valueEventListener != null)
                    myRef.removeEventListener(valueEventListener);

                if (valueEventListener2 != null)
                    myRef.removeEventListener(valueEventListener2);

                finish();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    //---------------------------------------------------------------------------------------------------

}