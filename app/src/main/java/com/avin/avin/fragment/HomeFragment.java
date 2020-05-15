package com.avin.avin.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avin.avin.R;
import com.avin.avin.allactivity.BookingActivity;
import com.avin.avin.allactivity.SearchActivity;
import com.avin.avin.model.BannnerList;
import com.avin.avin.model.HomeListing;
import com.avin.avin.server.ApiServer;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    //header listinh
    BannaarAdapter bannaarAdapter;
    LinearLayoutManager manager;
    private RecyclerView headerlistmore;
    ArrayList<BannnerList> bannnerLists=new ArrayList<>();
    BannnerList bannnerList;


    /// Listing List

  //  private Listingdapter mAdapter;


    ListingAdapter ListinAdapter;
    LinearLayoutManager listinlinear;
    ArrayList<HomeListing> listings=new ArrayList<>();
    HomeListing homeListing;
    ProgressDialog dialog;
    private RecyclerView verticals;
    LinearLayout bt_search;
    TextView tv_nobanner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_home, container, false);

        verticals = (RecyclerView)view. findViewById(R.id.verticals);
        bt_search = (LinearLayout) view. findViewById(R.id.bt_search);
        headerlistmore = (RecyclerView)view .findViewById(R.id.headerlistmore);
        tv_nobanner = (TextView) view .findViewById(R.id.tv_nobanner);


        getBannerMethod();
        getMethod();


        bt_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return false;
            }
        });


     return view;
    }

    private void getBannerMethod() {


        AndroidNetworking.get(ApiServer.ServerKey.Banner_Api)
                .setTag("Login")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                     System.out.println("Bannerimage"+response);

                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")){
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    bannnerList=new BannnerList();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    bannnerList.setId(jsonObject1.getString("id"));
                                    bannnerList.setTitle1(jsonObject1.getString("title1"));
                                    bannnerList.setSub_title1(jsonObject1.getString("sub_title1"));
                                    bannnerList.setSlide1(jsonObject1.getString("slide1"));

                                    bannnerLists.add(bannnerList);

                                }


                                manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                                headerlistmore.setLayoutManager(manager);
                                bannaarAdapter = new BannaarAdapter(getActivity(), bannnerLists);
                                headerlistmore.setAdapter(bannaarAdapter);
                                headerlistmore.setNestedScrollingEnabled(false);

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

                    }
                });

    }

    private void getMethod() {



        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait.");
        dialog.setCancelable(false);
        dialog.show();


        AndroidNetworking.get(ApiServer.ServerKey.ListingApi_Api)
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
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    homeListing=new HomeListing();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    homeListing.setId(jsonObject1.getString("id"));
                                    homeListing.setTitle(jsonObject1.getString("title"));
                                    homeListing.setSub_title(jsonObject1.getString("sub_title"));
                                    homeListing.setImages(jsonObject1.getString("images"));

                                    listings.add(homeListing);
                                    tv_nobanner.setText(" ");
                                }


                                listinlinear = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                verticals.setLayoutManager(listinlinear);
                                ListinAdapter = new ListingAdapter(getActivity(), listings);
                                verticals.setAdapter(ListinAdapter);
                                verticals.setNestedScrollingEnabled(false);
                            }
                            else {
                                tv_nobanner.setText("No Service Available");
                              //  Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println("forgotresError"+anError.toString());
                      //  Toast.makeText(getActivity(), "Allready Register", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
    /// header list

    public class BannaarAdapter extends RecyclerView.Adapter<BannaarAdapter.ViewHolder>{

        ArrayList<BannnerList> bannnerLists;
        Context context;


        public BannaarAdapter(Context context,ArrayList<BannnerList> bannnerLists) {
            this.bannnerLists = bannnerLists;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.bannerimage, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

           // holder.tv_banner.setText(bannnerLists.get(position).getTitle1());
            Glide.with(getActivity()).load(ApiServer.ServerKey.imagefetch+bannnerLists.get(position).getSlide1()).placeholder(R.color.placeholder_bg).into(holder.home1);




        }


        @Override
        public int getItemCount() {
            return bannnerLists.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            ImageView home1;
            ProgressBar tv_progressbar;
            TextView tv_banner;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_banner = (TextView) itemView.findViewById(R.id.tv_banner);
                home1 = (ImageView) itemView.findViewById(R.id.home1);
               /* itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, AllServiceActivity.class);
                        intent.putExtra("servicename",bannnerLists.get(getAdapterPosition()).getTitle1());
                        context.startActivity(intent);
                    }
                });*/


            }
        }
    }


    public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder>{

        ArrayList<HomeListing> homeListings;
        Context context;


        public ListingAdapter(Context context, ArrayList<HomeListing> homeListings) {
            this.homeListings = homeListings;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.homemenu, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.tv_titlename.setText(homeListings.get(position).getTitle());
            holder.tv_subtitle.setText(homeListings.get(position).getSub_title());

            Glide.with(getActivity()).load(ApiServer.ServerKey.imagefetch+homeListings.get(position).getImages()).placeholder(R.color.placeholder_bg).into(holder.imageview);


        }


        @Override
        public int getItemCount() {
            return homeListings.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageview;
            ProgressBar tv_progressbar;
            TextView tv_titlename,tv_subtitle;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_titlename = (TextView) itemView.findViewById(R.id.tv_titlename);
                tv_subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
                imageview = (ImageView) itemView.findViewById(R.id.imageview);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Intent intent=new Intent(context, AllServiceActivity.class);
                        intent.putExtra("servicename",homeListings.get(getAdapterPosition()).getTitle());
                        intent.putExtra("Id",homeListings.get(getAdapterPosition()).getId());
                        context.startActivity(intent);*/

                        Intent intent=new Intent(context, BookingActivity.class);
                        intent.putExtra("servicename",homeListings.get(getAdapterPosition()).getTitle());
                        intent.putExtra("Id",homeListings.get(getAdapterPosition()).getId());
                        /*intent.putExtra("Idone",Id);
                        intent.putExtra("servicenameone",servicename);*/
                        context.startActivity(intent);
                       // finishActivity();
                    }
                });


            }
        }
    }



}
