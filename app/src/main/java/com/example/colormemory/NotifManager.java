package com.example.colormemory;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.example.colormemory.Score.HightScore;

import java.util.Random;

public class NotifManager implements INotifManager {

    private static final String DEFAULT_CHANNEL_ID = "ChannelId";

    private final String _channelId;

    private final Random _idRandom;

    private final Context _context;


    public NotifManager(Context context) {
        this(context, DEFAULT_CHANNEL_ID);
    }

    public NotifManager(Context context, String channelId){
        _channelId = channelId;
        _context = context;
        _idRandom = new Random();

        createChannel();
    }

    private void createChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        CharSequence name = "chanel Name";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(_channelId, name, importance);

        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = _context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public int sendNotification(String title, String text, int iconId) {

        Intent goLeaderboard = new Intent(_context, HightScore.class);
        PendingIntent letsGo = PendingIntent.getActivity(_context, 0, goLeaderboard, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(_context, _channelId)
                .setSmallIcon(R.drawable.courone)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(letsGo)
                .setAutoCancel(true);

        // Gets an instance of the NotificationManager service
        NotificationManager notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
        int id = _idRandom.nextInt();
        notificationManager.notify(id, builder.build());
        return id;
    }


    @Override
    public int sendNotification(int titleRes, int textRes, int iconId) {
        Resources resources=_context.getResources();
        String title = resources.getString(titleRes);
        String text= resources.getString(textRes);

        return sendNotification(title, text, iconId);
    }
}
