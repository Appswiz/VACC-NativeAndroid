package au.com.vacc.timesheets;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import au.com.vacc.timesheets.pushnotif.GCMClientManager;
import au.com.vacc.timesheets.utils.MMAConstants;


public class SplashActivity extends AppCompatActivity {

	private Handler splashHandler = new Handler();
	private boolean hasStarted, hasRequestPermission, hasGoToSettings, isRequestDialogOpen;
	private Runnable splashRunnable = new Runnable() {
		@Override
		public void run() {
			splashHandler.removeCallbacks(splashRunnable);
			startActivity(new Intent(SplashActivity.this, MainMenuActivity.class));
			finish();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		if (hasRequestPermission && hasGoToSettings) {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
				if (!isRequestDialogOpen)
					showRequestDialog();
			} else {
				if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					if (!isRequestDialogOpen)
						showRequestDialog();
				} else {
					if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						if (!isRequestDialogOpen)
							showRequestDialog();
					} else {
						if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
							if (!isRequestDialogOpen)
								showRequestDialog();
						} else {
							if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
								if (!isRequestDialogOpen)
									showRequestDialog();
							} else {
								if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
									if (!isRequestDialogOpen)
										showRequestDialog();
								} else {
									continueSplashScreen();
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
	/*	if(SharedPrefManager.getAppVersion(sharedPref) != Utils.getAppVersion(context)) {
			SharedPrefManager.removeSavedMemberData(sharedPref);
		} */
		continueSplashScreen();
	//	checkPermissions();


	}


	private void checkPermissions() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			hasRequestPermission = true;
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MMAConstants.PERMISSION_REQUEST_CAMERA);
		} else {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				hasRequestPermission = true;
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MMAConstants.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
			} else {
				if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					hasRequestPermission = true;
					ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MMAConstants.PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
				} else {
					if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
						hasRequestPermission = true;
						ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MMAConstants.PERMISSION_REQUEST_READ_CONTACTS);
					} else {
						if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
							hasRequestPermission = true;
							ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, MMAConstants.PERMISSION_REQUEST_WRITE_CONTACTS);
						} else {
							if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
								hasRequestPermission = true;
								ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MMAConstants.PERMISSION_REQUEST_CALL_PHONE);
							} else {
								continueSplashScreen();
							}
						}
					}
				}
			}
		}
	}


	private void continueSplashScreen() {
			hasStarted = true;
			splashHandler.postDelayed(splashRunnable, 3000);
			setAlarm();

	}

	public void showRequestDialog() {
		isRequestDialogOpen = true;
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setCancelable(false);
		dialog.setTitle(getString(R.string.permission));
		dialog.setMessage(getString(R.string.permission_info));
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				hasGoToSettings = true;
				isRequestDialogOpen = false;
				final Intent i = new Intent();
				i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				i.addCategory(Intent.CATEGORY_DEFAULT);
				i.setData(Uri.parse("package:" + getPackageName()));
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				startActivity(i);
				dialog.cancel();
			}
		});
		dialog.show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (hasStarted) {
			splashHandler.removeCallbacks(splashRunnable);
		}
	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == MMAConstants.PERMISSION_REQUEST_CAMERA) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MMAConstants.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
			} else {
				showRequestDialog();
			}
		} else if (requestCode == MMAConstants.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MMAConstants.PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
			} else {
				showRequestDialog();
			}
		}  else if (requestCode == MMAConstants.PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MMAConstants.PERMISSION_REQUEST_READ_CONTACTS);
			} else {
				showRequestDialog();
			}
		} else if (requestCode == MMAConstants.PERMISSION_REQUEST_READ_CONTACTS) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, MMAConstants.PERMISSION_REQUEST_WRITE_CONTACTS);
			} else {
				showRequestDialog();
			}
		} else if (requestCode == MMAConstants.PERMISSION_REQUEST_WRITE_CONTACTS) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MMAConstants.PERMISSION_REQUEST_CALL_PHONE);
			} else {
				showRequestDialog();
			}
		} else if (requestCode == MMAConstants.PERMISSION_REQUEST_CALL_PHONE) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				continueSplashScreen();
			} else {
				showRequestDialog();
			}
		}
	}


	private void setAlarm() {
		Intent alarmIntent = new Intent(this, AlarmReceiver.class);
		alarmIntent.putExtra("message", "Don\'t forget to do your timesheets if you haven\'t already.");
		alarmIntent.putExtra("title", "Remind");

		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 101, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);



		Calendar calendar = Calendar.getInstance();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date today = new Date();
		Date todayWithZeroTime = null;
		try {
			todayWithZeroTime = formatter.parse(formatter.format(today));
			calendar.setTime(todayWithZeroTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar nextFriday = getNextFriday(calendar, Calendar.FRIDAY);
		nextFriday.add(Calendar.HOUR_OF_DAY, 17);
		alarmManager.set(AlarmManager.RTC_WAKEUP,nextFriday.getTimeInMillis(),pendingIntent);

		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, nextFriday.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
	}

	public static Calendar getNextFriday(Calendar today, int friday)
	{
		int weekday = today.get(Calendar.DAY_OF_WEEK);
		if (weekday != Calendar.FRIDAY)
		{
			int days = (Calendar.WEDNESDAY - weekday + 2) % 7;
			today.add(Calendar.DAY_OF_YEAR, days);
		}
		return today;
	}
}
