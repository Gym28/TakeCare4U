package com.gina.takecare4u.activities.Utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.UsersProvider;

import java.util.List;

public class ViewedMessageHelper {

    public static  void updateOnline(boolean status, Context context) {
        UsersProvider usersProvider = new UsersProvider();
        AuthProvider authProvider = new AuthProvider();
        if(authProvider.getUid()!= null){
            if(isApplicationSendtoBackground(context)){
                usersProvider.upDateOnline(authProvider.getUid(), status);
            }
            else if (status) {
                usersProvider.upDateOnline(authProvider.getUid(), status);
            }
        }


    }
    public static boolean isApplicationSendtoBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> task = activityManager.getRunningTasks(1);
        if(!task.isEmpty()){
            ComponentName topActivity = task.get(0).topActivity;
            if(!topActivity.getPackageName().equals(context.getPackageName())){
                return true;
            }

        }
        return false;
    }

}
