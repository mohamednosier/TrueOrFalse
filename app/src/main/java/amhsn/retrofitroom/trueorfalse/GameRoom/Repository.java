package amhsn.retrofitroom.trueorfalse.GameRoom;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import amhsn.retrofitroom.trueorfalse.Constant;
import amhsn.retrofitroom.trueorfalse.model.User;

public class Repository {

    private static Repository mInstance;
    private List<User> userList = new ArrayList<>();
    private MutableLiveData<List<User>> listMutableLiveData = new MutableLiveData<>();

    public static Repository getInstance() {
        if (mInstance == null) {
            mInstance = new Repository();
        }
        return mInstance;
    }

    public MutableLiveData<List<User>> getUsersOnline() {
        if (userList.size() == 0) {
            loadUsers();
        }

        listMutableLiveData.setValue(userList);

        return listMutableLiveData;
    }

    private void loadUsers() {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        Query sortUserOrder = myRef.child("user").orderByChild(Constant.ONLINE_STATUS).equalTo(true);

        ValueEventListener _valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();
                for (DataSnapshot _data : dataSnapshot.getChildren()) {

                    if (_data.exists() && _data.child(Constant.USER_NAME).getValue() != null) {
                        String playerName = String.valueOf(_data.child(Constant.USER_NAME).getValue());
                        String uid = String.valueOf(_data.getKey());
                        if (!Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(uid)) {
                            userList.add(new User(playerName, uid));
                        }
                    }
                }
                listMutableLiveData.postValue(userList);
                Log.i("TAG", "onDataChange: userList: " + userList.size());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };


        sortUserOrder.addValueEventListener(_valueEventListener);
    }
}
