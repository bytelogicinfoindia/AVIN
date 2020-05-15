package com.avin.avin.fragment.booking;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avin.avin.R;
import com.avin.avin.server.ApiServer;
import com.avin.avin.server.SessionManager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewBookingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewBookingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewBookingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ViewBookingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewBookingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewBookingFragment newInstance(String param1, String param2) {
        ViewBookingFragment fragment = new ViewBookingFragment();
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
    ProgressDialog dialog;

    TextView tv_fullnamehits,tv_hitsfathername,tv_hitsmothername,tv_hitsmobileno,tv_hitsemail,tv_hitsgender,tv_hitesdob,tv_hitsaddress,tv_hitcity,tv_hitamount;

    TextView tv_v1,tv_v2,tv_v3,tv_v4,tv_v5,tv_v6,tv_v7,tv_v8,tv_v9,tv_v10,tv_v11,tv_v12;
    TextView tv_fullnamehits1,tv_hitsfathername1,tv_hitsmothername1,tv_hitsmobileno1,tv_hitsemail1,tv_hitsgender1,tv_hitesdob1,tv_hitsaddress1,tv_hitcity1,tv_hitamount1,tv_time,tv_time1;
    TextView tv_name;
    String ID,Servicename,UserId;
    SessionManager sessionManager;
    HashMap<String, String> user ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_view_booking, container, false);

        tv_name=(TextView)view.findViewById(R.id.tv_name);
        tv_fullnamehits=(TextView)view.findViewById(R.id.tv_fullnamehits);
        tv_hitsfathername=(TextView)view.findViewById(R.id.tv_hitsfathername);
        tv_hitsmothername=(TextView)view.findViewById(R.id.tv_hitsmothername);
        tv_hitsmobileno=(TextView)view.findViewById(R.id.tv_hitsmobileno);
        tv_hitsemail=(TextView)view.findViewById(R.id.tv_hitsemail);
        tv_hitsgender=(TextView)view.findViewById(R.id.tv_hitsgender);
        tv_hitesdob=(TextView)view.findViewById(R.id.tv_hitesdob);
        tv_hitsaddress=(TextView)view.findViewById(R.id.tv_hitsaddress);
        tv_hitcity=(TextView)view.findViewById(R.id.tv_hitcity);
        tv_hitamount=(TextView)view.findViewById(R.id.tv_hitamount);
        tv_name=(TextView)view.findViewById(R.id.tv_name);
        tv_time=(TextView)view.findViewById(R.id.tv_time);
        ID=getArguments().getString("ID");
        Servicename=getArguments().getString("Servicename");
        tv_name.setText(Servicename);


        tv_fullnamehits1=(TextView)view.findViewById(R.id.tv_fullnamehits1);
        tv_hitsfathername1=(TextView)view.findViewById(R.id.tv_hitsfathername1);
        tv_hitsmothername1=(TextView)view.findViewById(R.id.tv_hitsmothername1);
        tv_hitsmobileno1=(TextView)view.findViewById(R.id.tv_hitsmobileno1);
        tv_hitsemail1=(TextView)view.findViewById(R.id.tv_hitsemail1);
        tv_hitsgender1=(TextView)view.findViewById(R.id.tv_hitsgender1);
        tv_hitesdob1=(TextView)view.findViewById(R.id.tv_hitesdob1);
        tv_hitsaddress1=(TextView)view.findViewById(R.id.tv_hitsaddress1);
        tv_hitcity1=(TextView)view.findViewById(R.id.tv_hitcity1);
        tv_hitamount1=(TextView)view.findViewById(R.id.tv_hitamount1);
        tv_time1=(TextView)view.findViewById(R.id.tv_time1);

        tv_v1=(TextView)view.findViewById(R.id.tv_v1);
        tv_v2=(TextView)view.findViewById(R.id.tv_v2);
        tv_v3=(TextView)view.findViewById(R.id.tv_v3);
        tv_v4=(TextView)view.findViewById(R.id.tv_v4);
        tv_v5=(TextView)view.findViewById(R.id.tv_v5);
        tv_v6=(TextView)view.findViewById(R.id.tv_v6);
        tv_v7=(TextView)view.findViewById(R.id.tv_v7);
        tv_v8=(TextView)view.findViewById(R.id.tv_v8);
        tv_v9=(TextView)view.findViewById(R.id.tv_v9);
        tv_v10=(TextView)view.findViewById(R.id.tv_v10);
        tv_v12=(TextView)view.findViewById(R.id.tv_v12);



        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails();
        UserId=user.get(SessionManager.KEY_ID);
        System.out.println("Responcetocheck"+UserId);


        if (sessionManager.isLoggedIn()) {
            GetViewDetails(ID);

        }else {
            Toast.makeText(getActivity(), "Please Login Here", Toast.LENGTH_SHORT).show();
        }


       return view;
    }

    private void GetViewDetails(String ID) {

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait.");
        dialog.setCancelable(false);
        dialog.show();
        AndroidNetworking.post(ApiServer.ServerKey.BookingViewApi_Api_Api)
                .addBodyParameter(ApiServer.ApiParams.ID,ID)
                .setTag("Login")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        System.out.println("BookingListView"+response);

                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")){
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    String id=jsonObject1.getString("id");
                                    String name=jsonObject1.getString("name");
                                    String user_id=jsonObject1.getString("user_id");
                                    String email=jsonObject1.getString("email");
                                    String phone=jsonObject1.getString("phone");
                                    String address=jsonObject1.getString("address");
                                    String zip_code=jsonObject1.getString("zip_code");
                                    String service_id=jsonObject1.getString("service_id");
                                    String service_name=jsonObject1.getString("service_name");
                                    String date=jsonObject1.getString("date");
                                    String time=jsonObject1.getString("time");
                                    String status1=jsonObject1.getString("status");
                                    String orderno=jsonObject1.getString("orderno");


                                    tv_v1.setText("-");
                                    tv_v2.setText("-");
                                    tv_v3.setText("-");
                                    tv_v4.setText("-");
                                    tv_v5.setText("-");
                                    tv_v6.setText("-");
                                    tv_v7.setText("-");
                                    tv_v8.setText("-");
                                    tv_v9.setText("-");
                                    tv_v10.setText("-");
                                    tv_v12.setText("-");

                                    tv_fullnamehits.setText("Full Name");
                                    tv_hitsfathername.setText("Email");
                                    tv_hitsmothername.setText("Mobile Number");
                                    tv_hitsmobileno.setText("address");
                                    tv_hitsemail.setText("Zip Code");
                                    tv_hitsgender.setText("Order No");
                                    tv_hitesdob.setText("Date");
                                    tv_hitsaddress.setText("Status");
                                    tv_hitamount.setText("Amount");
                                    tv_time.setText("Time");


                                    tv_fullnamehits1.setText(name);
                                    tv_hitsfathername1.setText(email);
                                    tv_hitsmothername1.setText(phone);
                                    tv_hitsmobileno1.setText(address);
                                    tv_hitsemail1.setText(zip_code);
                                    tv_hitsgender1.setText("#"+orderno);
                                    // String[] splitStr = created_on.split("\\s+");
                                    // tv_hitesdob1.setText(splitStr[0]);
                                    tv_hitesdob1.setText(date);
                                    tv_hitsaddress1.setText(status1);
                                    tv_hitamount1.setText("Rs."+"50");
                                    tv_time1.setText(time);


                                }

                            }
                            else {
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
       /* if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
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
}
