package au.com.vacc.timesheets.pushnotif;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import au.com.vacc.timesheets.MainMenuActivity;
import au.com.vacc.timesheets.R;

public class GCMMessageHandler extends GcmListenerService {

	@Override
	public void onMessageReceived(String from, Bundle data) {
		String message = data.getString("message");
		SendNotification(message);
	}

	private void SendNotification(String message) {
		Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
		Notification n;
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			String CHANNEL_ID = String.valueOf(0);// The id of the channel.
			CharSequence name = getResources().getString(R.string.app_name);// The user-visible name of the channel.
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel mChannel = new NotificationChannel(
					CHANNEL_ID, name, importance);
			notificationManager.createNotificationChannel(mChannel);
			n = new NotificationCompat.Builder(this)
					.setContentTitle("Timesheet Message")
					.setSmallIcon(R.mipmap.icon)
					.setPriority(Notification.PRIORITY_HIGH)
					.setContentText(message)
					.setDefaults(Notification.DEFAULT_SOUND)
					.setChannelId(CHANNEL_ID)
					.setContentIntent(pendingIntent)
					.setAutoCancel(true).build();
		} else {
			n = new NotificationCompat.Builder(this)
					.setContentTitle("Timesheet Message")
					.setSmallIcon(R.mipmap.icon)
					.setPriority(Notification.PRIORITY_HIGH)
					.setContentText(message)
					.setDefaults(Notification.DEFAULT_SOUND)
					.setContentIntent(pendingIntent)
					.setAutoCancel(true).build();
		}

		notificationManager.notify(0, n);
	}

}