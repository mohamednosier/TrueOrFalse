package amhsn.retrofitroom.trueorfalse.room.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

import amhsn.retrofitroom.trueorfalse.data.ApiClient;
import amhsn.retrofitroom.trueorfalse.data.responseJson.jsonquestion.ResponseAPI;
import amhsn.retrofitroom.trueorfalse.room.entity.Question;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebServiceRepository {

    private Application application;

    public WebServiceRepository(Application application) {
        this.application = application;
    }


    /*
    * A function return all questions
    * And get data from server and store in room database
    * And get data from room database and display them on the screen
    * */
    public LiveData<List<Question>> getAllQuestions() {

        final MutableLiveData<List<Question>> data = new MutableLiveData<>();

        String response = "";
        try {
            Call<ResponseAPI> call = ApiClient
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

//                    QuestionRepository postRoomDBRepository = new QuestionRepository(application);
//                    postRoomDBRepository.insert(questionList);

                    data.setValue(questionList);
                }

                @Override
                public void onFailure(Call<ResponseAPI> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        //  return retrofit.create(ResultModel.class);
        return data;

    }



}
