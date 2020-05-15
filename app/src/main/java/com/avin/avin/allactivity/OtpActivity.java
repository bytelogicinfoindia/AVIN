package com.avin.avin.allactivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avin.avin.R;
import com.avin.avin.otherclass.BaseActivity;
import com.avin.avin.server.ApiServer;
import com.avin.avin.server.SessionManager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.TimeUnit;

import custom.RainbowTextView;

public class OtpActivity extends BaseActivity {


    TextInputLayout et_hintsotp;
    EditText et_otpnumber;
    RainbowTextView bt_otpVerify;

    private FirebaseAuth mAuth;
    private String mVerificationId;
    TextView tv_number;
    ProgressDialog dialog;
    SessionManager session;
String ImagePath,Name,Phone,Address,Zip_code,Email,Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth =FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_otp);

        TextView textView = new TextView(this);
        textView.setText("Otp Verify ");
        textView.setGravity(Gravity.CENTER);
        getSupportActionBar().setCustomView(textView);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView.setTextSize(20);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.colorWhite));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);

        et_hintsotp = (TextInputLayout) findViewById(R.id.et_hintsotp);
        et_otpnumber = (EditText) findViewById(R.id.et_otpnumber);
        tv_number=(TextView) findViewById(R.id.tv_number);
        session = new SessionManager(getApplicationContext());
        ImagePath=getIntent().getStringExtra("ImagePath");
        Name=getIntent().getStringExtra("Name");
        Email=getIntent().getStringExtra("Email");
        Phone=getIntent().getStringExtra("Phone");
        Address=getIntent().getStringExtra("Address");
        Zip_code=getIntent().getStringExtra("Zip_code");
        Password=getIntent().getStringExtra("Password");
        tv_number.setText("Please type the verification code send to  +91"+Phone);
        sendVerificationCode(Phone);

        bt_otpVerify = (RainbowTextView) findViewById(R.id.bt_otpVerify);

        bt_otpVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitForm();

            }

        });


        et_otpnumber.addTextChangedListener(new MyTextWatcher(et_otpnumber));
    }

    private void submitForm() {

        if (!validateNumber()) {
            return ;
        }else {

            String otpnumber=et_otpnumber.getText().toString();

            verifyVerificationCode(otpnumber);
         }


    }

    private void OtpVerifyNumber() {
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.et_otpnumber:
                    validateNumber();
                    break;

            }
        }
    }

    private boolean validateNumber() {

        String zip_code = et_otpnumber.getText().toString().trim();

        if (zip_code.isEmpty() || !isValidZipCode(zip_code)) {
            et_hintsotp.setError("Please enter otp");
            requestFocus(et_otpnumber);
            return false;
        } else {
            et_hintsotp.setErrorEnabled(false);
        }

        return true;
    }

    private boolean isValidZipCode(String pass) {
        boolean check = false;
        //if(!Pattern.matches("[a-zA-Z]+", phone)) {
        if (pass.length() < 3 || pass.length() > 8) {
            // if(phone.length() != 10) {
            check = false;
            et_otpnumber.setError("Enter valid code");
        } else {
            check = true;
        }
        // } else {
        // check=false;
        // }
        return check;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



    private void sendVerificationCode(String mobile) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

          /*  pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                et_otpnumber.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        dialog = new ProgressDialog(OtpActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        try {
                            dialog.dismiss();
                        }catch (NullPointerException e){
                            e.fillInStackTrace();
                        }
                        if (task.isSuccessful()) {

                            Toast.makeText(OtpActivity.this, "Number Verify", Toast.LENGTH_LONG).show();
                            RegisterMethod();

                        } else {

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                                Toast.makeText(OtpActivity.this, message.toString(), Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                });
    }


    private void RegisterMethod() {
        dialog = new ProgressDialog(OtpActivity.this);
        dialog.setMessage("Please wait.");
        dialog.setCancelable(false);
        dialog.show();
        File file = new File(ImagePath);
        AndroidNetworking.upload(ApiServer.ServerKey.Register_Api)
                .addMultipartParameter(ApiServer.ApiParams.Name,Name)
                .addMultipartParameter(ApiServer.ApiParams.Email,Email)
                .addMultipartParameter(ApiServer.ApiParams.Phone,Phone)
                .addMultipartParameter(ApiServer.ApiParams.Address,Address)
                .addMultipartParameter(ApiServer.ApiParams.Zip_code,Zip_code)
                .addMultipartParameter(ApiServer.ApiParams.Password,Password)
                .addMultipartFile(ApiServer.ApiParams.User_profile,file)
                .setTag("Login")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Registerresponse"+response);
                        dialog.dismiss();

                        try {
                            System.out.println("forgotresresponse"+response);

                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")){
                                String message=jsonObject.getString("message");
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                JSONObject jsonObject1=jsonArray.getJSONObject(0);
                                String id=jsonObject1.getString("id");
                                String name=jsonObject1.getString("name");
                                String email=jsonObject1.getString("email");
                                String phone=jsonObject1.getString("phone");
                                String user_profile=jsonObject1.getString("user_profile");
                                String password =jsonObject1.getString("password");
                                String address =jsonObject1.getString("address");
                                String zip_code =jsonObject1.getString("zip_code");
                                String created_at =jsonObject1.getString("created_at");
                                session.createLoginSession(id,name,email,phone,user_profile,password,address,zip_code);
                                System.out.println("forgotresresponse"+name);
                                Toast.makeText(OtpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(OtpActivity.this,MainActivity.class);
                                startActivity(intent);
                                finishActivity();


                            }
                            else {
                                Toast.makeText(OtpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }




                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        catch (NullPointerException e){
                            e.fillInStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println("forgotresError"+anError.toString());
                        System.out.println("forgotresError"+anError.getErrorBody());
                        System.out.println("forgotresError"+anError.getResponse());
                        Toast.makeText(OtpActivity.this, "Allready Register", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

    }

}
