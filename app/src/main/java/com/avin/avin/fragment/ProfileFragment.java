package com.avin.avin.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avin.avin.R;
import com.avin.avin.allactivity.LoginActivity;
import com.avin.avin.server.ApiServer;
import com.avin.avin.server.FilePath;
import com.avin.avin.server.SessionManager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.github.javiersantos.bottomdialogs.BottomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView tv_name;
    ImageView add_image_profile;
    EditText et_name,et_emails,et_mobilenumber,et_address,et_zipcode;

    SessionManager sessionManager;
    HashMap<String, String> user ;
    String  UserId,service_name;
    CircleImageView profile_image;

    Button bt_login;
    BottomDialog bottomDialog;
    SharedPreferences preferences;
    LinearLayout bt_loginhere;
    String PDfcheck,pathsdadda,selectedPath;
    public static final int RequestPermissionCode = 1;
    private final int CAMERA_REQUEST = 20;
    int PICK_IMAGE_MULTIPLE = 100;
    Bitmap bitmap;
    String pimage = "null";
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_profile, container, false);
        tv_name=(TextView)view.findViewById(R.id.tv_name);
        et_name=(EditText) view.findViewById(R.id.et_name);
        et_emails=(EditText) view.findViewById(R.id.et_emails);
        bt_loginhere=(LinearLayout) view.findViewById(R.id.bt_loginhere);
        et_mobilenumber=(EditText) view.findViewById(R.id.et_mobilenumber);
        et_address=(EditText) view.findViewById(R.id.et_address);
        et_zipcode=(EditText) view.findViewById(R.id.et_zipcode);
        bt_login=(Button)view.findViewById(R.id.bt_login);
        profile_image=(CircleImageView) view.findViewById(R.id.profile_image);
        add_image_profile=(ImageView) view.findViewById(R.id.add_image_profile);
        tv_name.setText("Profile");
        add_image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!checkPermission()) {
                    if (sessionManager.isLoggedIn()) {
                        selectImage();

                    }else {
                        Toast.makeText(getActivity(), "Please login here", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    requestPermission();
                }

            }
        });


        et_name.setEnabled(false);
        et_emails.setEnabled(false);
        et_mobilenumber.setEnabled(false);
        et_address.setEnabled(false);
        et_zipcode.setEnabled(false);

        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails();
        UserId=user.get(SessionManager.KEY_ID);
        bt_loginhere.setVisibility(View.VISIBLE);
        if (sessionManager.isLoggedIn()) {

            et_name.setText(user.get(SessionManager.KEY_full_name));
            et_emails.setText(user.get(SessionManager.KEY_email));
            et_mobilenumber.setText(user.get(SessionManager.KEY_mobile));
            et_address.setText(user.get(SessionManager.KEY_address));
            et_zipcode.setText(user.get(SessionManager.KEY_zip_code));

            Glide.with(getActivity()).load(ApiServer.ServerKey.imagefetch+user.get(SessionManager.KEY_user_profile)).placeholder(R.color.placeholder_bg).into(profile_image);

            bt_login.setText("Logout");

            bt_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogOutDialog();
                }
            });



        }else {
            bt_login.setText("Login");
            bt_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                }
            });

        }




            return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
      //  mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void LogOutDialog() {


        bottomDialog = new BottomDialog.Builder(getActivity())
                .setTitle("Are you sure you want to logout?")
                .setPositiveText("YES")
                .setCancelable(false)
                .setPositiveBackgroundColorResource(R.color.colorPrimary)
                .setPositiveTextColorResource(android.R.color.white)
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(BottomDialog dialog) {
                        bottomDialog.dismiss();
                        sessionManager.logoutUser();
                        getActivity().finish();
                        getActivity(). overridePendingTransition(R.anim.translation2, R.anim.translation);
                        preferences = getActivity().getSharedPreferences("CPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();

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


    /**
     * *********** Dialog  Image,PDF and Video Method   **********
     */


    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery ","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
            if (PDfcheck.equalsIgnoreCase("PHOTOIMAGE")) {


                if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    Uri jjsdjjd = getImageUri(getActivity(), bitmap);
                    System.out.println("ImagePathcamera" + jjsdjjd);
                    profile_image.setImageURI(jjsdjjd);
                    selectedPath = FilePath.getPath(getActivity(),jjsdjjd);
                    pimage ="photo";
                    ProfileMethod();


                }

                if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {

                    if (data.getData() != null) {
                        Uri uri1 = data.getData();
                        selectedPath = FilePath.getPath(getActivity(),uri1);
                        profile_image.setImageURI(uri1);
                        Log.v("gallerycount", "Selected Images" + uri1);
                        //  mmArrayUri.add(uri1);
                        pimage ="gallery";
                        ProfileMethod();

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

    private void requestPermission() {
        android.support.v4.app.ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, READ_PHONE_STATE, READ_EXTERNAL_STORAGE}, RequestPermissionCode);

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

                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        //  Toast.makeText(LoginActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }


    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getActivity(), READ_PHONE_STATE);
        int externalstorage = ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED  && ThirdPermissionResult == PackageManager.PERMISSION_GRANTED  && externalstorage == PackageManager.PERMISSION_GRANTED;
    }


    private void ProfileMethod() {



        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait.");
        dialog.setCancelable(false);
        dialog.show();

        File  file = new File(selectedPath);
         AndroidNetworking.upload(ApiServer.ServerKey.ChangeProfileApi_Api)
                .addMultipartParameter(ApiServer.ApiParams.User_id,UserId)
                .addMultipartFile(ApiServer.ApiParams.User_profile,file)
                .setTag("Login")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ChangeProfileApi_Api"+response);
                        dialog.dismiss();

                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")){
                                String message=jsonObject.getString("message");
                                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                String id=jsonObject1.getString("id");
                                String user_profile=jsonObject1.getString("user_profile");


                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                Glide.with(getActivity()).load(ApiServer.ServerKey.imagefetch+user_profile).placeholder(R.color.placeholder_bg).into(profile_image);

                                sessionManager.createLoginSession(user.get(SessionManager.KEY_ID),user.get(SessionManager.KEY_full_name),
                                        user.get(SessionManager.KEY_email),user.get(SessionManager.KEY_mobile),user_profile,
                                        user.get(SessionManager.KEY_password),user.get(SessionManager.KEY_address),user.get(SessionManager.KEY_zip_code));


                            }
                            else {
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println("forgotresError"+anError.toString());
                        Toast.makeText(getActivity(), "Allready Register", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

    }


}
