package amhsn.retrofitroom.trueorfalse.helper;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @Headers("Content-Type: text/plain")
    @POST("create")
    Call<Map> create(@Body Map map);

    @Headers("Content-Type: text/plain")
    @POST("createRequest")
    Call<Map> createRequest(@Body Map map);


    @Headers("Content-Type: text/plain")
    @POST("getTime")
    Call<Map> getTime(@Body Map map);
}
