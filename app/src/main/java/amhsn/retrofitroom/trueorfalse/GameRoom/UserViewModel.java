package amhsn.retrofitroom.trueorfalse.GameRoom;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import amhsn.retrofitroom.trueorfalse.model.User;

 public class UserViewModel extends ViewModel {

    public MutableLiveData<List<User>> users;

     public  void init(){

        if(users != null){
            return;
        }

        users = Repository.getInstance().getUsersOnline();
    }

     public LiveData<List<User>> getAllUser(){
        return users;
    }


}
