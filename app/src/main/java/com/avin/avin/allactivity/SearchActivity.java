package com.avin.avin.allactivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avin.avin.R;
import com.avin.avin.model.SearchModel;
import com.avin.avin.server.ApiServer;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    EditText et_namesearch;
    RecyclerView recyclerview;
    ProgressDialog dialog;
    SearchModel searchModel;
    ArrayList<SearchModel> searchModels =new ArrayList<>();;
    SearchAdapter searchAdapter;
    ProgressBar progressbar;
    TextView tv_nobanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().hide();
        getSupportActionBar().hide();
        et_namesearch=(EditText)findViewById(R.id.et_namesearch);
        progressbar=(ProgressBar)findViewById(R.id.progressbar);
        recyclerview=(RecyclerView)findViewById(R.id.recyclerview);
        tv_nobanner=(TextView) findViewById(R.id.tv_nobanner);
        et_namesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        et_namesearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!searchModels.isEmpty()) {
                    searchModels.clear();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!searchModels.isEmpty()) {
                    searchModels.clear();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (!editable.toString().isEmpty()){
                        System.out.println("countwordaeditable"+editable);
                        GetSearchMethodApi(editable.toString());
                    }
                    else  {

                        searchModels.clear();
                        searchAdapter.listclean();
                        searchAdapter.notifyDataSetChanged();
                        tv_nobanner.setText(" ");
                    }

                } catch (NullPointerException e) {
                    e.fillInStackTrace();
                }
            }
        });


    }


    private void GetSearchMethodApi(String name) {




                progressbar.setVisibility(View.VISIBLE);
                AndroidNetworking.post(ApiServer.ServerKey.SearchItemsApi_Api)
                        .addBodyParameter(ApiServer.ApiParams.Name,name)
                        .setTag(this)
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("subbannerdata"+response);
                                progressbar.setVisibility(View.GONE);
                                if (!searchModels.isEmpty()) {
                                    searchModels.clear();
                                }

                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    String status=jsonObject.getString("status");
                                    if (status.equalsIgnoreCase("1")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            searchModel=new SearchModel();
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            searchModel.setId(jsonObject1.getString("id"));
                                            searchModel.setTitle(jsonObject1.getString("title"));
                                            searchModel.setSub_title(jsonObject1.getString("sub_title"));
                                            searchModel.setImages(jsonObject1.getString("images"));

                                            searchModels.add(searchModel);

                                        }


                                        searchAdapter = new SearchAdapter(SearchActivity.this,searchModels);
                                        recyclerview.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
                                        recyclerview.setAdapter(searchAdapter);
                                        recyclerview.setNestedScrollingEnabled(false);
                                        tv_nobanner.setText(" ");

                                    }else {
                                        tv_nobanner.setText("No Service Available");
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                System.out.println("subbannerdataanError"+anError);
                                progressbar.setVisibility(View.GONE);

                            }
                        });




    }

    public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

        ArrayList<SearchModel> searchModels;
        Context context;


        public SearchAdapter(Context context, ArrayList<SearchModel> searchModels) {
            this.searchModels = searchModels;
            this.context = context;
        }

        @Override
        public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.searchlist, parent, false);
            SearchAdapter.ViewHolder viewHolder = new SearchAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final SearchAdapter.ViewHolder holder, int position) {

            holder.tv_titlenamesearch.setText(searchModels.get(position).getTitle());
            Glide.with(context).load(ApiServer.ServerKey.imagefetch+searchModels.get(position).getImages()).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .placeholder(R.color.placeholder_bg)
                    .error(R.color.placeholder_bg)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.tv_progressbar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.tv_progressbar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.imageesearch);

        }


        @Override
        public int getItemCount() {
            return searchModels.size();
        }
        public void listclean(){
            if (!searchModels.isEmpty()) {
                searchModels.clear();
            }

        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageesearch;
            ProgressBar tv_progressbar;
            TextView tv_titlenamesearch;

            public ViewHolder(View itemView) {
                super(itemView);
                imageesearch = (ImageView) itemView.findViewById(R.id.su_imageview);
                tv_progressbar = (ProgressBar) itemView.findViewById(R.id.tv_progressbar);
                tv_titlenamesearch = (TextView) itemView.findViewById(R.id.tv_subename);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* Intent intent=new Intent(context, AllServiceActivity.class);
                        intent.putExtra("servicename",searchModels.get(getAdapterPosition()).getTitle());
                        intent.putExtra("Id",searchModels.get(getAdapterPosition()).getId());
                        context.startActivity(intent);*/
                        Intent intent=new Intent(context, BookingActivity.class);
                        intent.putExtra("servicename",searchModels.get(getAdapterPosition()).getTitle());
                        intent.putExtra("Id",searchModels.get(getAdapterPosition()).getId());
                        /*intent.putExtra("Idone",Id);
                        intent.putExtra("servicenameone",servicename);*/
                        context.startActivity(intent);
                    }
                });

            }
        }
    }
}
