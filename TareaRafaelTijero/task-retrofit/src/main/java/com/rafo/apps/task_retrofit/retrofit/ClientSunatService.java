package com.rafo.apps.task_retrofit.retrofit;

import com.rafo.apps.task_retrofit.response.ResponseSUNAT;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ClientSunatService {

    @GET("v2/sunat/ruc/full")
    Call<ResponseSUNAT> getInfoSunat(@Header("Authorization") String token,
                                     @Query("numero") String numero);

}
