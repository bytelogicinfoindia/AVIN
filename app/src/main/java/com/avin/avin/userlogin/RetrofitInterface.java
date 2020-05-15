package com.avin.avin.userlogin;

import com.avin.avin.server.Result;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @FormUrlEncoded
    @POST("register")
    Call<Result> createUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("gender") String gender);


    @POST("User_login/LoginApi")
    Call<Result> getStringScalar(@Body SendData body);

    /*@POST("User_login/LoginApi")
    Call<Object> getUser(@Body Map<String, String> body);*/
}
