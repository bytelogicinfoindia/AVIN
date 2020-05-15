package com.avin.avin.allactivity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avin.avin.R;
import com.avin.avin.otherclass.BaseActivity;

public class ContactUsActivity extends BaseActivity {


    ImageButton back;
    TextView tv_name;


    ImageButton bt_back;
    RelativeLayout re_sharingApp;
    Dialog mBottomSheetDialog;
    TextView tv_contact,tv_emails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        back=(ImageButton)findViewById(R.id.back);
        tv_name=(TextView) findViewById(R.id.tv_name);
        back.setVisibility(View.VISIBLE);
        tv_name.setText("Contact Us ");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        tv_contact=(TextView) findViewById(R.id.tv_contact);
        tv_emails=(TextView) findViewById(R.id.tv_emails);

        tv_emails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String recepientEmail = tv_emails.getText().toString(); // either set to destination email or leave empty
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + recepientEmail));
                    startActivity(intent);
                }catch (ActivityNotFoundException e){
                    e.fillInStackTrace();
                }
            }
        });

        tv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String number=tv_contact.getText().toString();
                    Intent call_intent = new Intent(Intent.ACTION_DIAL);
                    call_intent.setData(Uri.parse("tel:" + number));
                    startActivity(call_intent);
                } catch (NullPointerException e) {
                    e.fillInStackTrace();
                }

            }
        });



    }
}
