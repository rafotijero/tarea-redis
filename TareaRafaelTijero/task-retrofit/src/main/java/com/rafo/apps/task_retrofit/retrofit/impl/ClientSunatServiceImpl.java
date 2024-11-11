package com.rafo.apps.task_retrofit.retrofit.impl;

import com.rafo.apps.task_retrofit.utils.Constants;
import lombok.extern.log4j.Log4j2;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Log4j2
public class ClientSunatServiceImpl {

    private static String BASE_URL = Constants.BASE_URL;
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit() {
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            log.info("ClientSunatServiceImpl -> " + Constants.MESSAGE_RETROFIT);
        }
        return retrofit;
    }

}
