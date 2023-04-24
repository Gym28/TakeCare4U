package com.gina.takecare4u.retrofit;

import com.gina.takecare4u.modelos.FCMRBody;
import com.gina.takecare4u.modelos.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {
    //headers
    @Headers({
            "Content-Type:application/json",
            "Autorization:key=AAAA1qevfp0:APA91bE1ht05SX8HM9cth6F2hUxmfnSmncCHt82Mce_cooELqhmvvEbFcY42qIH0zJUg1t0jCGuHs6CdoRJHj-qYEwLqa4IIJuFZ3DPbJymoL-0ZXxjHo4DCxbztFRDf6MSBe5m0MIAb"
    })

    //tipo de notificación
    @POST("fcm/send")
    Call <FCMResponse> send(@Body FCMRBody body);

}
