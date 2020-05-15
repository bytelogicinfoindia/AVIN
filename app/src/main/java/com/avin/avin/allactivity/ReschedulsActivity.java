package com.avin.avin.allactivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import custom.RainbowTextView;

public class ReschedulsActivity extends BaseActivity {

    ImageButton back;
    TextView tv_name;
    EditText et_firstbookname,et_fullemail,et_fullmobileno,et_firstaddress,et_zipcodecheck,et_date,et_time;
    ProgressDialog dialog;
    SessionManager sessionManager;
    HashMap<String, String> user ;
    String UserId;
    String ID;
    RainbowTextView bt_booknow;

    private java.text.SimpleDateFormat dateFormatter;

    private DatePickerDialog dobDatePickerDialog;
    private int mYear, mMonth, mDay, mHour, mMinute;
    TimePickerDialog timePickerDialog;
    Calendar c;
    String am_pm = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescheduls);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        back=(ImageButton)findViewById(R.id.back);
        tv_name=(TextView) findViewById(R.id.tv_name);
        bt_booknow=(RainbowTextView) findViewById(R.id.bt_booknow);

        back.setVisibility(View.VISIBLE);
        tv_name.setText("Re-Scheduls ");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        et_firstbookname=(EditText) findViewById(R.id.et_firstbookname);
        et_fullemail=(EditText) findViewById(R.id.et_fullemail);
        et_fullmobileno=(EditText) findViewById(R.id.et_fullmobileno);
        et_firstaddress=(EditText) findViewById(R.id.et_firstaddress);
        et_zipcodecheck=(EditText) findViewById(R.id.et_zipcodecheck);
        et_date=(EditText) findViewById(R.id.et_date);
        et_time=(EditText) findViewById(R.id.et_time);
        ID=getIntent().getStringExtra("ID");


        et_fullemail.setEnabled(false);
        et_fullmobileno.setEnabled(false);

        sessionManager = new SessionManager(ReschedulsActivity.this);
        user = sessionManager.getUserDetails();
        UserId=user.get(SessionManager.KEY_ID);
        if (sessionManager.isLoggedIn()) {
            et_firstbookname.setText(user.get(SessionManager.KEY_full_name));
            et_fullemail.setText(user.get(SessionManager.KEY_email));
            et_fullmobileno.setText(user.get(SessionManager.KEY_mobile));
            et_firstaddress.setText(user.get(SessionManager.KEY_address));
            et_zipcodecheck.setText(user.get(SessionManager.KEY_zip_code));

        }
        bt_booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Validations()){
                    if (sessionManager.isLoggedIn()) {
                        Rescheduls(ID);

                    }else {
                        Toast.makeText(ReschedulsActivity.this, "Please Login Here", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        try {

            dateFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd");

            dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            java.util.Calendar newCalendar = java.util.Calendar.getInstance();
            et_date.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    dobDatePickerDialog.show();
                    return false;
                }
            });

            // Get Current Date
            c= Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);



            dobDatePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            et_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            dobDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());



            et_time.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    timePickerDialog.show();
                    return false;
                }
            });
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            if (c.get(Calendar.AM_PM) == Calendar.AM) {
                am_pm = "AM";
            }
            else if (c.get(Calendar.AM_PM) == Calendar.PM) {
                am_pm = "PM";
            }

            // Launch Time Picker Dialog
            timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            et_time.setText(hourOfDay + ":" + minute+":"+am_pm);
                        }
                    }, mHour, mMinute, false);





        }catch (NullPointerException e){
            e.printStackTrace();
        }





    }

    private boolean Validations() {
        if (et_firstbookname.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (et_fullemail.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (et_fullmobileno.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter contact number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (et_firstaddress.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter full address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (et_zipcodecheck.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter zip code", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (et_date.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter date", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (et_time.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter time", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void Rescheduls(String Id) {

        try {
                dialog = new ProgressDialog(ReschedulsActivity.this);
                dialog.setMessage("Please wait.");
                dialog.setCancelable(false);
                dialog.show();
                AndroidNetworking.post(ApiServer.ServerKey.ReschedulsApi_Api)
                        .addBodyParameter(ApiServer.ApiParams.User_id,UserId)
                        .addBodyParameter(ApiServer.ApiParams.ID,Id)
                        .addBodyParameter(ApiServer.ApiParams.Name,et_firstbookname.getText().toString())
                        .addBodyParameter(ApiServer.ApiParams.Email,et_fullemail.getText().toString())
                        .addBodyParameter(ApiServer.ApiParams.Phone,et_fullmobileno.getText().toString())
                        .addBodyParameter(ApiServer.ApiParams.Address,et_firstaddress.getText().toString())
                        .addBodyParameter(ApiServer.ApiParams.Zip_code,et_zipcodecheck.getText().toString())
                        .addBodyParameter(ApiServer.ApiParams.Date,et_date.getText().toString())
                        .addBodyParameter(ApiServer.ApiParams.Time,et_time.getText().toString())
                        .setTag("Login")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                dialog.dismiss();
                                System.out.println("ReschedulsPassponce"+response);

                                try {

                                    JSONObject jsonObject=new JSONObject(response);
                                    String status=jsonObject.getString("status");
                                    if (status.equalsIgnoreCase("1")){
                                        String message=jsonObject.getString("message");
                                        Toast.makeText(ReschedulsActivity.this, message, Toast.LENGTH_SHORT).show();


                                    }
                                    else {
                                        Toast.makeText(ReschedulsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
