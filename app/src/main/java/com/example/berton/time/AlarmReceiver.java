package com.example.berton.time;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.sql.Time;

/**
 * Created by Berton on 8/7/2016.
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        createNotification(context, "MSGTITLE", "TESTMSG", "Alert");
    }
    public void createNotification(Context context, String msg, String msgText, String msgAlert){
        PendingIntent notificIntent = PendingIntent.getActivity(context, 0, new Intent(context, TimeActivity.class),0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.timer)
                .setContentTitle(msg)
                .setTicker(msgAlert)
                .setContentText(msgText);
        mBuilder.setContentIntent(notificIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,mBuilder.build());

    }
}
