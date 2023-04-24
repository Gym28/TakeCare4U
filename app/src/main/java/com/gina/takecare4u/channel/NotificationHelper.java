package com.gina.takecare4u.channel;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.gina.takecare4u.R;

public class NotificationHelper extends ContextWrapper {

    private static final String CHANNEL_ID ="com.gina.takecare4u";
    private static final String CHANNEL_NAME = "Takecare4u";
    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createChannel();

        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH

        );
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(Color.GRAY);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getManager (){
        if(manager==null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getNotification(String title, String body){

        return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setColor(Color.GRAY)
                .setSmallIcon(R.drawable.chica)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setBigContentTitle(title));

    }



    }



