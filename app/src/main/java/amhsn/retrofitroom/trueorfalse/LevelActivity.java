package amhsn.retrofitroom.trueorfalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import amhsn.retrofitroom.trueorfalse.room.entity.Question;
import amhsn.retrofitroom.trueorfalse.utils.InternetConnectivity;
import amhsn.retrofitroom.trueorfalse.viewModel.QuestionsListViewModel;

public class LevelActivity extends FragmentActivity {

    GridView gvLevels;

    SharedPreferences pref;

    Typeface tfQuestion, tfFromanBold;

    ArrayList<Integer> numberOfLevels = new ArrayList<>();
    static final int numberQuestionForLevel = 10;

    ProgressBar progressBar;
    ProgressDialog dialog;

    // ViewModel
    private QuestionsListViewModel retroViewModel;
    private List<Question> questionList = new ArrayList<>();
    private Question question;
    private int qCounter = 0;
    private int qid = 0;

    // vars Timer
    private final int REQUEST_INTERVAL = 1;
    private Timer requestIntervalTimer;
    private boolean running;

    // Adapter
    private GridAdapter adapter;


    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
            Intent intent = new Intent(getApplicationContext(),
                    SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Try Again",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {


            setContentView(R.layout.activity_level);

            tfQuestion = Typeface.createFromAsset(
                    LevelActivity.this.getAssets(), "HOBOSTD.OTF");

            tfFromanBold = Typeface.create(tfQuestion, Typeface.BOLD);

            TextView lblLevel = findViewById(R.id.lblLevel);
            lblLevel.setTypeface(tfFromanBold);

            pref = getApplicationContext().getSharedPreferences("prefname",
                    MODE_PRIVATE);

            gvLevels = findViewById(R.id.gvLevels);

            GridAdapter adapter = new GridAdapter(getApplicationContext());
            gvLevels.setAdapter(adapter);

            gvLevels.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {

                    if (GlobalVar.isSound) {
                        ClassSound.PlaySound(getApplicationContext());
                    }

                    int t = (arg2) * GlobalVar.QUESTION_FOR_SINGLE_LEVEL;

                    GlobalVar.level = arg2 + 1;

                    GlobalVar.aryLevel1 = null;

                    GlobalVar.aryLevel1 = new Integer[GlobalVar.QUESTION_FOR_SINGLE_LEVEL];

                    for (int j = 0; j < GlobalVar.aryLevel1.length; j++) {
                        GlobalVar.aryLevel1[j] = j + t;
                    }

                    Collections.shuffle(Arrays.asList(GlobalVar.aryLevel1));

                    GlobalVar.k = 0;
                    GlobalVar.playerScore = 0;
                    GlobalVar.playerLife = 0;
                    GlobalVar.skipCounter = 0;

                    // finish();
                    Intent intent = new Intent(LevelActivity.this,
                            SinglePlayerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);

                    overridePendingTransition(R.anim.enter, R.anim.exit);

                    /*
                     * Toast.makeText(getApplicationContext(),
                     * Integer.toString(GlobalVar.aryLevel1[0]),
                     * Toast.LENGTH_SHORT).show();
                     */
                }

            });



        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Try Again",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public class GridAdapter extends BaseAdapter {

        private Context context;
        private List<Question> list = new ArrayList<>();

        // private final Integer[] LevelVals;

        GridAdapter(Context c) {
            context = c;
            if (InternetConnectivity.isConnectedToWifiNetwork(getApplicationContext())) {
                timerStart();
            }
            // this.LevelVals = mobileValues;
        }

        @Override
        public int getCount() {
            return numberOfLevels.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /*
         * @Override public boolean isEnabled(int position) {
         *
         * if (isLocked.get(position) == 0) return true;
         *
         * return false;
         *
         * }
         */

        @Override
        public boolean isEnabled(int position) {

            return position < (pref.getInt("clearedLevels", 0) + 1);

        }

        @SuppressLint({"SetTextI18n", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // txtLevel.setText("Level:" + pref.getInt("clearedLevels", 1));

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            if (convertView == null) {

                gridView = new View(context);

                assert inflater != null;
                gridView = inflater.inflate(R.layout.layout_gridview, null);


//                // Move this initialization to constructor so that its not initalized again and again.
                Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.move_left);
//
//                // By default all grid items will animate together and will look like the gridview is
                // animating as a whole. So, experiment with incremental delays as below to get a
                // wave effect.
                anim.setStartOffset(position * 500);

                gridView.setAnimation(anim);
                anim.start();
//                int resId = R.anim.layout_animation_fall_down;
//                GridLayoutAnimationController animation = AnimationUtils.loadAnimation(getBaseContext(), resId);
//                gridView.setAnimation(animation);

//                Animation a = new FadeUpAnimation(gridView);
//                a.setInterpolator(new AccelerateInterpolator());
//                a.setDuration(300);
//                gridView.setAnimation(a);
//                gridView.startAnimation(a);

            } else {
                gridView = convertView;
            }

            TextView txtLevel = gridView.findViewById(R.id.txtLevel);
            txtLevel.setTypeface(tfFromanBold);
            TextView lblLevel = gridView.findViewById(R.id.lblLevel);
            lblLevel.setText("Level");
            lblLevel.setTypeface(tfQuestion);

            ImageView imglock = gridView.findViewById(R.id.imglock);

            LinearLayout llLevelBg = gridView.findViewById(R.id.llLevelBg);

            txtLevel.setTextColor(Color.BLACK);

            if (position < (pref.getInt("clearedLevels", 0) + 1)) {
                txtLevel.setVisibility(View.VISIBLE);
                txtLevel.setText(String.valueOf(position + 1));
                imglock.setVisibility(View.INVISIBLE);
                llLevelBg.setVisibility(View.VISIBLE);
            } else {
                imglock.setVisibility(View.VISIBLE);
                txtLevel.setVisibility(View.INVISIBLE);
                llLevelBg.setVisibility(View.INVISIBLE);
            }


            return gridView;
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
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchQuestions();
    }


    public void timerStart() {
        running = true;
        if (requestIntervalTimer == null) {
            requestIntervalTimer = new Timer();
            requestIntervalTimer.schedule(new TimerTask() {
                public void run() {
                    runOnUiThread(TimerRunnable);
                }
            }, 0, REQUEST_INTERVAL);
        }
    }

    Runnable TimerRunnable = new Runnable() {
        public void run() {

            qCounter = questionList.size();

            int a = qCounter / numberQuestionForLevel;

            Log.d("onStart", "a: " + a);

            // --- Level
            if (numberOfLevels.isEmpty()) {
                for (int j = 0; j < a; j++) {
                    numberOfLevels.add(j);
                }
            }
            // --- Level
            gvLevels.invalidateViews();
            gvLevels.refreshDrawableState();


            Log.d("oaanStart", "a: " + a);

            Log.d("onStart", "numberOfLevels: " + numberOfLevels.size());

            gvLevels.invalidateViews();
            gvLevels.refreshDrawableState();

        }
    };

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
