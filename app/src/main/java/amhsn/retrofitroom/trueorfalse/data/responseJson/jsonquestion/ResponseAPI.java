package amhsn.retrofitroom.trueorfalse.data.responseJson.jsonquestion;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amhsn.retrofitroom.trueorfalse.room.entity.Question;

public class ResponseAPI {

    @SerializedName("serverres")
    @Expose
    private List<Question> serverres = null;

    public List<Question> getServerres() {
        return serverres;
    }

    public void setServerres(List<Question> serverres) {
        this.serverres = serverres;
    }

}