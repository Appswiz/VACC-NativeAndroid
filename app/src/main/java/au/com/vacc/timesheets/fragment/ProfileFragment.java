package au.com.vacc.timesheets.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import au.com.vacc.timesheets.MainMenuActivity;
import au.com.vacc.timesheets.MembersActivity;
import au.com.vacc.timesheets.R;
import au.com.vacc.timesheets.model.ProfileData;
import au.com.vacc.timesheets.utils.SharedPrefManager;

/**
 * Created by Juhachi on 11/10/2017.
 */

public class ProfileFragment extends Fragment {
    private int defaultColour = Color.argb(255, 4, 41, 138);
    private int highlightColour = Color.argb(255, 255, 90, 0);

    private ProfileData profileData;
    private Activity act;
    private EditText etFirstName, etLastName, etPayrollNo, etEmail, etMobile, etFieldManager, etEmployerEmail, etExtraHostEmail;
    private CheckBox cbMeal, cbParking, cbUniform, cbSchoolBased;
    private ProgressBar progressBar;
    private ImageView profileInfoImage;
    private SharedPreferences sharedPref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        act = getActivity();

        sharedPref = SharedPrefManager.getSharedPref(act);
        String profileDataJsonString = SharedPrefManager.getProfileData(sharedPref);
        profileData = new ProfileData(profileDataJsonString);

        rootView.findViewById(R.id.iv_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.onBackPressed();
            }
        });

        etFirstName = (EditText) rootView.findViewById(R.id.et_first_name);
        etLastName = (EditText) rootView.findViewById(R.id.et_last_name);
        etPayrollNo = (EditText) rootView.findViewById(R.id.et_payroll_no);
        etEmail = (EditText) rootView.findViewById(R.id.et_email);
        etMobile = (EditText) rootView.findViewById(R.id.et_mobile);
        etFieldManager = (EditText) rootView.findViewById(R.id.et_field_manager);
        etEmployerEmail = (EditText) rootView.findViewById(R.id.et_employer_email);
        etExtraHostEmail = (EditText) rootView.findViewById(R.id.et_extra_host_email);

        cbMeal = (CheckBox) rootView.findViewById(R.id.cb_meal);
        cbParking = (CheckBox) rootView.findViewById(R.id.cb_parking);
        cbUniform = (CheckBox) rootView.findViewById(R.id.cb_uniform);
        cbSchoolBased = (CheckBox) rootView.findViewById(R.id.cb_school_based);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        profileInfoImage = (ImageView) rootView.findViewById(R.id.profileInfo);

        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                onProfileUpdated();
            }
        });
        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                onProfileUpdated();
            }
        });
        etPayrollNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                onProfileUpdated();
            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                onProfileUpdated();
            }
        });
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                onProfileUpdated();
            }
        });
        etFieldManager.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                onProfileUpdated();
            }
        });
        etEmployerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                onProfileUpdated();
            }
        });
        etExtraHostEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                onProfileUpdated();
            }
        });

        profileInfoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });
        profileInfoImage.setColorFilter(defaultColour);

        rootView.findViewById(R.id.resetProfileButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetProfile();
            }
        });

        return rootView;
    }

    public void saveData()
    {
        profileData.setFirstName(etFirstName.getText().toString());
        profileData.setLastName(etLastName.getText().toString());
        profileData.setApprenticeMembershipNumber(etPayrollNo.getText().toString());
        profileData.setEmailAddress(etEmail.getText().toString());
        profileData.setMobileNumber(etMobile.getText().toString());
        profileData.setEmployerEmailAddress(etEmployerEmail.getText().toString());
        profileData.setExtraHostEmailAddress(etExtraHostEmail.getText().toString());
        profileData.setFieldManager(etFieldManager.getText().toString());

        profileData.setMealAllowance(cbMeal.isChecked());
        profileData.setParkingAllowance(cbParking.isChecked());
        profileData.setUniformAllowance(cbUniform.isChecked());
        profileData.setSchoolBasedApprentice(cbSchoolBased.isChecked());

        //Save data to private storage
        SharedPrefManager.setProfileData(sharedPref, profileData.getProfileAsJsonString(profileData));
        ((MembersActivity) act).registerGCM();
    }

    private void onProfileUpdated()
    {
		int totalFields = 7;

        //Lets see how many non-empty strings we have
        int fieldsCompleted = 0;

        if(!TextUtils.isEmpty(etFirstName.getText().toString())) fieldsCompleted++;
        if(!TextUtils.isEmpty(etLastName.getText().toString())) fieldsCompleted++;
        if(!TextUtils.isEmpty(etPayrollNo.getText().toString())) fieldsCompleted++;
        if(!TextUtils.isEmpty(etEmail.getText().toString())) fieldsCompleted++;
        if(!TextUtils.isEmpty(etMobile.getText().toString())) fieldsCompleted++;
        if(!TextUtils.isEmpty(etFieldManager.getText().toString())) fieldsCompleted++;
        if(!TextUtils.isEmpty(etEmployerEmail.getText().toString())) fieldsCompleted++;
        if(!TextUtils.isEmpty(etExtraHostEmail.getText().toString())) fieldsCompleted++;


        int progressVal = (int)(((float)fieldsCompleted / (float) totalFields) * 100.0f);
        progressBar.setProgress(progressVal);
    }

    public void showInfoDialog()
    {
        profileInfoImage.setColorFilter(highlightColour);
        AlertDialog.Builder dialog = new AlertDialog.Builder(act);
        dialog.setTitle("Profile Info");
        dialog.setTitle("Enter your details to complete the profile. When your profile is complete, you may complete and submit your timesheet via the Timesheet tab.");
        dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                profileInfoImage.setColorFilter(defaultColour);
                dialog.cancel();
            }
        });
        dialog.show();
    }


    public void onResetProfile()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(act);
        dialog.setTitle("Reset Profile");
        dialog.setMessage("Resetting your profile will clear all of your profile details. Are you sure you want to reset your profile?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etFirstName.setText("");
                etLastName.setText("");
                etPayrollNo.setText("");
                etEmail.setText("");
                etMobile.setText("");
                etFieldManager.setText("");
                etEmployerEmail.setText("");
                etExtraHostEmail.setText("");

                cbMeal.setChecked(false);
                cbParking.setChecked(false);
                cbUniform.setChecked(false);
                cbSchoolBased.setChecked(false);

                profileData.setEmployerCodeAdded(false);
                saveData();
                dialog.cancel();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void onStop() {
        saveData();
        super.onStop();
    }

    public void onPause() {
        super.onPause();
        saveData();
    }

    public void onResume() {
        super.onResume();
        Log.v("sak","onREsume " + profileData.getFirstName());
        etFirstName.setText(profileData.getFirstName());
        etLastName.setText(profileData.getLastName());
        etPayrollNo.setText(profileData.getApprenticeMembershipNumber());
        etEmail.setText(profileData.getEmailAddress());
        etMobile.setText(profileData.getMobileNumber());
        etEmployerEmail.setText(profileData.getEmployerEmailAddress());
        etExtraHostEmail.setText(profileData.getExtraHostEmailAddress());
        etFieldManager.setText(profileData.getFieldManager());

        cbMeal.setChecked(profileData.isMealAllowance());
        cbParking.setChecked(profileData.isParkingAllowance());
        cbUniform.setChecked(profileData.isUniformAllowance());
        cbSchoolBased.setChecked(profileData.isSchoolBasedApprentice());
    }

}