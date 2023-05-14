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
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;

import com.gina.takecare4u.R;
import com.gina.takecare4u.modelos.Messages;

import java.util.Date;

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

    public NotificationCompat.Builder getNotificationMessage(Messages[]messages){
        Person person1 = new Person.Builder()
                .setName("Gina")
                .setIcon(IconCompat.createWithResource(getApplicationContext(), R.drawable.chica))
                .build();
        Person person2 = new Person.Builder()
                .setName("Magda")
                .setIcon(IconCompat.createWithResource(getApplicationContext(), R.drawable.chica))
                .build();

        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(person1);
        NotificationCompat.MessagingStyle.Message message1 =
                new NotificationCompat.MessagingStyle.Message(
                        "ultimo Mensaje",
                        new Date().getTime(),
                        person1);
        messagingStyle.addMessage(message1);

        for (Messages m: messages){
            NotificationCompat.MessagingStyle.Message message2 =
                new NotificationCompat.MessagingStyle.Message(
                        m.getMessage(),
                        m.getTimeestamp(),
                        person2);
            messagingStyle.addMessage(message1);

        }
        return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setSmallIcon(R.drawable.chica)
                .setStyle(messagingStyle);
    }


    }



