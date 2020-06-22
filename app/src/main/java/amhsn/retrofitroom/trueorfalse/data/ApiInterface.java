package amhsn.retrofitroom.trueorfalse.data;

import amhsn.retrofitroom.trueorfalse.data.responseJson.jsonquestion.ResponseAPI;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("get.php")
    Call<ResponseAPI> getAllPosts();


}