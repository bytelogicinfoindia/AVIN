package com.avin.avin.allactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avin.avin.R;
import com.avin.avin.otherclass.BaseActivity;

public class TermsPolicyActivity extends BaseActivity {

    ImageButton back;
    TextView tv_name;
    String name;
    TextView tv_titlenameshow,tv_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_policy);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        name=getIntent().getStringExtra("Name");
        back=(ImageButton)findViewById(R.id.back);
        tv_name=(TextView) findViewById(R.id.tv_name);
        tv_desc=(TextView) findViewById(R.id.tv_desc);
        back.setVisibility(View.VISIBLE);
        tv_name.setText(name);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        if (name.equalsIgnoreCase("Privacy Policy")){
            tv_desc.setText("Term insurance plan is a form of life cover, it provides coverage for defined period of time, and if the insured expires during the term of the policy then death benefit is payable to nominee. Term plans are specifically designed to secure your family needs in case of death or uncertainty");

        }else {

            tv_desc.setText("The Terms of Service Agreement is mainly used for legal purposes by companies which provide software or services, such as web browsers, e-commerce, web search engines, social media, and transport services.");

        }

    }
}
