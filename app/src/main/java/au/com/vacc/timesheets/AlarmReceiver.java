package au.com.vacc.timesheets;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        String title = intent.getStringExtra("title");

        Intent intentActivity = new Intent(context, MembersActivity.class);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 101, intentActivity, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            Notification n;
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
            style.bigText(message);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String CHANNEL_ID = String.valueOf(101);// The id of the channel.
                CharSequence name = context.getResources().getString(R.string.app_name);// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(
                        CHANNEL_ID, name, importance);
                notificationManager.createNotificationChannel(mChannel);
                n = new NotificationCompat.Builder(context)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.icon)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setStyle(style)
                        .setWhen(System.currentTimeMillis())
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setChannelId(CHANNEL_ID)
                        .setAutoCancel(true).build();
            } else {
                n = new NotificationCompat.Builder(context)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.icon)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setStyle(style)
                        .setWhen(System.currentTimeMillis())
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setAutoCancel(true).build();
            }

            notificationManager.notify(101, n);
        }
    }


}