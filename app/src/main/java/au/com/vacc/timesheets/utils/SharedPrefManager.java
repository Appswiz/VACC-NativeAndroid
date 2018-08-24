package au.com.vacc.timesheets.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefManager {

	public static SharedPreferences getSharedPref(Context context) {
		return context.getSharedPreferences(MMAConstants.SHARED_PREFS, Context.MODE_PRIVATE);
	}

	public static void resetData(SharedPreferences sharedPref) {
		Editor editor = sharedPref.edit();
		editor.clear();
		editor.commit();
	}




	public static void setProfileData(SharedPreferences sharedPref, String val) {
		Editor editor = sharedPref.edit();
		editor.putString(MMAConstants.KEY_PROFILE_DATA, val);
		editor.commit();
	}

	public static String getProfileData(SharedPreferences sharedPref) {
		return sharedPref.getString(MMAConstants.KEY_PROFILE_DATA, "{}");
	}

}
