package com.gina.takecare4u.providers;

import com.gina.takecare4u.modelos.FCMRBody;
import com.gina.takecare4u.modelos.FCMResponse;
import com.gina.takecare4u.retrofit.IFCMApi;
import com.gina.takecare4u.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https;//fcm.googleapis.com";

    public NotificationProvider() {
    }

    //metodo que retorna call
    public Call<FCMResponse> sendNotification(FCMRBody body){
        return RetrofitClient.getClient(url).create(IFCMApi.class).send(body);

    }
}
