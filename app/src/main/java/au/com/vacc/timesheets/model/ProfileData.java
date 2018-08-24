package au.com.vacc.timesheets.model;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import org.codehaus.jackson.JsonNode;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;

import au.com.vacc.timesheets.utils.SharedPrefManager;
import au.com.vacc.timesheets.utils.Utils;

public class ProfileData implements Serializable {

    private static final long serialVersionUID = -7007014696099017450L;
    private boolean IsAppUnlocked = false;
    private boolean EmployerCodeAdded = false;
    private String FirstName = "";
    private String LastName = "";
    private String ApprenticeMembershipNumber = "";
    private String EmailAddress = "";
    private String MobileNumber = "";
    private String FieldManager = "";
    private String EmployerEmailAddress = "";
    private String ExtraHostEmailAddress = "";

    private boolean ParkingAllowance = false;
    private boolean MealAllowance = false;
    private boolean UniformAllowance = false;
    private boolean SchoolBasedApprentice = false;

    public ProfileData() {}

    public ProfileData(String profileData) {
        JsonNode jsonNode = Utils.convertStringToJsonNode(profileData);
        JsonNode profileNode = jsonNode.path("ProfileData");
        this.IsAppUnlocked = profileNode.path("IsAppUnlocked").asBoolean();
        this.EmployerCodeAdded = profileNode.path("EmployerCodeAdded").asBoolean();
        this.FirstName = profileNode.path("FirstName").asText();
        Log.v("sak", FirstName);
        this.LastName = profileNode.path("LastName").asText();
        this.ApprenticeMembershipNumber = profileNode.path("ApprenticeMembershipNumber").asText();
        this.EmailAddress = profileNode.path("EmailAddress").asText();
        this.MobileNumber = profileNode.path("MobileNumber").asText();
        this.FieldManager = profileNode.path("FieldManager").asText();
        this.EmployerEmailAddress = profileNode.path("EmployerEmailAddress").asText();
        this.ExtraHostEmailAddress = profileNode.path("ExtraHostEmailAddress").asText();
        this.ParkingAllowance = profileNode.path("ParkingAllowance").asBoolean();
        this.MealAllowance = profileNode.path("MealAllowance").asBoolean();
        this.UniformAllowance = profileNode.path("UniformAllowance").asBoolean();
        this.SchoolBasedApprentice = profileNode.path("SchoolBasedApprentice").asBoolean();
    }

    public boolean isAppUnlocked() {
        return IsAppUnlocked;
    }

    public void setAppUnlocked(boolean appUnlocked) {
        IsAppUnlocked = appUnlocked;
    }

    public boolean isEmployerCodeAdded() {
        return EmployerCodeAdded;
    }

    public void setEmployerCodeAdded(boolean employerCodeAdded) {
        EmployerCodeAdded = employerCodeAdded;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getApprenticeMembershipNumber() {
        return ApprenticeMembershipNumber;
    }

    public void setApprenticeMembershipNumber(String apprenticeMembershipNumber) {
        ApprenticeMembershipNumber = apprenticeMembershipNumber;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getFieldManager() {
        return FieldManager;
    }

    public void setFieldManager(String fieldManager) {
        FieldManager = fieldManager;
    }

    public String getEmployerEmailAddress() {
        return EmployerEmailAddress;
    }

    public void setEmployerEmailAddress(String employerEmailAddress) {
        EmployerEmailAddress = employerEmailAddress;
    }

    public String getExtraHostEmailAddress() {
        return ExtraHostEmailAddress;
    }

    public void setExtraHostEmailAddress(String extraHostEmailAddress) {
        ExtraHostEmailAddress = extraHostEmailAddress;
    }

    public boolean isParkingAllowance() {
        return ParkingAllowance;
    }

    public void setParkingAllowance(boolean parkingAllowance) {
        ParkingAllowance = parkingAllowance;
    }

    public boolean isMealAllowance() {
        return MealAllowance;
    }

    public void setMealAllowance(boolean mealAllowance) {
        MealAllowance = mealAllowance;
    }

    public boolean isUniformAllowance() {
        return UniformAllowance;
    }

    public void setUniformAllowance(boolean uniformAllowance) {
        UniformAllowance = uniformAllowance;
    }

    public boolean isSchoolBasedApprentice() {
        return SchoolBasedApprentice;
    }

    public void setSchoolBasedApprentice(boolean schoolBasedApprentice) {
        SchoolBasedApprentice = schoolBasedApprentice;
    }

    public String getProfileAsJsonString(ProfileData profileData)
    {

        try {
            JSONObject json = new JSONObject();
            JSONObject profileJsonObj = new JSONObject();
            profileJsonObj.put("IsAppUnlocked", profileData.isAppUnlocked());
            profileJsonObj.put("EmployerCodeAdded",  profileData.isEmployerCodeAdded());
            profileJsonObj.put("FirstName", profileData.getFirstName());
            profileJsonObj.put("LastName", profileData.getLastName());
            profileJsonObj.put("ApprenticeMembershipNumber", profileData.getApprenticeMembershipNumber());
            profileJsonObj.put("EmailAddress",profileData.getEmailAddress());
            profileJsonObj.put("MobileNumber", profileData.getMobileNumber());
            profileJsonObj.put("FieldManager", profileData.getFieldManager());
            profileJsonObj.put("EmployerEmailAddress", profileData.getEmployerEmailAddress());
            profileJsonObj.put("ExtraHostEmailAddress", profileData.getExtraHostEmailAddress());
            profileJsonObj.put("ParkingAllowance", profileData.isParkingAllowance());
            profileJsonObj.put("MealAllowance", profileData.isMealAllowance());
            profileJsonObj.put("UniformAllowance", profileData.isUniformAllowance());
            profileJsonObj.put("SchoolBasedApprentice", profileData.isSchoolBasedApprentice());
            json.put("ProfileData", profileJsonObj);

            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return "{}";
    }

    public boolean isProfileComplete(ProfileData profileData)
    {
        //Lets see if all the non-optional strings aren't blank.
        return 	!TextUtils.isEmpty(profileData.getFirstName()) &&
                !TextUtils.isEmpty(profileData.getLastName()) &&
                !TextUtils.isEmpty(profileData.getApprenticeMembershipNumber()) &&
                !TextUtils.isEmpty(profileData.getEmailAddress()) &&
                !TextUtils.isEmpty(profileData.getMobileNumber()) &&
                !TextUtils.isEmpty(profileData.getFieldManager()) &&
                !TextUtils.isEmpty(profileData.getEmployerEmailAddress());
    }
}
