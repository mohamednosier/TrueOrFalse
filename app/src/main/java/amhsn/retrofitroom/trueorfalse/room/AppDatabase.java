package amhsn.retrofitroom.trueorfalse.room;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import amhsn.retrofitroom.trueorfalse.room.dao.QuestionDao;
import amhsn.retrofitroom.trueorfalse.room.entity.Question;

@Database(entities = {Question.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "TrueOrFalDatabase";
    private static AppDatabase mInstance;
    private static SupportSQLiteDatabase db;

    public static AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "getInstance: Creating a new database instance");
//                mInstance = Room.databaseBuilder(
//                        context.getApplicationContext(),
//                        AppDatabase.class,
//                        AppDatabase.DATABASE_NAME)
//                        .build();

                RoomDatabase.Builder roombuilder = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "AppDatabase.DATABASE_NAME");
                roombuilder.addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Log.d("ONCREATE", "Database has been created.");
                        db.execSQL("PRAGMA encoding='UTF-8'");
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                        Log.d("ONOPEN", "Database has been opened.");
                        db.execSQL("PRAGMA encoding='UTF-8'");
                    }
                });

                mInstance = (AppDatabase) roombuilder.build();

//                Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DATABASE_NAME)
//                        .addCallback(new Callback() {
//                            @Override
//                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                                super.onCreate(db);
//                                // add this code
//                                db.execSQL("PRAGMA encoding='UTF-8';");
//                            }
//                        }).allowMainThreadQueries().build();

            }
        }
        Log.d(TAG, "getInstance: Getting the database instance, no need to recreated it.");
        return mInstance;
    }

    public abstract QuestionDao trueOrFalseDao();

    /**
     * Populate Database Section
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            db.execSQL("PRAGMA encoding='UTF-8';");
            new PopulateDbAsync(mInstance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final QuestionDao mQuestionDao;

        PopulateDbAsync(AppDatabase db) {
            mQuestionDao = db.trueOrFalseDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            List<Question> list = new ArrayList<>();
            list.add(new Question("question 7", 0, "question 7"));
            list.add(new Question("question 8", 0, "question 7"));
            list.add(new Question("question 9", 0, "question 7"));
            list.add(new Question("question 10", 0, "question 7"));
            list.add(new Question("question 11", 0, "question 7"));
            mQuestionDao.insert(list);
            return null;
        }
    }
}