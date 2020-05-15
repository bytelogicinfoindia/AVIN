package com.avin.avin.allactivity;
import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.avin.avin.R;
import com.avin.avin.otherclass.BaseActivity;
import com.avin.avin.server.ApiServer;
import com.avin.avin.server.FilePath;
import com.avin.avin.server.SessionManager;
import com.google.firebase.FirebaseApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.regex.Pattern;
import custom.RainbowTextView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;

public class RegisterActivity extends BaseActivity {

    TextView tv_register, bt_forgotpass;
    TextInputLayout et_namehits, tv_hintsemails, et_hintsphone, et_hintsaddress, et_zipcdehints, et_passwordhints;
    EditText et_name, et_emailregister, et_mobile, et_address, et_zipcode, et_Pass;
    RainbowTextView bt_Register;

    ImageButton add_image_profile;
    private final int CAMERA_REQUEST = 20;
    int PICK_IMAGE_MULTIPLE = 100;
    Bitmap bitmap;
    String PDfcheck,pathsdadda,selectedPath;

    String pimage = "null";
    CircleImageView profile_image;
    public static final int RequestPermissionCode = 1;
    private static final int PERMISSION_RQ = 84;
    ProgressDialog dialog;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.register);
        TextView textView = new TextView(this);
        textView.setText("Register ");
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
        tv_register = (TextView) findViewById(R.id.tv_register);
        bt_forgotpass = (TextView) findViewById(R.id.bt_forgotpass);
        add_image_profile = (ImageButton) findViewById(R.id.add_image_profile);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        session = new SessionManager(getApplicationContext());
        StringBuilder aboutstring = new StringBuilder();
        aboutstring.append("<tr>")
                .append("<td><font color='#000'>")
                .append("Already Registered ")
                .append("</font></td>");
        aboutstring.append("</tr>")
                .append("<td><font color='#ff0000'>")
                .append("<b>Login Here </b>")
                .append("</font></td>");
        aboutstring.append("</tr>");
        aboutstring.append("<br/>");
        tv_register.setText(Html.fromHtml(aboutstring.toString()));
        bt_forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotActivity.class));
                finishActivity();

            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finishActivity();
            }
        });

        bt_Register = (RainbowTextView) findViewById(R.id.bt_Register);

        bt_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitForm();
            }

        });
        add_image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkPermission()) {
                    selectImage();

                } else {

                    requestPermission();

                }

            }
        });

        et_namehits = (TextInputLayout) findViewById(R.id.et_namehits);
        tv_hintsemails = (TextInputLayout) findViewById(R.id.tv_hintsemails);
        et_hintsphone = (TextInputLayout) findViewById(R.id.et_hintsphone);
        et_hintsaddress = (TextInputLayout) findViewById(R.id.et_hintsaddress);
        et_zipcdehints = (TextInputLayout) findViewById(R.id.et_zipcdehints);
        et_passwordhints = (TextInputLayout) findViewById(R.id.et_passwordhints);

        et_name = (EditText) findViewById(R.id.et_name);
        et_emailregister = (EditText) findViewById(R.id.et_emailregister);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_address = (EditText) findViewById(R.id.et_address);
        et_zipcode = (EditText) findViewById(R.id.et_zipcode);
        et_Pass = (EditText) findViewById(R.id.et_Pass);


        et_name.addTextChangedListener(new MyTextWatcher(et_name));
        et_emailregister.addTextChangedListener(new MyTextWatcher(et_emailregister));
        et_mobile.addTextChangedListener(new MyTextWatcher(et_mobile));
        et_address.addTextChangedListener(new MyTextWatcher(et_address));
        et_zipcode.addTextChangedListener(new MyTextWatcher(et_zipcode));
        et_Pass.addTextChangedListener(new MyTextWatcher(et_Pass));




    }


    private void submitForm() {
        if (!validateName()) {
            return ;
        }
       if (!validateEmail()) {
            return;
        }
        else if (!validatePhone()) {
            return;
        }
        else if (!validateAddress()) {
            return;
        }
        else  if (!validateZipCode()) {
            return;
        }
        else  if (!validatePasword()) {

            return;

        } else {

           RegisterMethod();
            /*if (pimage.equalsIgnoreCase("null")) {
                Toast.makeText(RegisterActivity.this, "Please select profile image", Toast.LENGTH_SHORT).show();


            }else {

            }*/

        }

    }

    private void RegisterMethod() {
/*
        Intent intent=new Intent(RegisterActivity.this,OtpActivity.class);
        intent.putExtra("ImagePath",selectedPath);
        intent.putExtra("Name",et_name.getText().toString());
        intent.putExtra("Email",et_emailregister.getText().toString());
        intent.putExtra("Phone",et_mobile.getText().toString());
        intent.putExtra("Address",et_address.getText().toString());
        intent.putExtra("Zip_code",et_zipcode.getText().toString());
        intent.putExtra("Password",et_Pass.getText().toString());
        startActivity(intent);
        finishActivity();*/

        dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setMessage("Please wait.");
        dialog.setCancelable(false);
        dialog.show();
        //File  file = new File(selectedPath);
         AndroidNetworking.upload(ApiServer.ServerKey.Register_Api)
                .addMultipartParameter(ApiServer.ApiParams.Name,et_name.getText().toString())
                .addMultipartParameter(ApiServer.ApiParams.Email,et_emailregister.getText().toString())
                .addMultipartParameter(ApiServer.ApiParams.Phone,et_mobile.getText().toString())
                .addMultipartParameter(ApiServer.ApiParams.Address,et_address.getText().toString())
                .addMultipartParameter(ApiServer.ApiParams.Zip_code,et_zipcode.getText().toString())
                .addMultipartParameter(ApiServer.ApiParams.Password,et_Pass.getText().toString())
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
                                Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                startActivity(intent);
                                finishActivity();


                            }
                            else {
                                Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegisterActivity.this, "Allready Register", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

    }

    private boolean validateName() {
        String name = et_name.getText().toString();
        if (name.isEmpty()) {
            et_namehits.setError("Please enter full name");
            requestFocus(et_name);
            return false;
        } else {
            et_namehits.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = et_emailregister.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            tv_hintsemails.setError("Please enter emails address");
            requestFocus(et_emailregister);
            return false;
        } else {
            tv_hintsemails.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePhone() {
        String phone = et_mobile.getText().toString().trim();

        if (phone.isEmpty() || !isValidMobile(phone)) {
            et_hintsphone.setError("Please enter mobile number");
            requestFocus(et_mobile);
            return false;
        } else {
            et_hintsphone.setErrorEnabled(false);
        }

        return true;
    }

    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 10 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
                et_mobile.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    private boolean validateAddress() {

        String address = et_address.getText().toString();
        if (address.isEmpty()) {
            et_hintsaddress.setError("Please enter address");
            requestFocus(et_address);
            return false;
        } else {
            et_hintsaddress.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateZipCode() {

        String zip_code = et_zipcode.getText().toString().trim();

        if (zip_code.isEmpty() /*|| !isValidZipCode(zip_code)*/) {
            et_zipcdehints.setError("Please enter ZipCode");
            requestFocus(et_zipcode);
            return false;
        } else {
            et_zipcdehints.setErrorEnabled(false);
        }

        return true;
    }

    private boolean isValidZipCode(String pass) {
        boolean check = false;
        //if(!Pattern.matches("[a-zA-Z]+", phone)) {
        if (pass.length() < 4 || pass.length() > 6) {
            // if(phone.length() != 10) {
            check = false;
            et_zipcode.setError("Zipcode Is Short");
        } else {
            check = true;
        }
        // } else {
        // check=false;
        // }
        return check;
    }

    private boolean validatePasword() {
        String password = et_Pass.getText().toString().trim();

        if (password.isEmpty() /*|| !isValidPassword(password)*/) {
            et_passwordhints.setError("Please enter password");
            requestFocus(et_Pass);
            return false;
        } else {
            et_passwordhints.setErrorEnabled(false);
        }

        return true;
    }

    private boolean isValidPassword(String pass) {
        boolean check = false;
        //if(!Pattern.matches("[a-zA-Z]+", phone)) {
        if (pass.length() < 4 || pass.length() > 8) {
            // if(phone.length() != 10) {
            check = false;
            et_Pass.setError("Password is Wrong");
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

                case R.id.et_name:
                    validateName();
                    break;
                case R.id.et_emailregister:
                    validateEmail();
                    break;
                case R.id.et_mobile:
                    validatePhone();
                    break;
                case R.id.et_address:
                    validateAddress();
                    break;
                case R.id.et_zipcode:
                    validateZipCode();
                    break;
                case R.id.et_Pass:
                    validatePasword();
                    break;
            }
        }
    }



    /**
     * *********** Dialog  Image,PDF and Video Method   **********
     */


    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery ","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

        builder.setTitle("Add Photo & Video!");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))
                {
                    PDfcheck="PHOTOIMAGE";
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST);


                }
                else if (options[item].equals("Choose from Gallery ")) {
                    PDfcheck="PHOTOIMAGE";
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE_MULTIPLE);


                }

                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();

                }

            }

        });

        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            /**
             * *********** Camera Image   ***********
             */
            if (PDfcheck.equalsIgnoreCase("PHOTOIMAGE")) {


                if (requestCode == CAMERA_REQUEST && resultCode == RegisterActivity.this.RESULT_OK) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    Uri jjsdjjd = getImageUri(RegisterActivity.this, bitmap);
                    System.out.println("ImagePathcamera" + jjsdjjd);
                    profile_image.setImageURI(jjsdjjd);
                    selectedPath = FilePath.getPath(RegisterActivity.this,jjsdjjd);
                    pimage ="photo";


                }

                /**
                 * *********** Gallery Image One  ***********
                 */


                if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {

                    if (data.getData() != null) {
                        Uri uri1 = data.getData();
                        selectedPath = FilePath.getPath(RegisterActivity.this,uri1);
                        profile_image.setImageURI(uri1);
                        Log.v("gallerycount", "Selected Images" + uri1);
                        //  mmArrayUri.add(uri1);
                        pimage ="gallery";

                    }


                }
            }


        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * *********** Get Image Url  Method   ***********
     */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        pathsdadda = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        System.out.println("arjunsinghImagePath" + pathsdadda);
        return Uri.parse(pathsdadda);
    }





    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finishActivity();
    }



    private void requestPermission() {
        android.support.v4.app.ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, READ_PHONE_STATE, READ_EXTERNAL_STORAGE}, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    boolean ReadPhoneStatePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStrage = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission  && ReadPhoneStatePermission  && ReadExternalStrage) {

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
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(this, READ_PHONE_STATE);
        int externalstorage = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED  && ThirdPermissionResult == PackageManager.PERMISSION_GRANTED  && externalstorage == PackageManager.PERMISSION_GRANTED;
    }


}


