package com.soulkey.mdlm.Model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitService {
    @POST("db/")
    Call<JsonElement> Insert_Query(@Body JsonObject jsonObject);

    @GET("trends/hrm/")
    Call<JsonElement> CallData_HRM();

    @GET("trends/bp/")
    Call<JsonElement> CallData_BP();

    @GET("trends/dd/")
    Call<JsonElement> CallData_DD();
}
