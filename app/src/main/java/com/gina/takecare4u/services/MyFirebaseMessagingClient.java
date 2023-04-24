package com.gina.takecare4u.services;

import androidx.core.app.NotificationCompat;

import com.gina.takecare4u.channel.NotificationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingClient extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);

        Map<String, String> data= message.getData();
        String title = data.get("title");
        String body = data.get("body");

        if(title!=null){
            showNotifications(title, body);

        }
    }

    private void showNotifications(String title, String body){
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotification(title, body);
        notificationHelper.getManager().notify(1, builder.build());

    }

}
