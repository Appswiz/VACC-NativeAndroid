package au.com.vacc.timesheets.utils;

/**
 * Created by Juhachi on 11/10/2017.
 */
import java.io.File;

public class MMAConstants {

    public static final String SHARED_PREFS = "VACC_PREF";
    public static final String KEY_PROFILE_DATA = "KEY_PROFILE_DATA";
    //dev = 1  qa = 2  prod = 3
    public static final int API = 3;
    public static final String API_DEV = "https://devapi.mmem.com.au/";
    public static final String API_DEV_2 = "https://devapi.mmem.com.au/Appswiz/Sparky/1.0/";

   public static final String API_QA = "https://qaapi.mmem.com.au/";
    public static final String API_QA_2 = "https://qaapi.mmem.com.au/Appswiz/Sparky/1.0/";

    public static final String API_PROD = "https://api.mmem.com.au/";
    public static final String API_PROD_2 = "https://api.mmem.com.au/Appswiz/Sparky/1.0/";

    public static final String NETFIRA_API = "http://sparky.netfira.com/nfinterface_v2/nfsparky/sparky/processdocument";
    public static final String INTEGRAPAY_API = "https://testpayments.integrapay.com.au/API/API.ashx";
    public static final int PERMISSION_REQUEST_READ_CONTACTS = 1;
    public static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 2;
    public static final int PERMISSION_REQUEST_CALL_PHONE = 3;
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 4;
    public static final int PERMISSION_REQUEST_CAMERA = 5;
    public static final int REQUEST_SELECT_IMAGE = 6;
    public static final int PERMISSION_REQUEST_WRITE_CONTACTS = 7;
    public static final int REQUEST_PICK_IMAGE_ID = 8;
    public static final int REQUEST_PICK_CONTACT = 9;
    public static File imageFile;
}
