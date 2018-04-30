package com.example.ehsan.homefood;

import android.*;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private static final String GOOGLE_TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    // ...
    private static final String TAG = "FacebookLogin";

    LoginButton loginButton;
    SignInButton signInButton;
    CallbackManager callbackManager;
    public static FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference reference = firebaseDatabase.getReference();
    private DatabaseReference Childreference = reference.child("ParkingArea").child("Slots");
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);
        initViews();
        setListeners();
        progressDialog = new ProgressDialog(this);

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");

        signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        googleSignIn();

        callbackManager=CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


//        if(Login.isLoggedIn)
//        {
//            Intent intent=new Intent(this,Home_Screen.class);
//            finish();
//            startActivity(intent);
//            // FirebaseAuth.getInstance().signOut();
//        }


    }



    private void googleSignInAction() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }

        // Pass the activity result to the Twitter login button.
        //tLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading Credentails...");
            progressDialog.show();
            DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String getFullName = dataSnapshot.child("userName").getValue().toString();
                    String getEmailId = dataSnapshot.child("email").getValue().toString();
                    String getMobileNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                    String getLocation = dataSnapshot.child("location").getValue().toString();
                    Boolean isChef = (Boolean) dataSnapshot.child("isChef").getValue();
                    User newUser = new User(getEmailId, getFullName, getMobileNumber, getLocation, isChef);
                    User.setUser(newUser);
                    //Toast.makeText(getApplicationContext(), User.getUser().toMap().toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(Login.this, HomeScreen.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(GOOGLE_TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(GOOGLE_TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),"Successfull"+user.getDisplayName().toString(),Toast.LENGTH_SHORT).show();

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(GOOGLE_TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private  void googleSignIn(){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }





    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private static EditText emailid, password;
    private static Button loginBtn;
    private static TextView signUp,forgetPwd;
    private static CheckBox show_hide_password;
    private static ConstraintLayout loginLayout;
    private static Animation shakeAnimation;
    private ProgressDialog progressDialog;

    public void close(View view) {
        onBackPressed();
    }
    // Initiate Views
    private void initViews() {

        emailid = (EditText) findViewById(R.id.login_emailid);
        password = (EditText)findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        signUp = (TextView) findViewById(R.id.createAccount);
        forgetPwd = (TextView) findViewById(R.id.forgetTxt);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);
        loginLayout = (ConstraintLayout) findViewById(R.id.login_layout);
        //new MyApplication().getInstance().trackScreenView("Login Screen");
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {

                    DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                    root.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String getFullName = dataSnapshot.child("userName").getValue().toString();
                            String getEmailId = dataSnapshot.child("email").getValue().toString();
                            String getMobileNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                            String getLocation = dataSnapshot.child("location").getValue().toString();
                            Boolean isChef = (Boolean) dataSnapshot.child("isChef").getValue();
                            User newUser = new User(getEmailId, getFullName, getMobileNumber, getLocation, isChef);
                            User.setUser(newUser);
                            progressDialog.dismiss();
                            Intent intent;
                            if(User.getUser().getChef())
                                 intent= new Intent(Login.this, Chef_Activity.class);
                            else
                                intent=new Intent(Login.this,HomeScreen.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(this,
                R.anim.shake);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            show_hide_password.setTextColor(csl);
            signUp.setTextColor(csl);
        } catch (Exception e) {
        }
    }
    // Set Listeners
    private void setListeners() {
        loginBtn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgetPwd.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:{
                    checkValidation();
            }
            break;
            case R.id.createAccount: {
                finish();
                startActivity(new Intent(this, Signup.class));
            }
            break;
            case R.id.forgetTxt: {
                startActivity(new Intent(this, Forget_Password.class));
            }
            break;
            case R.id.sign_in_button:
                googleSignInAction();
                break;
        }

    }

    // Check Validation before login
    private void checkValidation() {
        // Get email id and password
        String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(this,loginLayout,
                    "Enter both credentials.");

        }
        // Check if email id is valid or not
        else if (!m.find()) {
            loginLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(this,loginLayout,
                    "Your Email Id is Invalid.");
        }
        // Else do login and do your stuff
        else {
            userLogin();
        }
    }

    private void userLogin(){
        String email = emailid.getText().toString().trim();
        String pswd = password.getText().toString().trim();
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, pswd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (!task.isSuccessful()) {
                    // there was an error
                    if (password.length() < 6) {
                        loginLayout.startAnimation(shakeAnimation);
                        new CustomToast().Show_Toast(Login.this,loginLayout,
                                "Password length is less than 8 characters.");
                    } else {
                        loginLayout.startAnimation(shakeAnimation);
                        new CustomToast().Show_Toast(Login.this,loginLayout,
                                "Authentication failed");
                    }
                } else {

                    Toast.makeText(getApplicationContext(),"Login Successfull",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
