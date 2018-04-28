package com.example.ehsan.homefood;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.callback.Callback;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initViews();
        setListeners();
    }
    
    private static EditText fullName, emailId, mobileNumber,code, location,
            password, confirmPassword;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    private LinearLayout view;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private RadioGroup radioGroup;
    private Boolean isChef;
    Animation shakeAnimation;
    // Initialize all views
    private void initViews() {
        fullName = (EditText) findViewById(R.id.fullName);
        emailId = (EditText) findViewById(R.id.userEmailId);
        mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        location = (EditText) findViewById(R.id.location);
        password = (EditText) findViewById(R.id.password);
        code = (EditText) findViewById(R.id.code);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        signUpButton = (Button) findViewById(R.id.signUpBtn);
        login = (TextView) findViewById(R.id.already_user);
        terms_conditions = (CheckBox) findViewById(R.id.terms_conditions);
        radioGroup=(RadioGroup)findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id=radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton=(RadioButton)findViewById(id);
                if(radioButton.getText().equals("Chef"))
                    isChef=true;
                else
                    isChef=false;
            }
        });
        send=(TextView)findViewById(R.id.send_code);
        verify=(TextView)findViewById(R.id.verify_code);

        view=(LinearLayout)findViewById(R.id.sign_up) ;
        shakeAnimation = AnimationUtils.loadAnimation(this,
                R.anim.shake);
        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String getFullName = fullName.getText().toString();
                String getEmailId = emailId.getText().toString();
                String getMobileNumber = mobileNumber.getText().toString();
                String getLocation = location.getText().toString();
                User newUser=new User(getEmailId,getFullName,getMobileNumber,getLocation,isChef);
                User.setUser(newUser);
                if (user != null) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("users").child(user.getUid()).setValue(newUser.toMap());
                    Toast.makeText(getApplicationContext(),"User Added Successfully",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
        send.setOnClickListener(this);
        verify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:
                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user: {

                finish();
                startActivity(new Intent(this,Login.class));

            }
            break;
            case R.id.send_code:
                if (!validatePhoneNumberAndCode()) {
                    return;
                }
                progressDialog.setMessage("Verifying...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                startPhoneNumberVerification(mobileNumber.getText().toString());
                break;
            case  R.id.verify_code:
                if (!validateSMSCode()) {
                    return;
                }

                verifyPhoneNumberWithCode(verificationid, code.getText().toString());
                break;
        }

    }
    private void registerUser(){
        String email = emailId.getText().toString().trim();
        String pswd = password.getText().toString().trim();
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

    if(Verified) {
        firebaseAuth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {

                    Toast.makeText(Signup.this, "User registered", Toast.LENGTH_SHORT).show();

                    finish();
                    startActivity(new Intent(Signup.this, HomeScreen.class));
                } else {
                    Toast.makeText(Signup.this, "Cannot register user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }else {
        code.setAnimation(shakeAnimation);
        code.setError("Verify Code First");
        new CustomToast().Show_Toast(Signup.this, view, "Verify Code First");
    }
    }

    private boolean Verified = false;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    TextView send,verify;

    private String verificationid;
    @Override
    protected void onStart() {
        super.onStart();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                progressDialog.dismiss();
                code.setText(phoneAuthCredential.getSmsCode());
                code.setEnabled(false);
                verify.setText("Verified");
                verify.setEnabled(false);
                Verified=true;
               // signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialog.dismiss();

                Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                verificationid = s;
            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks


    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "signInWithCredential:success"+credential.getSmsCode(),Toast.LENGTH_SHORT).show();

                            FirebaseUser user = task.getResult().getUser();
                            Verified=true;
                            verify.setText("verified");
                            verify.setEnabled(false);
                            //startActivity(new Intent(getApplicationContext(), HomeScreen.class));

                        } else {
                            // Sign in failed, display a message and update the UI
                            new CustomToast().Show_Toast(Signup.this ,view,"signInWithCredential:failure"+ task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                                code.setError("Invalid code.");

                            }

                        }
                    }
                });
        new MainActivity().signOut();
    }

    private boolean validatePhoneNumberAndCode() {
        String phoneNumber = mobileNumber.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mobileNumber.setError("Invalid phone number.");
            return false;
        }


        return true;
    }

    private boolean validateSMSCode(){
        String code1 = code.getText().toString();
        if (TextUtils.isEmpty(code1)) {
            code.setError("Enter verification Code.");
            return false;
        }

        return true;
    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getLocation = location.getText().toString();
        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0)

            new CustomToast().Show_Toast(this, view,
                    "All fields are required.");

            // Check if email id valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(this, view,
                    "Your Email Id is Invalid.");

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            new CustomToast().Show_Toast(this, view,
                    "Both password doesn't match.");

            // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked())
            new CustomToast().Show_Toast(this, view,
                    "Please select Terms and Conditions.");

            // Else do signup or do your stuff
        else{
            registerUser();
            Toast.makeText(this, "Do SignUp.", Toast.LENGTH_SHORT)
                    .show();
        }

    }
}
