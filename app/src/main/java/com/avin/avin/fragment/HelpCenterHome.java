package com.avin.avin.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avin.avin.R;
import com.avin.avin.allactivity.ChangePasswordActivity;
import com.avin.avin.allactivity.ContactUsActivity;
import com.avin.avin.allactivity.EditProfileActivity;
import com.avin.avin.allactivity.TermsPolicyActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HelpCenterHome.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HelpCenterHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpCenterHome extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HelpCenterHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpCenterHome.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpCenterHome newInstance(String param1, String param2) {
        HelpCenterHome fragment = new HelpCenterHome();
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
    Intent intent;
    TextView tv_name;
    RelativeLayout re_aacountseting,re_changepassword,re_termsuse,re_privacypolicy,re_contactUs,re_download,bt_sharingApp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_help_center_home, container, false);
        tv_name=(TextView)view.findViewById(R.id.tv_name);
        re_aacountseting=(RelativeLayout)view.findViewById(R.id.re_aacountseting);
        re_changepassword=(RelativeLayout)view.findViewById(R.id.re_changepassword);
        re_termsuse=(RelativeLayout)view.findViewById(R.id.re_termsuse);
        re_privacypolicy=(RelativeLayout)view.findViewById(R.id.re_privacypolicy);
        re_contactUs=(RelativeLayout)view.findViewById(R.id.re_contactUs);
        re_download=(RelativeLayout)view.findViewById(R.id.re_download);
        bt_sharingApp=(RelativeLayout)view.findViewById(R.id.bt_sharingApp);
        tv_name.setText("Help Center");
        re_aacountseting.setOnClickListener(this);
        re_changepassword.setOnClickListener(this);
        re_termsuse.setOnClickListener(this);
        re_privacypolicy.setOnClickListener(this);
        re_aacountseting.setOnClickListener(this);
        re_contactUs.setOnClickListener(this);
        re_download.setOnClickListener(this);
        bt_sharingApp.setOnClickListener(this);
   return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
      /*  if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
     //   mListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.re_aacountseting:
                 intent=new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.re_changepassword:
                intent=new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.re_termsuse:
                intent=new Intent(getActivity(), TermsPolicyActivity.class);
                intent.putExtra("Name","Terms of Use");
                startActivity(intent);
                break;

            case R.id.re_privacypolicy:
                intent=new Intent(getActivity(), TermsPolicyActivity.class);
                intent.putExtra("Name","Privacy Policy");
                startActivity(intent);
                break;
            case R.id.re_contactUs:
                intent=new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
                break;
            case R.id.re_download:
                try{

                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.avin.avin"));
                    startActivity(myIntent);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "No application can handle this request."
                            + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                break;
            case R.id.bt_sharingApp:

                try{


                    Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
                    String app_url = " \n" +
                            "   https://play.google.com/store/apps/details?id=com.avin.avin  ";
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
                    startActivity(Intent.createChooser(shareIntent, "Share via"));

                }catch (ActivityNotFoundException e){
                    e.fillInStackTrace();
                }

                break;

            default:
                break;
        }

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
