package com.avin.avin.allactivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;


import custom.RainbowTextView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;

public class LoginActivity extends BaseActivity {
    TextView tv_name,tv_createaccount,tv_forgotpass;
    RainbowTextView bt_skip,et_login;
    EditText et_mobile,et_password;
    TextInputLayout tv_mobilenumber,et_passs;
    ProgressDialog dialog;
    SessionManager session;
    public static final int RequestPermissionCode = 1;
    private static final int PERMISSION_RQ = 84;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkPermission()) {
            Toast.makeText(this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        } else {
            requestPermission();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission to save videos in external storage
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
        }
        setContentView(R.layout.activity_login);
        tv_createaccount=(TextView) findViewById(R.id.tv_createaccount);
        tv_forgotpass=(TextView) findViewById(R.id.tv_forgotpass);
        bt_skip=(RainbowTextView) findViewById(R.id.bt_skip);
        session = new SessionManager(getApplicationContext());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        StringBuilder aboutstring = new StringBuilder();
        aboutstring.append("<tr>")
                .append("<td><font color='#000'>")
                .append("Dont' have an account? ")
                .append("</font></td>");
        aboutstring.append("</tr>")
                .append("<td><font color='#ff0000'>")
                .append("<b>Register Here </b>")
                .append("</font></td>");
        aboutstring.append("</tr>");
        aboutstring.append("<br/>");

        tv_createaccount.setText(Html.fromHtml(aboutstring.toString()));

        bt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finishActivity();


            }
        });

        tv_forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(intent);
                finishActivity();

            }
        });
        tv_createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finishActivity();

            }
        });
        et_mobile=(EditText)findViewById(R.id.et_mobile);
        et_password=(EditText)findViewById(R.id.et_password);
        et_login=(RainbowTextView) findViewById(R.id.bt_Login);
        tv_mobilenumber=(TextInputLayout) findViewById(R.id.tv_mobilenumber);
        et_passs=(TextInputLayout) findViewById(R.id.et_passs);

        et_mobile.addTextChangedListener(new MyTextWatcher(et_mobile));
        et_password.addTextChangedListener(new MyTextWatcher(et_password));

        et_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });


    }

    private boolean isValidPassword(String pass) {
        boolean check=false;
        //if(!Pattern.matches("[a-zA-Z]+", phone)) {
        if(pass.length() < 4|| pass.length() > 8) {
            // if(phone.length() != 10) {
            check = false;
            et_password.setError("Password is Wrong");
        } else {
            check = true;
        }
        // } else {
        // check=false;
        // }
        return check;
    }

    private void submitForm() {

        if (!validatePhone()) {
            return;
        }
        if (!validatePasword()) {
            return;
        }
        else {

            LoginMethod();

        }



    }

    private boolean validatePhone() {
        String phone = et_mobile.getText().toString().trim();

        if (phone.isEmpty() || !isValidMobile(phone)) {
            tv_mobilenumber.setError("Please enter mobile number");
            requestFocus(et_mobile);
            return false;
        } else {
            tv_mobilenumber.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePasword() {
        String password = et_password.getText().toString().trim();

        if (password.isEmpty()/*||!isValidPassword(password)*/) {
            et_passs.setError("Please enter password");
            requestFocus(et_password);
            return false;
        } else {
            et_passs.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
           getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 10 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
                et_mobile.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
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

                case R.id.et_mobile:
                    validatePhone();
                    break;
                case R.id.et_password:
                    validatePasword();
                    break;
            }
        }
    }


    private void LoginMethod() {

        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Please wait.");
        dialog.setCancelable(false);
        dialog.show();

       AndroidNetworking.post(ApiServer.ServerKey.Login_Api)
                .addBodyParameter(ApiServer.ApiParams.Phone,et_mobile.getText().toString())
                .addBodyParameter(ApiServer.ApiParams.Password,et_password.getText().toString())
                .setTag("Login")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("forgotresresponse"+response);
                        dialog.dismiss();

                        try {

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

                                Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                finishActivity();


                            }
                            else {
                                Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                    }
                });



    }

  /// retrofit method

   /* Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://baccord.in/alien/index.php/alienApi/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();*/



       /* RetrofitInterface service = retrofit.create(RetrofitInterface .class);
        Call<Result> call=service.getStringScalar(new SendData(phone,password));
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                dialog.dismiss();
                Log.e("LoginResponce",""+ response.body().getStatus());
                Log.e("LoginResponce",""+ response.body().getMessage());
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                dialog.dismiss();
                //for getting error in network put here Toast, so get the error on network
            }
        });
*/


      /*  RetrofitInterface apiInterface = retrofit.create(RetrofitInterface.class);

        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("phone", "9555669857");
            requestBody.put("password", "12345");
            Call<Object> call=apiInterface.getUser(requestBody);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONObject object=new JSONObject(new Gson().toJson(response.body()));
                        Log.e("LoginResponce",""+ object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

    private void requestPermission() {
          android.support.v4.app.ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, READ_CONTACTS, READ_PHONE_STATE, ACCESS_FINE_LOCATION, READ_EXTERNAL_STORAGE}, RequestPermissionCode);

      }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadLocation = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStrage = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && ReadContactsPermission && ReadPhoneStatePermission && ReadLocation && ReadExternalStrage) {

                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        //  Toast.makeText(LoginActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }


    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(this, READ_CONTACTS);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(this, READ_PHONE_STATE);
        int fineloaction = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        int externalstorage = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED && SecondPermissionResult == PackageManager.PERMISSION_GRANTED && ThirdPermissionResult == PackageManager.PERMISSION_GRANTED && fineloaction == PackageManager.PERMISSION_GRANTED && externalstorage == PackageManager.PERMISSION_GRANTED;
    }
}
