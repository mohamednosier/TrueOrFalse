package amhsn.retrofitroom.trueorfalse.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import amhsn.retrofitroom.trueorfalse.room.AppDatabase;
import amhsn.retrofitroom.trueorfalse.room.AppExecutors;
import amhsn.retrofitroom.trueorfalse.room.dao.QuestionDao;
import amhsn.retrofitroom.trueorfalse.room.entity.Question;

public class QuestionRepository {


    private static final Object LOCK = new Object();
    private static QuestionRepository mInstance;
    private final QuestionDao trueOrFalseDao;
    private LiveData<List<Question>> list;


    QuestionRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        trueOrFalseDao = database.trueOrFalseDao();
        list = trueOrFalseDao.getAllQuestions();
    }


    public static QuestionRepository getInstance(Application application) {

        if (mInstance == null) {
            synchronized (LOCK) {
                mInstance = new QuestionRepository(application);
            }
        }

        return mInstance;
    }


    public LiveData<List<Question>> getAllQuestions() {
        return list;
    }


    public LiveData<Question> getQuestionById(final int questionID) {
        return trueOrFalseDao.getQuestionById(questionID);
    }


    public void insert(final List<Question> trueOrFalseEntity) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                trueOrFalseDao.insert(trueOrFalseEntity);
            }
        });
    }


    public void update(final Question trueOrFalseEntity) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                trueOrFalseDao.update(trueOrFalseEntity);
            }
        });
    }


    public void delete(final Question trueOrFalseEntity) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                trueOrFalseDao.delete(trueOrFalseEntity);
            }
        });
    }


    public void deleteAll() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                trueOrFalseDao.deleteAll();
            }
        });
    }




}
