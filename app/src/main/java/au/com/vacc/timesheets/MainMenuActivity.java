package au.com.vacc.timesheets;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.codehaus.jackson.JsonNode;

import au.com.vacc.timesheets.model.ProfileData;
import au.com.vacc.timesheets.pushnotif.GCMClientManager;
import au.com.vacc.timesheets.utils.CustomJsonCallBack;
import au.com.vacc.timesheets.utils.CustomJsonRequest;
import au.com.vacc.timesheets.utils.SharedPrefManager;


public class MainMenuActivity extends AppCompatActivity  {
    private GCMClientManager pushClientManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        findViewById(R.id.btn_apprenticeship_program).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PDFViewerActivity.class);
                intent.putExtra("pdfFileName","program.pdf");
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_automative_careers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AutomativeCareersActivity.class));
            }
        });
        findViewById(R.id.btn_about_vacc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AboutVaccActivity.class));
            }
        });
        findViewById(R.id.btn_vet_in_school).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), VetInSchoolActivity.class));
            }
        });
        findViewById(R.id.btn_members).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MembersActivity.class));
            }
        });

        registerGCM();
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