package com.avin.avin.allactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
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

import custom.RainbowTextView;

public class EditProfileActivity extends BaseActivity {

    ImageButton back;
    TextView tv_name;
    EditText tv_votefirstname,tv_emails,tv_phone,tv_address,tv_zipcode;
    RainbowTextView bt_save;
    HashMap<String, String> user ;
    String  UserId,service_name;
    SessionManager sessionManager;

    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().hide();
        back=(ImageButton)findViewById(R.id.back);
        tv_name=(TextView) findViewById(R.id.tv_name);
        back.setVisibility(View.VISIBLE);
        tv_name.setText("Edit Profile");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finishActivity();
            }
        });

        tv_votefirstname=(EditText)findViewById(R.id.tv_votefirstname);
        tv_emails=(EditText)findViewById(R.id.tv_emails);
        tv_phone=(EditText)findViewById(R.id.tv_phone);
        tv_address=(EditText)findViewById(R.id.tv_address);
        tv_zipcode=(EditText)findViewById(R.id.tv_zipcode);
        sessionManager = new SessionManager(EditProfileActivity.this);
        bt_save=(RainbowTextView)findViewById(R.id.bt_save);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggedIn()) {
                    if (Validations()) {
                      EditProfilMethod();
                    }
                }else {
                    Toast.makeText(EditProfileActivity.this, "Please login here", Toast.LENGTH_SHORT).show();
                }
            }
        });


        user = sessionManager.getUserDetails();
        UserId = user.get(SessionManager.KEY_ID);
        tv_emails.setEnabled(false);
        tv_phone.setEnabled(false);
        if (sessionManager.isLoggedIn()) {
            tv_votefirstname.setText(user.get(SessionManager.KEY_full_name));
            tv_emails.setText(user.get(SessionManager.KEY_email));
            tv_phone.setText(user.get(SessionManager.KEY_mobile));
            tv_address.setText(user.get(SessionManager.KEY_address));
            tv_zipcode.setText(user.get(SessionManager.KEY_zip_code));


        }

    }

    private void EditProfilMethod() {


        dialog = new ProgressDialog(EditProfileActivity.this);
        dialog.setMessage("Please wait.");
        dialog.setCancelable(false);
        dialog.show();
        AndroidNetworking.post(ApiServer.ServerKey.UpdateProfileApi_Api)
                .addBodyParameter(ApiServer.ApiParams.User_id,UserId)
                .addBodyParameter(ApiServer.ApiParams.Name,tv_votefirstname.getText().toString())
                .addBodyParameter(ApiServer.ApiParams.Address,tv_address.getText().toString())
                .addBodyParameter(ApiServer.ApiParams.Zip_code,tv_zipcode.getText().toString())
                .setTag("Login")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        System.out.println("BookingUpdateProfile"+response);

                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")){
                                String message=jsonObject.getString("message");

                                sessionManager.createLoginSession(user.get(SessionManager.KEY_ID),tv_votefirstname.getText().toString(),
                                        tv_emails.getText().toString(),tv_phone.getText().toString(),user.get(SessionManager.KEY_user_profile),
                                        user.get(SessionManager.KEY_password),tv_address.getText().toString(),tv_zipcode.getText().toString());
                                Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();


                            }
                            else {
                                Toast.makeText(EditProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    }

    private boolean Validations() {
        if (tv_votefirstname.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (tv_emails.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (tv_phone.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter contact number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (tv_address.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter full address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (tv_zipcode.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter zip code", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
