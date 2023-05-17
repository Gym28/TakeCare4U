package com.gina.takecare4u.services;

import androidx.core.app.NotificationCompat;

import com.gina.takecare4u.channel.NotificationHelper;
import com.gina.takecare4u.modelos.Messages;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;
import java.util.Random;

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
            if(title.equals("NUEVO MENSAJE")){

             showNotificationsMessage(data);

            }
            else {
                showNotifications(title, body);
            }
        }
    }

    private void showNotifications(String title, String body){
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotification(title, body);
        Random random = new Random();
        int n = random.nextInt(10000);
        notificationHelper.getManager().notify(n, builder.build());
    }
    private void showNotificationsMessage(Map<String, String> data) {
        String title = data.get("title");
        String body = data.get("body");
        String usernamesender = data.get("usernamesender");
        String usernamereceiver = data.get("usernamereceiver");
        String lastMessage = data.get("lastMessage");
        String messagesJSON = data.get("MESSAGES");
        int idNotification = Integer.parseInt(data.get("idNotification"));
        Gson gson = new Gson();
        Messages[] messages = gson.fromJson(messagesJSON, Messages[].class);

        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotificationMessage2(messages, usernamesender, usernamereceiver, lastMessage);
        notificationHelper.getManager().notify(idNotification, builder.build());
    }

}
