package com.avin.avin.allactivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
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
import com.github.javiersantos.bottomdialogs.BottomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;

import custom.RainbowTextView;

public class BookingActivity extends BaseActivity {
    ImageButton back;
    TextView tv_name,tv_nameservice;
    HashMap<String, String> user ;
    String  UserId;
    SessionManager sessionManager;
    EditText et_firstname,et_fullemailaddresss,et_fullmobileno,etdate,et_firsttime,et_firstaddress,et_zipcodecheck;

    private java.text.SimpleDateFormat dateFormatter;

    private DatePickerDialog dobDatePickerDialog;
    private int mYear, mMonth, mDay, mHour, mMinute;
    TimePickerDialog timePickerDialog;
    Calendar c;
    String am_pm = "";
    RainbowTextView bt_paynow;

    ProgressDialog dialog;
   BottomDialog bottomDialog;
    CheckBox checkBox2;
    String servicename,Id;
    String numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().hide();
        getSupportActionBar().hide();
        back=(ImageButton)findViewById(R.id.back);
        tv_name=(TextView) findViewById(R.id.tv_name);
        bt_paynow=(RainbowTextView) findViewById(R.id.bt_paynow);
        checkBox2=(CheckBox) findViewById(R.id.checkBox2);
        et_firstname=(EditText) findViewById(R.id.et_firstname);
        et_fullemailaddresss=(EditText) findViewById(R.id.et_fullemailaddresss);
        et_fullmobileno=(EditText) findViewById(R.id.et_fullmobileno);
        et_firsttime=(EditText) findViewById(R.id.et_firsttime);
         etdate=(EditText) findViewById(R.id.etdate);
        et_firstaddress=(EditText) findViewById(R.id.et_firstaddress);
        et_zipcodecheck=(EditText) findViewById(R.id.et_zipcodecheck);
        tv_nameservice=(TextView) findViewById(R.id.tv_nameservice);
        back.setVisibility(View.VISIBLE);
        tv_name.setText("Booking Now");
        servicename=getIntent().getStringExtra("servicename");
        Id=getIntent().getStringExtra("Id");
        System.out.println("CheckIdnameone"+Id+servicename);
        tv_nameservice.setText(servicename);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sessionManager = new SessionManager(BookingActivity.this);
        user = sessionManager.getUserDetails();
        UserId = user.get(SessionManager.KEY_ID);
        et_fullemailaddresss.setEnabled(false);
        et_fullmobileno.setEnabled(false);
        if (sessionManager.isLoggedIn()) {
            et_firstname.setText(user.get(SessionManager.KEY_full_name));
            et_fullemailaddresss.setText(user.get(SessionManager.KEY_email));
            et_fullmobileno.setText(user.get(SessionManager.KEY_mobile));
            et_firstaddress.setText(user.get(SessionManager.KEY_address));
            et_zipcodecheck.setText(user.get(SessionManager.KEY_zip_code));


        }
        numbers=generateSessionKey();
        System.out.println("number"+numbers);

        bt_paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggedIn()) {
                    if (Validations()) {
                        Pay();
                    }
                }else {
                    Toast.makeText(BookingActivity.this, "Please login here", Toast.LENGTH_SHORT).show();
                }
            }
        });


        try {

            dateFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd");

            dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            java.util.Calendar newCalendar = java.util.Calendar.getInstance();
            etdate.setOnTouchListener(new View.OnTouchListener() {
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

                            etdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            dobDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());



            et_firsttime.setOnTouchListener(new View.OnTouchListener() {
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

                            et_firsttime.setText(hourOfDay + ":" + minute+":"+am_pm);
                        }
                    }, mHour, mMinute, false);





        }catch (NullPointerException e){
            e.printStackTrace();
        }




    }

    public static String generateSessionKey(){
        String alphabet = new String("0123456789"); //9
        int n = alphabet.length(); //10

        String result = new String();
        Random r = new Random(); //11

        for (int i=0; i<10; i++) //12
            result = result + alphabet.charAt(r.nextInt(n)); //13

        return result;
    }

    private void Pay() {


        bottomDialog = new BottomDialog.Builder(this)
                .setTitle("Are you sure you want to Book Now ?")
                .setPositiveText("YES")
                .setCancelable(false)
                .setPositiveBackgroundColorResource(R.color.colorPrimary)
                .setPositiveTextColorResource(android.R.color.white)
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(BottomDialog dialog) {
                        bottomDialog.dismiss();

                        Booking();

                    }
                })
                .setNegativeText("NO")
                .setNegativeTextColorResource(R.color.colorAccent)
                .onNegative(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(BottomDialog dialog) {
                        bottomDialog.dismiss();
                    }
                })
                .build();
        bottomDialog.show();

    }

    private void Booking() {

        dialog = new ProgressDialog(BookingActivity.this);
        dialog.setMessage("Please wait.");
        dialog.setCancelable(false);
        dialog.show();
        AndroidNetworking.post(ApiServer.ServerKey.Booking_Api)
                .addBodyParameter(ApiServer.ApiParams.User_id,UserId)
                .addBodyParameter(ApiServer.ApiParams.Name,et_firstname.getText().toString())
                .addBodyParameter(ApiServer.ApiParams.Email,et_fullemailaddresss.getText().toString())
                .addBodyParameter(ApiServer.ApiParams.Phone,et_fullmobileno.getText().toString())
                .addBodyParameter(ApiServer.ApiParams.Address,et_firstaddress.getText().toString())
                .addBodyParameter(ApiServer.ApiParams.Zip_code,et_zipcodecheck.getText().toString())
                .addBodyParameter(ApiServer.ApiParams.Date,etdate.getText().toString())
                .addBodyParameter(ApiServer.ApiParams.Time,et_firsttime.getText().toString())
                .addBodyParameter(ApiServer.ApiParams.Service_name,tv_nameservice.getText().toString())
                .addBodyParameter(ApiServer.ApiParams.Service_id,Id)
                .addBodyParameter(ApiServer.ApiParams.Status,"Pending")
                .addBodyParameter(ApiServer.ApiParams.OrderNo,numbers)
                .setTag("Login")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        System.out.println("BookingPassponce"+response);

                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")){
                                 String message=jsonObject.getString("message");
                                  Toast.makeText(BookingActivity.this, message, Toast.LENGTH_SHORT).show();
                                  onBackPressed();

                            }
                            else {
                                Toast.makeText(BookingActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
        if (et_firstname.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (et_fullemailaddresss.getText().toString().isEmpty()){
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
        else if (etdate.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter date", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (et_firsttime.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter time", Toast.LENGTH_SHORT).show();
            return false;
        }
        else  if (checkBox2.isChecked()==false) {
            Toast.makeText(this, "Please select terms & Conditions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
      //  intent.putExtra("Id",getIntent().getStringExtra("Idone"));
       // intent.putExtra("servicename",getIntent().getStringExtra("servicenameone"));
        startActivity(intent);
        finishActivity();
    }
}
