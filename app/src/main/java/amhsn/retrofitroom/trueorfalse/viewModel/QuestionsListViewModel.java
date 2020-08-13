package amhsn.retrofitroom.trueorfalse.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import amhsn.retrofitroom.trueorfalse.room.entity.Question;
import amhsn.retrofitroom.trueorfalse.room.repository.QuestionRepository;
import amhsn.retrofitroom.trueorfalse.room.repository.WebServiceRepository;

public class QuestionsListViewModel extends AndroidViewModel {

    private QuestionRepository mRepository;
    private LiveData<List<Question>> mAllLists;
    private LiveData<List<Question>> mRetroObservable;
    private WebServiceRepository webServiceRepository ;


    public QuestionsListViewModel(@NonNull Application application) {
        super(application);
        mRepository = QuestionRepository.getInstance(application);
        webServiceRepository = new WebServiceRepository(application);
        mRetroObservable = webServiceRepository.getAllQuestions();
//        mRetroObservable = mRepository.getAllQuestions();
    }


    public LiveData<List<Question>> getAllQuestions() {
        return mRetroObservable;
    }

//
//    public void getQuestionById(int questionId) {
//        mRepository.getQuestionById(questionId);
//    }
//
//
//    public void insert(List<Question> questions) {
//        mRepository.insert(questions);
//    }
//
//
//    public void update(Question questions) {
//        mRepository.update(questions);
//    }
//
//
//    public void delete(Question questions) {
//        mRepository.delete(questions);
//    }
//
//
//    public void deleteAll() {
//        mRepository.deleteAll();
//    }

}
