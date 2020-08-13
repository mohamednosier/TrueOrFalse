package amhsn.retrofitroom.trueorfalse.login;

import android.annotation.SuppressLint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

import amhsn.retrofitroom.trueorfalse.R;
import amhsn.retrofitroom.trueorfalse.SplashActivity;
import amhsn.retrofitroom.trueorfalse.helper.Session;
import amhsn.retrofitroom.trueorfalse.helper.Utils;
import amhsn.retrofitroom.trueorfalse.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginActivity extends AppCompatActivity {

    private final static String TAG = "LoginActivity";

    // vars
    private int RC_SIGN_IN = 9001;
    private CallbackManager mCallbackManager;
    private String token;

    // vars firebase
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    // widgets
    private TextView tvPrivacy;
    private ProgressDialog mProgressDialog;
    private TextInputEditText edtEmail, edtPassword;
    private TextInputLayout inputEmail, inputPass;


    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: OnStarted.");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        // initialize vars firebase
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // initialize reference to views
        tvPrivacy = findViewById(R.id.tvPrivacy);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        inputEmail = findViewById(R.id.inputEmail);
        inputPass = findViewById(R.id.inputPass);


        // check is there internet or not
        if (!Utils.isNetworkAvailable(LoginActivity.this)) {
            Log.d(TAG, "onCreate: isNetworkAvailable: No internet connection!");
            setSnackBar(getString(R.string.msg_no_internet), getString(R.string.retry));
        }

        token = Session.getDeviceToken(getApplicationContext());
        if (token == null) {
            token = "token";
        }


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

        Utils.GetSystemConfig(getApplicationContext());
    }


    /*
     * A function login with facebook
     * */
    public void LoginWithFacebook(View view) {

        if (!Utils.isNetworkAvailable(LoginActivity.this)) {
            setSnackBar(getString(R.string.msg_no_internet), getString(R.string.retry));
            return;
        }

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        setResult(RESULT_OK);
                        Log.d(TAG, "LoginWithFacebook: onSuccess");
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "LoginWithFacebook: onCancel");
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e(TAG, "LoginWithFacebook: onError: ", error);
                        error.printStackTrace();
                    }
                });

    }


    /*
     * A function login anonymously
     * */
    public void PlayAsGuest(View view) {

        if (!Utils.isNetworkAvailable(LoginActivity.this)) {
            setSnackBar(getString(R.string.msg_no_internet), getString(R.string.retry));
            return;
        }

        showProgressDialog();
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Log.e(TAG, "PlayAsGuest: signInAnonymously: Failed");
                    hideProgressDialog();
                    return;
                }

                Log.d(TAG, "PlayAsGuest: signInAnonymously: Successful");
                UserSignUpWithSocialMedia("", "", "Guest", "Guest@Guest.com", "", "fb");
            }
        });

    }


    /*
     * A function use to signUp with SocialMedia
     * and create table user in firebase database realtime
     * and save data inside table user
     * */
    public void UserSignUpWithSocialMedia(final String fCode, final String referCode, final String name, final String email, final String profile, final String type) {

        User user = new User(name, email, 0, false, profile, 0, token, "user_id");


        FirebaseDatabase.getInstance().getReference("user").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task1) {

                if (!task1.isSuccessful()) {
                    Log.e(TAG, "UserSignUpWithSocialMedia: Not Successful");
                    hideProgressDialog();
                    return;
                }

                Log.d(TAG, "UserSignUpWithSocialMedia: Successful");
                hideProgressDialog();
                Intent i = new Intent(getApplicationContext(), SplashActivity.class);
                i.putExtra("type", "default");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

    }


    /*
     * A function get data from your facebook when logged with facebook
     * */
    private void getFbInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            Log.d("LOG_TAG", "fb json object: " + object);
                            Log.d("LOG_TAG", "fb graph response: " + response);

                            String id = object.getString("id");
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String gender = object.getString("gender");
                            String birthday = object.getString("birthday");
                            String image_url = "http://graph.facebook.com/" + id + "/picture?type=large";
                            Log.d("LOG_TAG", "fb json object: " + image_url);

                            String email;
                            if (object.has("email")) {
                                email = object.getString("email");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /*
     * A function handle facebook token
     * */
    private void handleFacebookAccessToken(AccessToken token) {
        showProgressDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "handleFacebookAccessToken: Successful");
                                FirebaseUser user = mAuth.getCurrentUser();
                                UserSignUpWithSocialMedia("", Objects.requireNonNull(user).getEmail(), user.getDisplayName(), user.getEmail(), Objects.requireNonNull(user.getPhotoUrl()).toString(), "fb");
                                hideProgressDialog();

                            } else {
                                Log.e(TAG, "handleFacebookAccessToken: Not Successful");
                                // If sign in fails, display a message to the user.
                                FirebaseDatabase.getInstance().getReference("user").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("online_status").setValue(false);
                                LoginManager.getInstance().logOut();
                                hideProgressDialog();
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthInvalidUserException | FirebaseAuthUserCollisionException invalidEmail) {

                                    setSnackBar(invalidEmail.getMessage(), getString(R.string.ok));
                                } catch (Exception e) {
                                    e.printStackTrace();

                                    setSnackBar(e.getMessage(), getString(R.string.ok));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    /*
     * A function move user from login to signUp
     * */
    public void SignUpWithEmail(View view) {
        Log.d(TAG, "SignUpWithEmail: Navigating to SignUpActivity");
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }


    /*
     * A function use to show dialog
     * */
    public void showProgressDialog() {
        Log.d(TAG, "showProgressDialog: OnStarted.");
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }


    /*
     * A function use to close dialog
     * */
    public void hideProgressDialog() {
        Log.d(TAG, "hideProgressDialog: OnClosed.");
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    /*
     * A function check values email and password not equal null or empty
     * */
    private boolean validateForm() {
        boolean valid = true;

        String email = Objects.requireNonNull(edtEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(edtPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.email_alert_1));
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            inputEmail.setError(getString(R.string.email_alert_2));
        } else {
            inputEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            inputPass.setError(getString(R.string.pass_alert));
            valid = false;
        } else {
            inputPass.setError(null);
        }


        return valid;
    }


    /*
     * A function use to login with email
     * */
    public void LoginWithEmail(View view) {

        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(Objects.requireNonNull(edtEmail.getText()).toString(), Objects.requireNonNull(edtPassword.getText()).toString())
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Log.d(TAG, "LoginWithEmail: Successful");
                            startActivity(new Intent(getBaseContext(), SplashActivity.class));
                            finish();

                        } else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                inputEmail.setError(getString(R.string.signup_alert));
                            } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                inputPass.setError(getString(R.string.invalid_pass));
                                // TODO: Take your action
                            } catch (Exception e) {
                                Log.e(TAG, "onComplete last: " + e.getMessage());
                            }
                        }
                        hideProgressDialog();
                    }
                });
    }


    /*
     * A function use to create custom snackBar
     * */
    public void setSnackBar(String message, String action) {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(LoginActivity.this)) {
                    snackbar.dismiss();
                } else {
                    snackbar.show();
                }
            }
        });
        View snackBarView = snackbar.getView();
        TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from Facebook SignIn API;
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    /*
     * when stop activity stop dialog
     * */
    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}