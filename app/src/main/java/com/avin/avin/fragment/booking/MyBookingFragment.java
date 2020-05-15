package com.avin.avin.fragment.booking;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.avin.avin.R;
import com.avin.avin.allactivity.ReschedulsActivity;
import com.avin.avin.model.BookingModel;
import com.avin.avin.server.ApiServer;
import com.avin.avin.server.SessionManager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.github.javiersantos.bottomdialogs.BottomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyBookingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyBookingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MyBookingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyBookingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyBookingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyBookingFragment newInstance(String param1, String param2) {
        MyBookingFragment fragment = new MyBookingFragment();
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
    ArrayList<BookingModel> bookingModels=new ArrayList<>();
    BookingModel bookingModel;
    BookingAdapter bookingAdapter;
    LinearLayoutManager listinlinear;
    RecyclerView bookinrecylervie;
    TextView tv_name;
    FragmentTransaction transaction;
    BottomDialog bottomDialog;
    SessionManager sessionManager;
    HashMap<String, String> user ;
    String  UserId,service_name;
    ProgressDialog dialog;
    SwipeRefreshLayout sw_refershview;
    TextView tv_nobanner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my_booking, container, false);
        bookinrecylervie=(RecyclerView)view.findViewById(R.id.bookinrecylervie);
        tv_name=(TextView)view.findViewById(R.id.tv_name);
        tv_nobanner=(TextView)view.findViewById(R.id.tv_nobanner);
        sw_refershview = (SwipeRefreshLayout) view.findViewById(R.id.sw_refershview);
        tv_name.setText("Booking");

        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails();
        UserId=user.get(SessionManager.KEY_ID);
        System.out.println("Responcetocheck"+UserId);

        sw_refershview.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        sw_refershview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {
                    sw_refershview.setRefreshing(true);
                    if (sessionManager.isLoggedIn()) {
                        GetBookingDetails();
                        bookingAdapter.listclean();
                        bookingModels.clear();
                        bookingAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getActivity(), "Please login here", Toast.LENGTH_SHORT).show();
                    }


                }catch (NullPointerException e){
                    e.fillInStackTrace();
                }


            }

        });

        sw_refershview.post(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isLoggedIn()) {
                    GetBookingDetails();
                }else {
                    Toast.makeText(getActivity(), "Please login here", Toast.LENGTH_SHORT).show();
                }


            }
        });


       return view;
   }

    private void GetBookingDetails() {

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait.");
        dialog.setCancelable(false);
        dialog.show();
        AndroidNetworking.post(ApiServer.ServerKey.AllBookingrApi_Api)
                .addBodyParameter(ApiServer.ApiParams.User_id,UserId)
                .setTag("Login")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        if (!bookingModels.isEmpty()){
                            bookingModels.clear();
                        }
                        System.out.println("BookingList"+response);

                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")){
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bookingModel=new BookingModel();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    bookingModel.setId(jsonObject1.getString("id"));
                                    bookingModel.setName(jsonObject1.getString("name"));
                                    bookingModel.setUser_id(jsonObject1.getString("user_id"));
                                    bookingModel.setEmail(jsonObject1.getString("email"));
                                    bookingModel.setPhone(jsonObject1.getString("phone"));
                                    bookingModel.setAddress(jsonObject1.getString("address"));
                                    bookingModel.setZip_code(jsonObject1.getString("zip_code"));
                                    bookingModel.setService_id(jsonObject1.getString("service_id"));
                                    bookingModel.setService_name(jsonObject1.getString("service_name"));
                                    bookingModel.setDate(jsonObject1.getString("date"));
                                    bookingModel.setTime(jsonObject1.getString("time"));
                                    bookingModel.setStatus(jsonObject1.getString("status"));
                                    bookingModel.setOrderno(jsonObject1.getString("orderno"));
                                    bookingModels.add(bookingModel);
                                    tv_nobanner.setText(" ");
                                }

                                listinlinear = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                bookinrecylervie.setLayoutManager(listinlinear);
                                bookingAdapter = new BookingAdapter(getActivity(), bookingModels);
                                bookinrecylervie.setAdapter(bookingAdapter);
                            }
                            else {
                                tv_nobanner.setText("No Booking");
                                //Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void OnmethodCall(String id,String service_name){

        Bundle bundle=new Bundle();

        ViewBookingFragment homeFragment=new ViewBookingFragment();
        transaction = getFragmentManager().beginTransaction();
        bundle.putString("ID",id);
        bundle.putString("Servicename",service_name);
        transaction.replace(R.id.frame, homeFragment);
        transaction.addToBackStack(null);
        homeFragment.setArguments(bundle);
        transaction.commit();


    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
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

    public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder>{

        ArrayList<BookingModel> bookingModels;
        Context context;


        public BookingAdapter(Context context,ArrayList<BookingModel> bookingModels) {
            this.bookingModels = bookingModels;
            this.context = context;
        }

        @Override
        public BookingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.bookinglist, parent, false);
           ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final BookingAdapter.ViewHolder holder, final int position) {

            holder.tv_creatorder.setText("Order No - #"+bookingModels.get(position).getOrderno());
            holder.txtview_servicename.setText("Service Name - "+bookingModels.get(position).getService_name());
            holder.tv_status.setText("Status  - "+bookingModels.get(position).getStatus());
            holder.tv_date.setText("Date - "+bookingModels.get(position).getDate()+" : "+ bookingModels.get(position).getTime());


             holder.bt_cancel.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     PopupMenu popup = new PopupMenu(getActivity(), v);
                     popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());

                     popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                         public boolean onMenuItemClick(MenuItem item) {
                             if (item.getTitle().equals("Cancel")){

                                 CancelDialog(position,bookingModels.get(position).getId());


                             }else {
                                          Intent intent=new Intent(context, ReschedulsActivity.class);
                                          intent.putExtra("ID",bookingModels.get(position).getId());
                                          startActivity(intent);
                             }

                             return true;
                         }
                     });

                     popup.show();//showing popup menu
                 }
             });
        }

        private void CancelDialog(final int positions, final String Id) {


          bottomDialog = new BottomDialog.Builder(context)
                    .setTitle("Are you sure you want to Cancel Your Service?")
                    .setPositiveText("YES")
                    .setCancelable(false)
                    .setPositiveBackgroundColorResource(R.color.colorPrimary)
                    .setPositiveTextColorResource(android.R.color.white)
                    .onPositive(new BottomDialog.ButtonCallback() {
                        @Override
                        public void onClick(BottomDialog dialog) {

                            bottomDialog.dismiss();
                            CancelMethod(positions,Id);


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

        private void CancelMethod(final int position, String id) {
            try {


                  dialog = new ProgressDialog(context);
                    dialog.setMessage("Please wait.");
                    dialog.setCancelable(false);
                    dialog.show();
                    AndroidNetworking.post(ApiServer.ServerKey.BookingCancelDeleteApi_Api)
                            .addBodyParameter(ApiServer.ApiParams.ID,id)
                            .setTag("Login")
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();

                                    System.out.println("cancelsresponce"+response);
                                    try {

                                        JSONObject jsonObject=new JSONObject(response);
                                        String status=jsonObject.getString("status");
                                        if (status.equalsIgnoreCase("1")){
                                            String message=jsonObject.getString("message");
                                            removeAt(position);
                                            Toast.makeText(context, "Successfully Cancel your services", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

        public void removeAt(int position) {
            bookingModels.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, bookingModels.size());
        }

        public void listclean(){
            if (!bookingModels.isEmpty()) {
                bookingModels.clear();
            }

        }

        @Override
        public int getItemCount() {
            return bookingModels.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_creatorder,txtview_servicename,tv_date,tv_status;
            LinearLayout viewliner;
            ImageView bt_cancel;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_creatorder = (TextView) itemView.findViewById(R.id.tv_creatorder);
                txtview_servicename = (TextView) itemView.findViewById(R.id.txtview_servicename);
                tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                viewliner = (LinearLayout) itemView.findViewById(R.id.viewliner);
                bt_cancel = (ImageView) itemView.findViewById(R.id.bt_cancel);
                tv_status = (TextView) itemView.findViewById(R.id.tv_status);
                viewliner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnmethodCall(bookingModels.get(getAdapterPosition()).getId(),bookingModels.get(getAdapterPosition()).getService_name());
                    }
                });


            }
        }
    }
}
