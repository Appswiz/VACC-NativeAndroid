package au.com.vacc.timesheets;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.codehaus.jackson.JsonNode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import au.com.vacc.timesheets.fragment.AboutFragment;
import au.com.vacc.timesheets.fragment.ProfileFragment;
import au.com.vacc.timesheets.fragment.TimeSheetFragment;
import au.com.vacc.timesheets.model.ProfileData;
import au.com.vacc.timesheets.pushnotif.GCMClientManager;
import au.com.vacc.timesheets.utils.CustomJsonCallBack;
import au.com.vacc.timesheets.utils.CustomJsonRequest;
import au.com.vacc.timesheets.utils.SharedPrefManager;

public class MembersActivity extends AppCompatActivity {
    private GCMClientManager pushClientManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                if (tabId == R.id.tab_timesheet) {
                    TimeSheetFragment timeSheetFragment = new TimeSheetFragment();
                    fragmentTransaction.replace(R.id.container, timeSheetFragment);
                } else if(tabId == R.id.tab_profile) {
                    ProfileFragment profileFragment = new ProfileFragment();
                    fragmentTransaction.replace(R.id.container, profileFragment);
                } else if(tabId == R.id.tab_about) {
                    AboutFragment aboutFragment = new AboutFragment();
                    fragmentTransaction.replace(R.id.container, aboutFragment);
                }
                fragmentTransaction.commit();
            }
        });

        bottomBar.setActiveTabColor(Color.parseColor("#FFA500"));
    }

    public void showTimeStamp()
    {
        List<String> timeStampList = getTimeStampFromPreferences();
        Collections.reverse(timeStampList);
        String timeStampString = splitList(timeStampList, "\n");
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Uploaded Timestamps");
        dialog.setMessage(timeStampString);
        dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private List<String> getTimeStampFromPreferences()
    {
        List<String> timeStampList = new ArrayList<String>();
        SharedPreferences pref = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        String timestamps = pref.getString("TimeStamp", null);
        if (timestamps == null)
        {
            return timeStampList;
        } else
        {
            String[] timeStampArray = timestamps.split("---");
            for(int i = 0; i < timeStampArray.length; i++) {
                timeStampList.add(timeStampArray[i]);
            }
        }
        Collections.reverse(timeStampList);
        return timeStampList;
    }

    private String splitList(List<String> timeStampList, String v)
    {
        String tempString = "";
        for (int i = 0; i < timeStampList.size(); i++)
        {
            if (i == 10)
            {
                break;
            }
            if (i != 0)
            {
                tempString += v;
            }
            tempString += timeStampList.get(i);
        }
        return tempString;
    }

    public void saveTimeStamp()
    {
        Calendar cal = Calendar.getInstance();
        String strDateFormat = "dd/MM/yyyy hh:mm:ss a";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String dtString = sdf.format(cal.getTime());
        List<String> timeStampList = getTimeStampFromPreferences();
        timeStampList.add(dtString);
        Collections.reverse(timeStampList);
        String timeStampString = splitList(timeStampList, "---");
        SharedPreferences pref = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("TimeStamp", timeStampString);
        editor.commit();
    }


    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void registerGCM() {
        pushClientManager = new GCMClientManager(this);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                sendRegistrationToAppServer(registrationId);
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });
    }

    private void sendRegistrationToAppServer(String token) {
        ProfileData profileData = new ProfileData(SharedPrefManager.getProfileData(SharedPrefManager.getSharedPref(this)));
        String postBody = "deviceToken=" + token + "&deviceType=Android&messagingService=GCM&applicationId=92139&externalRef=" + profileData.getApprenticeMembershipNumber();
        CustomJsonRequest jsonRequest = new CustomJsonRequest(this, "https://www.mymobileapp.online/api/v1/app/RegisterDeviceWithMessagingService", postBody, false, new CustomJsonCallBack() {
            @Override
            public void onError(String error) {
            }

            @Override
            public void getResult(String jsonNode) {
            }
        }, CustomJsonRequest.POST);
        jsonRequest.execute();
    }
}