package com.avin.avin.otherclass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.avin.avin.allactivity.MainActivity;
import com.avin.avin.R;

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    public void setToolbar(Toolbar toolbar, boolean titleEnabled){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(titleEnabled);
    }
    public void setToolbarTitle(int resId){
        getSupportActionBar().setTitle(resId);
    }


    /*  @Override
        public void onBackPressed() {
            super.onBackPressed();
            finishActivity();
        }
    */
    public void startActivity(Context context, Class name){
        startActivity(new Intent(context, name));
    }

    public void finishActivity(){
        this.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

  /*  public void setToastName(){

        Toast toast = Toast.makeText(BaseActivity.this, "Please Enter Your Name", Toast.LENGTH_LONG);
        View view1 = toast.getView();
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
        view1.setBackgroundResource(R.drawable.toast_squarebtn);
        TextView text = (TextView) view1.findViewById(android.R.id.message);
        text.setGravity(View.TEXT_ALIGNMENT_CENTER);
        text.setTextSize(R.dimen._14sdp);
        toast.show();

    }

    public void setToastEmail(){

        Toast toast = Toast.makeText(BaseActivity.this, "Please Enter Your Email", Toast.LENGTH_LONG);
        View view1 = toast.getView();
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
        view1.setBackgroundResource(R.drawable.toast_squarebtn);
        TextView text = (TextView) view1.findViewById(android.R.id.message);
        text.setGravity(View.TEXT_ALIGNMENT_CENTER);
        text.setTextSize(R.dimen._14sdp);
        toast.show();

    }


    public void setToastPass(){

        Toast toast = Toast.makeText(BaseActivity.this, "Please Enter Your Password", Toast.LENGTH_LONG);
        View view1 = toast.getView();
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
        view1.setBackgroundResource(R.drawable.toast_squarebtn);
        TextView text = (TextView) view1.findViewById(android.R.id.message);
        text.setGravity(View.TEXT_ALIGNMENT_CENTER);
        text.setTextSize(R.dimen._14sdp);
        toast.show();

    }


    public void setToastccode(){

        Toast toast = Toast.makeText(BaseActivity.this, "Please Enter Your Country Code", Toast.LENGTH_LONG);
        View view1 = toast.getView();
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
        view1.setBackgroundResource(R.drawable.toast_squarebtn);
        TextView text = (TextView) view1.findViewById(android.R.id.message);
        text.setGravity(View.TEXT_ALIGNMENT_CENTER);
        text.setTextSize(R.dimen._14sdp);
        toast.show();

    }



    public void setToastcnumber(){

        Toast toast = Toast.makeText(BaseActivity.this, "Please Fill Valid Mobile Number", Toast.LENGTH_LONG);
        View view1 = toast.getView();
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
        view1.setBackgroundResource(R.drawable.toast_squarebtn);
        TextView text = (TextView) view1.findViewById(android.R.id.message);
        text.setGravity(View.TEXT_ALIGNMENT_CENTER);
        text.setTextSize(R.dimen._14sdp);
        toast.show();

    }



    public  void setToastInvalid(){

        Toast toast = Toast.makeText(BaseActivity.this, "Invalid Email Id", Toast.LENGTH_LONG);
        View view1 = toast.getView();
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        view1.setBackgroundResource(R.drawable.toast_squarebtn);
        TextView text = (TextView) view1.findViewById(android.R.id.message);
        text.setGravity(View.TEXT_ALIGNMENT_CENTER);
        text.setTextSize(12);
        toast.show();
    }



    public  void setToastMobileNo(){

        Toast toast = Toast.makeText(BaseActivity.this, "Please Enter Your Mobile No.", Toast.LENGTH_LONG);
        View view1 = toast.getView();
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        view1.setBackgroundResource(R.drawable.toast_squarebtn);
        TextView text = (TextView) view1.findViewById(android.R.id.message);
        text.setGravity(View.TEXT_ALIGNMENT_CENTER);
        text.setTextSize(12);
        toast.show();


    }

    public void setloginsucess(){
        Toast toast = Toast.makeText(BaseActivity.this, "SendData Login Successfully", Toast.LENGTH_LONG);
        View view = toast.getView();
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        view.setBackgroundResource(R.drawable.toast_squarebtn);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setGravity(View.TEXT_ALIGNMENT_CENTER);
        text.setTextSize(12);
        toast.show();

    }
    public  void  setErrorToast(){

        Toast toast = Toast.makeText(BaseActivity.this, "Server Error! Try again after some time.", Toast.LENGTH_LONG);
        View view1 = toast.getView();
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        view1.setBackgroundResource(R.drawable.toast_squarebtn);
        TextView text = (TextView) view1.findViewById(android.R.id.message);
        text.setGravity(View.TEXT_ALIGNMENT_CENTER);
        text.setTextSize(12);
        toast.show();

    }

    public  void  setToastReg()
    {

        Toast toast = Toast.makeText(BaseActivity.this, "Register successfully", Toast.LENGTH_LONG);
        View view1 = toast.getView();
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        view1.setBackgroundResource(R.drawable.toast_squarebtn);
        TextView text = (TextView) view1.findViewById(android.R.id.message);
        text.setGravity(View.TEXT_ALIGNMENT_CENTER);
        text.setTextSize(12);
        toast.show();



    }*/
    public void showLoadingDialog() {
        if (isDestroyingActivity()) return;
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, R.style.DialogTheme);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.dialog_lollipop_progress);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * @return boolean value
     * return true if Progress Dialog show else return false
     */
    public boolean isShowingProgressDialog() {
        return !(mProgressDialog == null) && mProgressDialog.isShowing();
    }

    /***
     * hide the Progress Dialog
     */
    public void hideLoadingDialog() {
        if (isDestroyingActivity())
            return;
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    public boolean isDestroyingActivity() {

        return isFinishing() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed();
    }

    public  void  exitDialog (){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();alert.show();

    }

    public  void  patdailoge(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(BaseActivity.this);
        builder1.setMessage("Please Add Your Amount For Pay ! ");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(BaseActivity.this, MainActivity.class));
                        finishActivity();
                        dialog.cancel();
                    }});
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }



    public  void  requestdailoge(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(BaseActivity.this);
        builder1.setMessage("Please Select Your Name For Pay Your Amount ");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }});
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }



          /*((EditText)findViewById(R.id.inputReason)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
              @Override
              public void onFocusChange(View v, boolean hasFocus) {
                  if (!hasFocus) {
                      TextView tvNo;
                      LayoutInflater inflater = LayoutInflater.from(context);
                      final Dialog mDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
                      mDialog.setCanceledOnTouchOutside(true);
                      mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                      mDialog.getWindow().setGravity(Gravity.BOTTOM);
                      WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
                      lp.dimAmount = 0.75f;
                      mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                      mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                      mDialog.getWindow();
                      View dialoglayout = inflater.inflate(R.layout.pay_dialog, null);
                      mDialog.setContentView(dialoglayout);
                      tvNo = (TextView) mDialog.findViewById(R.id.btnpay);
                      tvNo.setText("&"+amount + "" + " Pay ");
                      tvNo.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              startActivity(new Intent(context,MainActivity.class));
                              finishActivity();
                              mDialog.dismiss();
                          }
                      });
                      mDialog.show();
                  }
              }
          });*/



}

