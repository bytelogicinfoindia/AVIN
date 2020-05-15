package com.avin.avin.allactivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avin.avin.R;
import com.avin.avin.otherclass.BaseActivity;

public class ForgotActivity extends BaseActivity {

    TextView tv_backlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        tv_backlogin=(TextView)findViewById(R.id.tv_backlogin);

     //   getSupportActionBar().setTitle("Frogot Password ");
        TextView textView = new TextView(this);
        textView.setText("Frogot Password ");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        getSupportActionBar().setCustomView(textView);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.colorWhite));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);


        StringBuilder aboutstring = new StringBuilder();
        aboutstring.append("<tr>")
                .append("<td><font color='#000'>")
                .append("Back to?")
                .append("</font></td>");
        aboutstring.append("</tr>")
                .append("<td><font color='#ff0000'>")
                .append("<b> Login</b>")
                .append("</font></td>");
        aboutstring.append("</tr>");
        aboutstring.append("<br/>");
        tv_backlogin.setText(Html.fromHtml(aboutstring.toString()));


        tv_backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ForgotActivity.this,LoginActivity.class);
                startActivity(intent);
                finishActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finishActivity();
    }
}
