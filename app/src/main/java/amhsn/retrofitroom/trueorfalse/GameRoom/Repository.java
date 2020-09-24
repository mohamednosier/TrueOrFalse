package amhsn.retrofitroom.trueorfalse.GameRoom;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import amhsn.retrofitroom.trueorfalse.Constant;
import amhsn.retrofitroom.trueorfalse.helper.ApiClient;
import amhsn.retrofitroom.trueorfalse.helper.ApiInterface;
import amhsn.retrofitroom.trueorfalse.model.RequestModel;
import amhsn.retrofitroom.trueorfalse.model.User;
import retrofit2.Call;
import retrofit2.Response;

public class Repository {
    private final static String TAG = "Repository";

    private static Repository mInstance;
    private List<User> userList = new ArrayList<>();
    private List<RequestModel> requestList = new ArrayList<>();
    private MutableLiveData<List<User>> userListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<RequestModel>> requestListMutableLiveData = new MutableLiveData<>();
    private long timeNow;

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

        userListMutableLiveData.setValue(userList);

        return userListMutableLiveData;
    }

    public MutableLiveData<List<RequestModel>> getRequestsList() {
        if (requestList.size() == 0) {
            showRequest();
        }

        requestListMutableLiveData.setValue(requestList);
        callGetTimeFunction();
        return requestListMutableLiveData;
    }

    private void loadUsers() {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        Query sortUserOrder = myRef.child("user").orderByChild(Constant.ONLINE_STATUS).equalTo(true);

        ValueEventListener _valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot _data : dataSnapshot.getChildren()) {

                    if (_data.exists() && _data.child(Constant.USER_NAME).getValue() != null) {
                        String playerName = String.valueOf(_data.child(Constant.USER_NAME).getValue());
                        String uid = String.valueOf(_data.getKey());
                        if (!Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(uid)) {
                            userList.add(new User(playerName, uid));
                        }
                    }
                }
                userListMutableLiveData.postValue(userList);
                Log.i("TAG", "onDataChange: userList: " + userList.size());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        sortUserOrder.addValueEventListener(_valueEventListener);
    }

    private void callGetTimeFunction() {
        Log.d(TAG, "callGetTimeFunction: y");
        Map<String, Object> registerMap = new HashMap<>();
        registerMap.put("timestamp", ServerValue.TIMESTAMP);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Map> call = apiService.getTime(registerMap);
        call.enqueue(new retrofit2.Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {

                if (response.body() != null) {
                    Log.d(TAG, "callGetTime: " + response.body().values().toString());
                    String old = response.body().values().toString().replace(".", "");
                    String news = old.substring(1, old.length() - 4);
                    Log.d(TAG, "news: " + news);
                    timeNow = Long.parseLong(news);
                    Log.d(TAG, "onResponse: timeStamp:: " + timeNow);
                } else {
                    Log.d(TAG, "callGetTime: ");
                }

            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                Log.d(TAG, "callGetTime: " + t.getMessage());

            }
        });
    }

    private void showRequest() {
        final String player = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        final DatabaseReference gameRequestRef = FirebaseDatabase.getInstance().getReference("game_request");
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        gameRequestRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()) {
                    callGetTimeFunction();
                    String key = String.valueOf(dataSnapshot.getKey());
                    Log.d(TAG, "onChildAdded: " + key);
                    long timestamp = (long) dataSnapshot.child("timestamp").getValue();
                    String uidSender = (String) dataSnapshot.child("user_id_sender").getValue();
                    String uidReceiver = (String) dataSnapshot.child("user_id_receiver").getValue();
                    long timeIn = timeNow - timestamp;
                    Log.d(TAG, "onChildAdded: timeNow:" + timeNow);

                    if (timeIn < 10 * 1000) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (!snapshot.getKey().equalsIgnoreCase("timestamp")) {
                                if (!snapshot.getKey().equalsIgnoreCase("user_id_sender")) {
                                    if (snapshot.getKey().equalsIgnoreCase("user_id_receiver")) {
                                        if (snapshot.getValue().equals(player)) {
                                            if (requestList.size() < 3) {
                                                String requestKey = key;
                                                requestList.add(new RequestModel(requestKey, uidSender, uidReceiver));
                                            } else {
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        requestListMutableLiveData.postValue(requestList);
                    }
                    Log.d(TAG, "onChildAdded: =============================");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: ");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: ");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved: ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
//        }, 2000);
//    }
}
