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
                mInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                        .addCallback(sRoomDatabaseCallback)
                        .build();

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

            list.add(new Question(1, "يتمتع الثعبان بحاسة سمع قوية.", 0, "الثعبان لا يمتلك أذن خارجية."));
            list.add(new Question(2, "هل صحيح انه لا يوجد فعل امر من حرف واحد فقط.", 0, "يوجد فعل امر من حرف واحد."));
            list.add(new Question(3, "باريس هى عاصمة فرنسا.", 0, ""));
            list.add(new Question(4, "المريخ هو الكوكب الأحمر.", 1, ""));
            list.add(new Question(5, "يموت الحصان إذا قطع ذيله.", 1, ""));
            list.add(new Question(6, "تنخفض درجة حرارة الأرض كلما زاد العمق.", 0, "ترتفع درجة حرارة الأرض كلما زاد العمق."));
            list.add(new Question(7, "عاصمة افريفيا الجنوبية هى بانغى.", 0, "عاصمة افريفيا الجنوبية هى بريتوريا."));
            list.add(new Question(8, "كوالالمبور هى عاصمة ماليزيا.", 1, ""));
            list.add(new Question(9, "القلب هو أكبر عضو في جسم الإنسان.", 0, "الكبد هو أكبر عضو في جسم الإنسان."));
            list.add(new Question(10, "الأرجنتين عاصمتها باكو.", 0, ""));
            list.add(new Question(11, "اطول نهر فى العالم هو نهر الكونغو.", 0, "اطول نهر فى العالم هو نهر النيل."));
            list.add(new Question(12, "ايسلندا عاصمتها ريكيافيك.", 1, ""));
            list.add(new Question(13, "موريتانيا عاصمتها نواكشوط.", 1, ""));
            list.add(new Question(14, "أول من بنى السجون في الإسلام هو إسماعيل عليه السلام.", 0, "أول من بنى السجون في الإسلام هو علي بن أبي طالب."));
            list.add(new Question(15, "أول من ركب الخيل هو  صلاح الدين الأيوبي.", 0, "أول من ركب الخيل هو إسماعيل عليه السلام."));
            list.add(new Question(16, "أول من سمى القرآن بالمصحف هو سعد بن أبي وقاص.", 0, "أول من سمى القرآن بالمصحف هو أبو بكر الصديق."));
            list.add(new Question(17, "أول أولاد آدم عليه السلام هو قابيل.", 1, ""));
            list.add(new Question(18, "اسم الجليد الذي لا ينصهر و إنما يتبخر الجليد الحجرى.", 0, "اسم الجليد الذي لا ينصهر و إنما يتبخر الجليد الجاف."));
            list.add(new Question(19, "يوجد نوع من أنواع الأسماك يمكنه ابتلاع إنسان بكاملة و هو سمك القرش.", 0, "يوجد نوع من أنواع الأسماك يمكنه ابتلاع إنسان بكاملة و هو السمك الصدفي ."));
            list.add(new Question(20, "أقوى تركيب حي إذا ما قورنت بوزنها و قياسها هو ريش الطائر.", 1, ""));

            mQuestionDao.insert(list);
            return null;
        }
    }
}