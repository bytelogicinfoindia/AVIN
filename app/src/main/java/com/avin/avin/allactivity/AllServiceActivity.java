package com.avin.avin.allactivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avin.avin.R;
import com.avin.avin.model.AllListingModel;
import com.avin.avin.otherclass.BaseActivity;
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

public class AllServiceActivity extends BaseActivity {
    String servicename,Id;
    ArrayList<AllListingModel> listingModels=new ArrayList<>();
    AllListingModel allListingModel;
    ListingAdapter ListinAdapter;
    LinearLayoutManager listinlinear;
    RecyclerView tv_servicename;
    ImageButton back;
    TextView tv_name;
    ProgressDialog dialog;
    TextView tv_nobanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_service);
        servicename=getIntent().getStringExtra("servicename");
        Id=getIntent().getStringExtra("Id");
        tv_nobanner=(TextView) findViewById(R.id.tv_nobanner);
        System.out.println("CheckIdname"+Id+servicename);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
       // getSupportActionBar().setTitle(name);
        getSupportActionBar().hide();
        back=(ImageButton)findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        tv_name=(TextView) findViewById(R.id.tv_name);
        tv_servicename=(RecyclerView) findViewById(R.id.tv_servicename);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        tv_name.setText(servicename);

        getMethod(Id);


    }

    private void getMethod(String id) {



        dialog = new ProgressDialog(AllServiceActivity.this);
        dialog.setMessage("Please wait.");
        dialog.setCancelable(false);
        dialog.show();


        AndroidNetworking.post(ApiServer.ServerKey.Category_SubApi_Api)
                .addBodyParameter(ApiServer.ApiParams.sub_cat,id)
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
                                    allListingModel=new AllListingModel();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    allListingModel.setId(jsonObject1.getString("id"));
                                    allListingModel.setTitle(jsonObject1.getString("title"));
                                    allListingModel.setSub_cat(jsonObject1.getString("sub_cat"));
                                    allListingModel.setImages(jsonObject1.getString("images"));
                                    tv_nobanner.setText(" ");
                                    listingModels.add(allListingModel);

                                }


                                listinlinear = new LinearLayoutManager(AllServiceActivity.this, LinearLayoutManager.VERTICAL, false);
                                tv_servicename.setLayoutManager(listinlinear);
                                ListinAdapter = new ListingAdapter(AllServiceActivity.this, listingModels);
                                tv_servicename.setAdapter(ListinAdapter);
                            }
                            else {
                                tv_nobanner.setText("No Service Available");
                              //  Toast.makeText(AllServiceActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println("forgotresError"+anError.toString());
                        Toast.makeText(AllServiceActivity.this, "Allready Register", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

    }

    public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder>{

        ArrayList<AllListingModel> allListingModels;
        Context context;


        public ListingAdapter(Context context,ArrayList<AllListingModel> allListingModels) {
            this.allListingModels = allListingModels;
            this.context = context;
        }

        @Override
        public ListingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.sub_menu, parent, false);
           ListingAdapter.ViewHolder viewHolder = new ListingAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ListingAdapter.ViewHolder holder, int position) {

            holder.tv_subename.setText(allListingModels.get(position).getTitle());
            Glide.with(context).load(ApiServer.ServerKey.imagefetch+allListingModels.get(position).getImages()).placeholder(R.color.placeholder_bg).into(holder.su_imageview);



        }


        @Override
        public int getItemCount() {
            return allListingModels.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            ImageView su_imageview;
            ProgressBar tv_progressbar;
            TextView tv_subename;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_subename = (TextView) itemView.findViewById(R.id.tv_subename);
                su_imageview = (ImageView) itemView.findViewById(R.id.su_imageview);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, BookingActivity.class);
                        intent.putExtra("servicename",allListingModels.get(getAdapterPosition()).getTitle());
                        intent.putExtra("Id",allListingModels.get(getAdapterPosition()).getId());
                        intent.putExtra("Idone",Id);
                        intent.putExtra("servicenameone",servicename);
                        context.startActivity(intent);
                        finishActivity();
                    }
                });


            }
        }
    }
}
