package com.avin.avin.allactivity;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePasswordActivity extends BaseActivity {
    ImageButton back;
    TextView tv_name;


    EditText et_oldpassword,et_newpassword,et_repassword;
    TextInputLayout et_oldpasshints,et_hintsnew,et_hintsrepass;
    Button bt_changepass;
    SessionManager sessionManager;
    HashMap<String, String> user ;
    String  UserId,mobile;
    ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().hide();
        back=(ImageButton)findViewById(R.id.back);
        tv_name=(TextView) findViewById(R.id.tv_name);
        back.setVisibility(View.VISIBLE);
        tv_name.setText("Password Change");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        et_oldpassword=(EditText)findViewById(R.id.et_oldpassword);
        et_newpassword=(EditText)findViewById(R.id.et_newpassword);
        et_repassword=(EditText)findViewById(R.id.et_repassword);
        bt_changepass=(Button)findViewById(R.id.bt_changepass);

        bt_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
        et_oldpasshints=(TextInputLayout)findViewById(R.id.et_oldpasshints);
        et_hintsnew=(TextInputLayout)findViewById(R.id.et_hintsnew);
        et_hintsrepass=(TextInputLayout)findViewById(R.id.et_hintsrepass);

        et_oldpassword.addTextChangedListener(new MyTextWatcher(et_oldpassword));
        et_newpassword.addTextChangedListener(new MyTextWatcher(et_newpassword));
        et_repassword.addTextChangedListener(new MyTextWatcher(et_repassword));

        et_oldpassword.setEnabled(false);
        sessionManager=new SessionManager(ChangePasswordActivity.this);
        user = sessionManager.getUserDetails();
        UserId= user.get(SessionManager.KEY_ID);
        et_oldpassword.setText(user.get(SessionManager.KEY_password));
    }
    private void submitForm() {
        if (!validateOldPassword()) {
            return;
        }
        else if (!validateNewPassword()) {
            return;
        }

        else if (!validateConfirmPassword()) {
            return;
        }else {
            ChangePass();
        }



    }

    private boolean isValidPassword(String pass) {
        boolean check=false;
        //if(!Pattern.matches("[a-zA-Z]+", phone)) {
        if(pass.length() < 4|| pass.length() > 8) {
            // if(phone.length() != 10) {
            check = false;
            et_newpassword.setError("Password is Wrong");
        } else {
            check = true;
        }
        // } else {
        // check=false;
        // }
        return check;
    }

    private boolean validateOldPassword() {
        if (et_oldpassword.getText().toString().trim().isEmpty()) {
            et_oldpasshints.setError("Please enter old password");
            requestFocus(et_oldpassword);
            return false;
        } else {
            et_oldpasshints.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateNewPassword() {
        String password = et_newpassword.getText().toString().trim();
        if (password.isEmpty()/*||!isValidPassword(password)*/) {
            et_hintsnew.setError("Please enter password");
            requestFocus(et_newpassword);
            return false;
        } else {
            et_hintsnew.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateConfirmPassword() {
        String pass=et_repassword.getText().toString().trim();
        String newpass=et_newpassword.getText().toString().trim();

        if (pass.isEmpty()|| !newpass.equals(pass)) {
            et_hintsrepass.setError("Please enter confirmed  not match new password");
            requestFocus(et_repassword);
            return false;
        } else {
            et_hintsrepass.setErrorEnabled(false);
        }

        return true;
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
                case R.id.et_oldpassword:
                    validateOldPassword();
                    break;
                case R.id.et_newpassword:
                    validateNewPassword();
                    break;
                case R.id.et_repassword:
                    validateConfirmPassword();
                    break;
            }
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ChangePasswordActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void ChangePass() {

        try {



                // do network operation here
                dialog = new ProgressDialog( ChangePasswordActivity.this);
                dialog.setMessage("Please wait.");
                dialog.setCancelable(false);
                dialog.show();
                AndroidNetworking.post(ApiServer.ServerKey.ChangePasswordApi_Api)
                        .addBodyParameter(ApiServer.ApiParams.User_id,UserId)
                        .addBodyParameter(ApiServer.ApiParams.Password,et_repassword.getText().toString())
                        .setTag("Login")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                dialog.dismiss();
                                System.out.println("changePassponce"+response);

                                try {

                                    JSONObject jsonObject=new JSONObject(response);
                                    String status=jsonObject.getString("status");
                                    if (status.equalsIgnoreCase("1")){
                                        String message=jsonObject.getString("message");
                                        sessionManager.createLoginSession(user.get(SessionManager.KEY_ID),user.get(SessionManager.KEY_full_name),
                                                user.get(SessionManager.KEY_email),user.get(SessionManager.KEY_email),user.get(SessionManager.KEY_user_profile),
                                                et_repassword.getText().toString(),user.get(SessionManager.KEY_address),user.get(SessionManager.KEY_zip_code));
                                        Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();



                                    }
                                    else {
                                        Toast.makeText(ChangePasswordActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                                dialog.dismiss();
                                System.out.println("forgotresError"+anError.getErrorBody());
                            }
                        });



        }catch (NullPointerException e){
            e.fillInStackTrace();
        }

    }
}
