package amhsn.retrofitroom.trueorfalse.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Objects;

import amhsn.retrofitroom.trueorfalse.R;
import amhsn.retrofitroom.trueorfalse.helper.Session;

public class SignUpActivity extends AppCompatActivity {

    // vars
    private final static String TAG = "SignUpActivity";
    private String currentUserID;
    private String token;

    // widgets
    public TextInputEditText edtName, edtEmail, edtPassword, edtRefer;
    public TextInputLayout inputName, inputEmail, inputPass;
    public ProgressDialog mProgressDialog;

    // vars firebase
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: onStarted.");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        // initialize vars
        mAuth = FirebaseAuth.getInstance();
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtRefer = findViewById(R.id.edtRefer);
        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPass = findViewById(R.id.inputPass);

        token = Session.getDeviceToken(getApplicationContext());
        if (token == null) {
            token = "token";
        }

        edtName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, 0, 0);
        edtEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email, 0, 0, 0);
        edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.ic_show, 0);
        edtPassword.setTag("show");
        edtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;


                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edtPassword.getRight() - edtPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (edtPassword.getTag().equals("show")) {
                            edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.ic_hide, 0);
                            edtPassword.setTransformationMethod(null);
                            edtPassword.setTag("hide");
                        } else {
                            edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.ic_show, 0);
                            edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                            edtPassword.setTag("show");
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }


    /*
     * A function use to register with email and password
     * and store data in firebase
     * */
    public void SignUpWithEmail(View view) {

        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        final String email = Objects.requireNonNull(edtEmail.getText()).toString();
        final String password = Objects.requireNonNull(edtPassword.getText()).toString();
        final String name = Objects.requireNonNull(edtName.getText()).toString();


        final HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("fcm_id", token);
        userMap.put("email", email);
        userMap.put("password", password);
        userMap.put("num_of_loss",0);
        userMap.put("online_status",false);
        userMap.put("profile_Pic","");
        userMap.put("num_of_wins","0");
        userMap.put("user_id","user_id");
        userMap.put("roomId","");

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Log.e(TAG, "SignUpWithEmail: Not Successful.");
                    hideProgressDialog();
                    return;
                }

                FirebaseDatabase.getInstance().getReference("user").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(userMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    hideProgressDialog();
                                    Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                    Log.e(TAG, "SignUpWithEmail:  Successful.");
                                } else {
                                    hideProgressDialog();
                                    Log.e(TAG, "SignUpWithEmail:  OnFailed.");
                                }

                            }
                        });

            }
        });

    }


    /*
    * A function check value email and password empty or not
    * */
    private boolean validateForm() {
        boolean valid = true;

        String name = Objects.requireNonNull(edtName.getText()).toString().trim();
        String email = Objects.requireNonNull(edtEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(edtPassword.getText()).toString().trim();

        //String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(name)) {
            inputName.setError("Required.");
            valid = false;
        } else {
            inputName.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.email_alert_1));
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            inputEmail.setError(getString(R.string.email_alert_1));
        } else {
            inputEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            inputPass.setError("Required.");
            valid = false;
        } else if (password.length() < 6) {
            inputPass.setError(getString(R.string.password_valid));
            valid = false;
        } else {
            inputPass.setError(null);
        }


        return valid;
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }


    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
