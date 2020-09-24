package amhsn.retrofitroom.trueorfalse.GameRoom;

import android.annotation.SuppressLint;

import android.app.Activity;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import amhsn.retrofitroom.trueorfalse.Constant;
import amhsn.retrofitroom.trueorfalse.R;
import amhsn.retrofitroom.trueorfalse.data.responseJson.jsonquestion.ResponseAPI;
import amhsn.retrofitroom.trueorfalse.helper.ApiClient;
import amhsn.retrofitroom.trueorfalse.helper.ApiInterface;
import amhsn.retrofitroom.trueorfalse.helper.CircleImageView;
import amhsn.retrofitroom.trueorfalse.helper.Session;
import amhsn.retrofitroom.trueorfalse.helper.Utils;
import amhsn.retrofitroom.trueorfalse.model.RequestModel;
import amhsn.retrofitroom.trueorfalse.model.User;
import amhsn.retrofitroom.trueorfalse.notification.MyApplication;
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
    private ArrayList<User> battleList = new ArrayList<>();
    private boolean isRunning = false, isStarting = false;
    private boolean player1Status, player2Status;
    private String numRandom = "";
    public static List<Question> battleQuestionList, questionArrayList;
    private static CountDownTimer countDownTimer, countDownTimerRequest;
    private FirebaseFunctions mFunctions;
    long timeNow = 0;


    // widgets
    private Toolbar toolbar;
    private EditText etPrivateKey;
    private TextView tvPlayer1, tvPlayer2, tvTimeLeft, tvSecond, tvSearch, tvSearchPlayer, tvStateTitle, tvJoinGame, tvPlayWithFriend, tv_time_left_request;
    private NetworkImageView imgPlayer1, imgPlayer2;
    private DatabaseReference database, myRef, gameRequestRef;
    //    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private LinearLayout alertLayout, contentLayout;
    private RelativeLayout timerLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ProgressDialog mProgressDialog;
    private AlertDialog quiteDialog, listDialog;
    private AlertDialog leaveDialog, timeAlertDialog, battleDialog, requestDialog;
    private View itemRequest;

    // listener
    private ValueEventListener valueEventListener, valueEventListener2, valueEventListener3;
    private String privateKey;
    private List<User> usersList = new ArrayList<>();
    private FriendsListAdapter adapter;
    private HashMap<String, Object> timestampCreated;
    private String userNameSender;
    private String requestKey = null;
    private String uidSender;
    private boolean isCreatedRoom = false;
    private String uidreciver = null;
    private MyApplication mMyApp;

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

        mMyApp = (MyApplication)this.getApplicationContext();

        // initialize reference to view
        adapter = new FriendsListAdapter(this);
        mFunctions = FirebaseFunctions.getInstance();
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
        tvJoinGame = findViewById(R.id.tvJoinGame);
        tvPlayWithFriend = findViewById(R.id.tvPlayWithFriend);
        etPrivateKey = findViewById(R.id.etPrivateKey);
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
                                                                    if (!data.getKey().equalsIgnoreCase("private_key")) {
                                                                        if (!data.getKey().equalsIgnoreCase("timestamp")) {
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
                                                                    if (!data.getKey().equalsIgnoreCase("private_key")) {
                                                                        if (!data.getKey().equalsIgnoreCase("timestamp")) {
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
        callGetTimeFunction();
        getAllFriends();

        if(Constant.flag){
            showWinnerDialog();
        }
//        getAllRequests();
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
            myRef = FirebaseDatabase.getInstance().getReference(Constant.DB_GAME_ROOM);
            gameRequestRef = FirebaseDatabase.getInstance().getReference("game_request");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gameRequestRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            if (dataSnapshot.exists()) {
                                callGetTimeFunction();
                                String key = String.valueOf(dataSnapshot.getKey());
                                Log.d(TAG, "onChildAdded: " + key);
                                long timestamp = (long) dataSnapshot.child("timestamp").getValue();
                                uidSender = String.valueOf(dataSnapshot.child("user_id_sender").getValue());
                                uidreciver = String.valueOf(dataSnapshot.child("user_id_receiver").getValue());
                                long timeIn = timeNow - timestamp;

                                if (timeIn < 10 * 1000) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        if (!snapshot.getKey().equalsIgnoreCase("timestamp")) {
                                            if (!snapshot.getKey().equalsIgnoreCase("user_id_sender")) {
                                                if (snapshot.getKey().equalsIgnoreCase("user_id_receiver")) {
                                                    if (snapshot.getValue().equals(player)) {
                                                        if (!isStarting) {
                                                            requestKey = key;
                                                            showRequestGameDialog(String.valueOf(uidSender));
                                                            Log.d(TAG, "onChildAdded: yesssssssssssssssssssss :: " + snapshot.getValue());
                                                            return;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Log.d(TAG, "onChildAdded: =============================");
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            Log.d(TAG, "onChildChanged: ");
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "onChildRemoved: ");
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            Log.d(TAG, "onChildMoved: ");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

//                    createRoom();
                }
            }, 2000);

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


            tvJoinGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    privateKey = etPrivateKey.getText().toString();
                    if (!privateKey.isEmpty()) {
                        SearchPlayerClickMethod1();
                    } else {
                        Toast.makeText(mContext, "enter private key", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            tvPlayWithFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playWithFriends();
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
        getQuestionForComputer();
        callGetRoomFunction(1);
        startTimer();
        if (player2UserId.isEmpty()) {
            Log.d(TAG, "SearchPlayerClickMethod: player2UserId is empty");
            myRef.addValueEventListener(valueEventListener);
        } else {
            Log.d(TAG, "SearchPlayerClickMethod: player2UserId is not empty: " + player2UserId);
            myRef.addValueEventListener(valueEventListener2);
        }
    }

    public void SearchPlayerClickMethod2() {
        exist = true;
        timerLayout.setVisibility(View.VISIBLE);
        tvSearchPlayer.setVisibility(View.GONE);
        getQuestionForComputer();
        callGetRoomFunction(1);
        startTimer();

        myRef.addValueEventListener(valueEventListener2);

    }


    public void SearchPlayerClickMethod1() {
        exist = true;
        timerLayout.setVisibility(View.VISIBLE);
        tvSearchPlayer.setVisibility(View.GONE);
        myRef = FirebaseDatabase.getInstance().getReference(Constant.DB_GAME_ROOM);
        getQuestionForComputer();
        callGetRoomFunction(2);
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
//                            imgPlayer1.setImageUrl(dataSnapshot.child(Constant.PROFILE_PIC).getValue().toString(), imageLoader);
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
//                    profilePlayer2 = dataSnapshot.child(Constant.PROFILE_PIC).getValue().toString();
                    tvPlayer2.setText(player2Name);
//                    imgPlayer2.setImageUrl(profilePlayer2, imageLoader);
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


    private void playWithFriends() {

        try {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(GetOpponentActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View dialogView = inflater.inflate(R.layout.dialog_all_friend, null);
            dialog.setView(dialogView);
            dialog.setCancelable(true);
            listDialog = dialog.create();
            RecyclerView recVw = dialogView.findViewById(R.id.list_friend);
            recVw.setHasFixedSize(true);
            recVw.setLayoutManager(new LinearLayoutManager(this));
            recVw.setAdapter(adapter);

            adapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    itemRequest = view;
                    String uid = String.valueOf(usersList.get(position).getUser_id());
                    callGetRequestFunction(uid);
                    itemRequest.setVisibility(View.INVISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            itemRequest.setVisibility(View.VISIBLE);
                        }
                    }, 10000);

                }
            });

            Objects.requireNonNull(listDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


            listDialog.show();
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

//        imgPlayer1.setImageUrl("removed", imageLoader);
//        imgPlayer2.setImageUrl("removed", imageLoader);
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


    private void showRequestGameDialog(final String uid) {
        try {


            final AlertDialog.Builder dialog = new AlertDialog.Builder(GetOpponentActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.dialog_request_game, null);
            dialog.setView(dialogView);
            dialog.setCancelable(true);

            requestDialog = dialog.create();
            final TextView tvMessage = dialogView.findViewById(R.id.tv_message);
            tv_time_left_request = dialogView.findViewById(R.id.tv_time_left_request);
            TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
            TextView btnok = dialogView.findViewById(R.id.btn_ok);
            TextView btnNo = dialogView.findViewById(R.id.btnNo);
            database.child(Constant.DB_USER).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userNameSender = snapshot.child(Constant.USER_NAME).getValue().toString();
                        tvMessage.setText("Do you play with " + userNameSender + " ?");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            startTimerRequest();

            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isStarting = false;
                    if (countDownTimerRequest != null) {
                        countDownTimerRequest.cancel();
                    }
                    player2UserId = uidSender;
                    Log.d(TAG, "" + player2UserId);
                    database.child(Constant.DB_USER).child(uidSender).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String online_status_game = String.valueOf(snapshot.child("online_status_game").getValue());
                                Log.d(TAG, "onDataChange: online_status_game: " + online_status_game);
                                if (online_status_game.equals("false")) {
                                    isCreatedRoom = true;
                                    gameRequestRef.child(requestKey).child(player).child("request_type").setValue("accept");
                                    database.child(Constant.DB_USER).child(uidSender).child("online_status_game").setValue("onGoing");
                                    SearchPlayerClickMethod2();
                                    requestDialog.dismiss();
                                    return;
                                }

                                if (online_status_game.equals("onGoing")) {
                                    gameRequestRef.child(requestKey).child(player).child("request_type").setValue("reject");
                                    Toast.makeText(mContext, "the player in another game", Toast.LENGTH_SHORT).show();
                                    requestDialog.dismiss();
                                    return;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isStarting = false;
                    if (countDownTimerRequest != null) {
                        countDownTimerRequest.cancel();
                    }
                    gameRequestRef.child(requestKey).child(player).child("request_type").setValue("reject");
                    if (!requestKey.equalsIgnoreCase("")) {
                        gameRequestRef.child(requestKey).removeValue();
                    }
                    requestDialog.dismiss();
                }
            });
            requestDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            requestDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
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
    void callGetRoomFunction(int num) {
        Map<String, Object> registerMap = new HashMap<>();
        registerMap.put(Constant.USER_ID, player);
        registerMap.put(Constant.LANGUAGE_ID, "2");
        registerMap.put(Constant.QUE_ID, numRandom);
        registerMap.put(Constant.USER_ID_1, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        registerMap.put(Constant.USER_ID_2, opponentId);
        registerMap.put("timestamp", ServerValue.TIMESTAMP);
        if (num == 1) {
            registerMap.put("private_key", randomAlphaNumeric(8));
        } else {
            registerMap.put("private_key", privateKey);
        }

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Map> call = apiService.create(registerMap);
        call.enqueue(new retrofit2.Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                try {
                    if (response.body() != null) {
                        Log.e(TAG, "hhh : " + response.body().toString());
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


    void callGetTimeFunction() {
        Log.d(TAG, "callGetTimeFunction: y");
        Map<String, Object> registerMap = new HashMap<>();
        registerMap.put("timestamp", ServerValue.TIMESTAMP);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Map> call = apiService.getTime(registerMap);
        call.enqueue(new retrofit2.Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {

                if (response.body() != null) {
                    Log.d(TAG, "callGetTime: " + response.body().values().toString());
                    String old = response.body().values().toString().replace(".", "");
                    String news = old.substring(1, old.length() - 4);
                    Log.d(TAG, "news: " + news);
                    timeNow = Long.parseLong(news);
                    Log.d(TAG, "onResponse: timeStamp:: " + timeNow);
                } else {
                    Log.d(TAG, "callGetTime: ");
                }

            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                Log.d(TAG, "callGetTime: " + t.getMessage());

            }
        });
    }


    void callGetRequestFunction(String user_id_receiver) {
        Map<String, Object> registerMap = new HashMap<>();
        registerMap.put(Constant.USER_ID, player);
        registerMap.put("user_id_receiver", user_id_receiver);
        registerMap.put("timestamp", ServerValue.TIMESTAMP);
        registerMap.put("request_type", "request_type");
        registerMap.put("online_status", "true");

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Map> call = apiService.createRequest(registerMap);
        call.enqueue(new retrofit2.Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                if (response.body() != null) {
                    Log.d(TAG, response.body().toString());
                    Toast.makeText(GetOpponentActivity.this, "1", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "ww");
                    Toast.makeText(GetOpponentActivity.this, "2", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                Log.e(TAG, "onFailure: 1111111111");
                Toast.makeText(GetOpponentActivity.this, "3", Toast.LENGTH_SHORT).show();
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
    public static String randomAlphaNumericChar(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * Constant.ALPHA_NUMERIC_STRING.length());
            builder.append(Constant.ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    /*
     * A function generate random alpha numeric
     * */
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * Constant.ALPHA_NUMERIC_STRING1.length());
            builder.append(Constant.ALPHA_NUMERIC_STRING1.charAt(character));
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
     * use timer toh get opposite player in specific time
     * */
    private void startTimerRequest() {
        if (countDownTimerRequest != null) {
            countDownTimerRequest.cancel();
        }
        countDownTimerRequest = new CountDownTimer(10000, Constant.COUNT_DOWN_TIMER) {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {
                isStarting = true;
                int progress = (int) (millisUntilFinished / 1000);
                tv_time_left_request.setText("" + String.format(FORMAT, progress));
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                isStarting = false;
                gameRequestRef.child(requestKey).child(player).child("request_type").setValue("ignore");
                tv_time_left_request.setText("00");
                if (!requestKey.equalsIgnoreCase("")) {
                    gameRequestRef.child(requestKey).removeValue();
                }
                requestDialog.dismiss();
            }
        }.start();
    }


    private void getAllFriends() {


        UserViewModel viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        viewModel.init();

        viewModel.getAllUser().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                usersList = users;
                adapter.setList(usersList);

            }
        });
    }

//    private void getAllRequests() {
//
//        RequestViewModel viewModel = ViewModelProviders.of(this).get(RequestViewModel.class);
//        viewModel.init();
//
//        viewModel.getAllRequests().observe(this, new Observer<List<RequestModel>>() {
//            @Override
//            public void onChanged(List<RequestModel> users) {
//               for(RequestModel model:users){
//                   Log.d(TAG, "getAllRequests: requestKey: "+model.getKeyRequest());
//                   Log.d(TAG, "getAllRequests: uidSender: "+model.getUidSender());
//                   Log.d(TAG, "getAllRequests: uidReceiver: "+model.getUidReceiver());
//                   return;
//               }
//            }
//        });
//    }

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
        clearReferences();
        if (countDownTimerRequest != null) {
            countDownTimerRequest.cancel();
        }
        isStarting = false;
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

            }
        }
    }

    private void clearReferences(){
        Activity currActivity = mMyApp.getCurrentActivity();
        if (this.equals(currActivity))
            mMyApp.setCurrentActivity(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMyApp.setCurrentActivity(this);
    }

    //---------------------------------------------------------------------------------------------------

    void createRoom() {
        Query sortUserOrder = gameRequestRef.orderByChild("user_id_sender").equalTo(player);

        ValueEventListener _valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot _data : dataSnapshot.getChildren()) {

                    if (_data.exists()) {
                        Log.d(TAG, "onDataChange: playerName: " + _data.getKey());
                        String playerName = String.valueOf(_data.child("user_id_sender").getValue());
                        if (playerName.equals(player)) {

                            String user_id_receiver = String.valueOf(_data.child("user_id_receiver").getValue());
                            player2UserId = user_id_receiver;
                            String request_type = String.valueOf(_data.child(user_id_receiver).child("request_type").getValue());
                            if (request_type.equals("accept")) {
                                if(listDialog.isShowing()){
                                    listDialog.dismiss();
                                }
                                Log.d(TAG, "onDataChange: playerName: " + playerName);
                                Log.d(TAG, "onDataChange: playerName: " + request_type);
                                Toast.makeText(mContext, "test", Toast.LENGTH_SHORT).show();
                                SearchPlayerClickMethod2();

                                return;
                            }
                        }
                    } else {
                        Toast.makeText(mContext, "test1", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        sortUserOrder.addValueEventListener(_valueEventListener);
    }

    /*
     * A function use to show dialog on screen when ended game
     * */
    private void showWinnerDialog() {
        try {


            if (countDownTimer != null) {
                countDownTimer.cancel();
            }


            final android.app.AlertDialog.Builder dialog1 = new android.app.AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.winner_dialog, null);
            dialog1.setView(dialogView);
            dialog1.setCancelable(false);
            final android.app.AlertDialog alertDialog = dialog1.create();
            TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
            TextView tvMessage = dialogView.findViewById(R.id.tv_message);
            Button btnok = dialogView.findViewById(R.id.btn_ok);
            Button btnReBattle = dialogView.findViewById(R.id.btnReBattle);
            NetworkImageView winnerImg = dialogView.findViewById(R.id.winnerImg);
            String winner="";
            if (winner.equals("you")) {
                tvTitle.setText(getString(R.string.congrats));
//                tvMessage.setText(winnerMessage);
            } else {
                tvTitle.setText(getString(R.string.next_time));
                tvMessage.setText(R.string.next_time);
            }

            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    DatabaseReference databaseReference = myGameRef.child(gameId);
//                    if (databaseReference != null) {
//                        databaseReference.removeValue();
//                    }
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
}