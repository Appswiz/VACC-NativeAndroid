package au.com.vacc.timesheets.utils;

/**
 * Created by Juhachi on 11/10/2017.
 */
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import au.com.vacc.timesheets.model.ProfileData;
import au.com.vacc.timesheets.model.TimeSheetData;

public class TimeSheetCore {
    public static BigDecimal HoursTarget = new BigDecimal("38");            // the hours in a normally paid working week
    public static BigDecimal HoursUntilOvertime = new BigDecimal("3");      // the time (hours) until overtime starts

    public ProfileData profileData;
    public TimeSheetData timeSheetData;

    public static boolean resumedApp = false;
    public static  boolean unlockCodeNeeded = false;

    public TimeSheetCore(SharedPreferences sharedPref)
    {
        profileData = new ProfileData(SharedPrefManager.getProfileData(sharedPref));
        timeSheetData = new TimeSheetData();
    }

    public String EncodeEmployerApprovalString()
    {
        String dateNumbers = "0123456789";
        String codeLetters = "ZXVTRPNLJH";
        String encodedString = "";

        for (int i = 0; i < timeSheetData.getEmployerCode().length(); i++)
        {
            for (int j = 0; j < dateNumbers.length(); j++)
            {
                if (timeSheetData.getEmployerCode().charAt(i) == dateNumbers.charAt(j))
                {
                    encodedString += codeLetters.charAt(j);
                    break;
                }
            }
        }

        return encodedString;
    }

    public String GetTimesheetAsEmail()
    {
        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("NOTE: The following information is also attached to this email as a .CSV file that can be opened in Excel.");
        sb.append("\n");
        sb.append("\n");
        sb.append("*** PROFILE ***");
        sb.append("\n");
        sb.append(String.format("Name: %s, %s\n", profileData.getLastName().toUpperCase(), profileData.getFirstName()));
        sb.append(String.format("Apprentice Payroll #: %s\n", profileData.getApprenticeMembershipNumber()));
        sb.append(String.format("Mobile: %s\n", profileData.getMobileNumber()));
        sb.append(String.format("Email: %s\n", profileData.getEmailAddress()));
        sb.append(String.format("Host Employer Email: %s\n", profileData.getEmployerEmailAddress()));
        sb.append(String.format("Extra Host Email: %s\n", profileData.getExtraHostEmailAddress()));
        sb.append(String.format("Field Manager: %s\n", profileData.getFieldManager()));
        sb.append("\n");
        sb.append("** Allowances **");
        sb.append("\n");
        sb.append(String.format("Parking: %s\n", profileData.isParkingAllowance()));
        sb.append(String.format("Meal: %s\n", profileData.isMealAllowance()));
        sb.append(String.format("Uniform: %s\n", profileData.isUniformAllowance()));
        sb.append("\n");
        sb.append("** Apprentice Type **");
        sb.append("\n");
        sb.append(String.format("School Based: %s\n", profileData.isSchoolBasedApprentice()));
        sb.append("\n");
        sb.append("*** TIMESHEET ***");
        sb.append("\n");
        sb.append(String.format("Week: %s to %s\n", formatDate.format(timeSheetData.getStartDate().getTime()), formatDate.format(timeSheetData.getEndDate().getTime())));
        sb.append("\n");
        sb.append("** Hours **");
        sb.append("\n");
        sb.append(String.format("Work: %s\n", timeSheetData.getWorkHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
        sb.append(String.format("Total Time and a Half: %s\n", timeSheetData.getTimeAndAHalfHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
        sb.append(String.format("Total BigDecimal Time: %s\n", timeSheetData.getDoubleTimeHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
        sb.append("\n");
        sb.append("* Overtime *");
        sb.append("\n");
        for(int i = 0; i < timeSheetData.getOvertimeHours().size(); i++) {
            String item = timeSheetData.getOvertimeHours().get(i);
            String[] itemArr = item.split("---");
            BigDecimal val = new BigDecimal(itemArr[1]);
            if(val.compareTo(BigDecimal.ZERO) > 0) {
                sb.append(itemArr[0]);
                sb.append(String.format(": %s\n", val.setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
            }
        }

        sb.append(String.format("Total Overtime: %s\n", timeSheetData.getTotalOvertimeHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));

        sb.append("\n");

        sb.append("* Sick Leave *");
        sb.append("\n");
        for(int i = 0; i < timeSheetData.getSickLeaveHours().size(); i++) {
            String item = timeSheetData.getSickLeaveHours().get(i);
            String[] itemArr = item.split("---");
            BigDecimal val = new BigDecimal(itemArr[1]);
            if(val.compareTo(BigDecimal.ZERO) > 0) {
                sb.append(itemArr[0]);
                sb.append(String.format(": %s\n", val.setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
            }
        }
        sb.append(String.format("Total Sick Leave: %s\n", timeSheetData.getTotalSickLeaveHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));

        sb.append("\n");

        sb.append("* Annual Leave *");
        sb.append("\n");
        for(int i = 0; i < timeSheetData.getAnnualLeaveHours().size(); i++) {
            String item = timeSheetData.getAnnualLeaveHours().get(i);
            String[] itemArr = item.split("---");
            BigDecimal val = new BigDecimal(itemArr[1]);
            if(val.compareTo(BigDecimal.ZERO) > 0) {
                sb.append(itemArr[0]);
                sb.append(String.format(": %s\n", val.setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
            }
        }
        sb.append(String.format("Total Annual Leave: %s\n", timeSheetData.getTotalAnnualLeaveHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));

        sb.append("\n");

        sb.append("* Bereavement Leave *");
        sb.append("\n");
        for(int i = 0; i < timeSheetData.getBereavementLeaveHours().size(); i++) {
            String item = timeSheetData.getBereavementLeaveHours().get(i);
            String[] itemArr = item.split("---");
            BigDecimal val = new BigDecimal(itemArr[1]);
            if(val.compareTo(BigDecimal.ZERO) > 0) {
                sb.append(itemArr[0]);
                sb.append(String.format(": %s\n", val.setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
            }
        }
        sb.append(String.format("Total Bereavement Leave: %s\n", timeSheetData.getTotalBereavementLeaveHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));

        sb.append("\n");

        sb.append("* VACC Time *");
        sb.append("\n");
        for(int i = 0; i < timeSheetData.getVACCTimeHours().size(); i++) {
            String item = timeSheetData.getVACCTimeHours().get(i);
            String[] itemArr = item.split("---");
            BigDecimal val = new BigDecimal(itemArr[1]);
            if(val.compareTo(BigDecimal.ZERO) > 0) {
                sb.append(itemArr[0]);
                sb.append(String.format(": %s\n", val.setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
            }
        }
        sb.append(String.format("Total VACC Time: %s\n", timeSheetData.getTotalVACCTimeHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));


        sb.append("\n");

        sb.append(String.format("RTO / TAFE: %s\n",timeSheetData.getTAFEHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
        sb.append(String.format("Notes:\n%s\n", (TextUtils.isEmpty(timeSheetData.getNotes())) ? " no notes " : timeSheetData.getNotes()));
        sb.append("\n");
        sb.append(String.format("Employer Approval Code: %s\n", EncodeEmployerApprovalString()));

        return sb.toString();
    }

    public String GetTimesheetAsCsv()
    {
        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("*** PROFILE ***");
        sb.append("\n");
        sb.append(String.format("Name:,%s %s\n", profileData.getLastName().toUpperCase(), profileData.getFirstName()));
        sb.append(String.format("Apprentice Payroll #:,%s\n", profileData.getApprenticeMembershipNumber()));
        sb.append(String.format("Mobile:,%s\n", profileData.getMobileNumber()));
        sb.append(String.format("Email:,%s\n", profileData.getEmailAddress()));
        sb.append(String.format("Host Employer Email:,%s\n", profileData.getEmployerEmailAddress()));
        sb.append(String.format("Extra Host Email:,%s\n", profileData.getExtraHostEmailAddress()));
        sb.append(String.format("Field Manager:,%s\n", profileData.getFieldManager()));
        sb.append("\n");
        sb.append("** Allowances **");
        sb.append("\n");
        sb.append(String.format("Parking:,%s\n", profileData.isParkingAllowance()));
        sb.append(String.format("Meal:,%s\n", profileData.isMealAllowance()));
        sb.append(String.format("Uniform:,%s\n", profileData.isUniformAllowance()));
        sb.append("\n");
        sb.append("** Apprentice Type **");
        sb.append("\n");
        sb.append(String.format("School Based:,%s\n", profileData.isSchoolBasedApprentice()));
        sb.append("\n");
        sb.append("*** TIMESHEET ***");
        sb.append("\n");
        sb.append(String.format("Week:,%s to %s\n", formatDate.format(timeSheetData.getStartDate().getTime()), formatDate.format(timeSheetData.getEndDate().getTime())));
        sb.append("\n");
        sb.append("** Hours **");
        sb.append("\n");
        sb.append(String.format("Work:,%s\n", timeSheetData.getWorkHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
        sb.append(String.format("Total Time and a Half:,%s\n", timeSheetData.getTimeAndAHalfHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
        sb.append(String.format("Total BigDecimal Time:,%s\n", timeSheetData.getDoubleTimeHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
        sb.append("\n");
        sb.append("* Overtime *");
        sb.append("\n");
        for(int i = 0; i < timeSheetData.getOvertimeHours().size(); i++) {
            String item = timeSheetData.getOvertimeHours().get(i);
            String[] itemArr = item.split("---");
            BigDecimal val = new BigDecimal(itemArr[1]);
            if(val.compareTo(BigDecimal.ZERO) > 0) {
                sb.append(itemArr[0]);
                sb.append(String.format(":,%s\n", val.setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
            }
        }

        sb.append(String.format("Total Overtime:,%s\n", timeSheetData.getTotalOvertimeHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));

        sb.append("\n");

        sb.append("* Sick Leave *");
        sb.append("\n");
        for(int i = 0; i < timeSheetData.getSickLeaveHours().size(); i++) {
            String item = timeSheetData.getSickLeaveHours().get(i);
            String[] itemArr = item.split("---");
            BigDecimal val = new BigDecimal(itemArr[1]);
            if(val.compareTo(BigDecimal.ZERO) > 0) {
                sb.append(itemArr[0]);
                sb.append(String.format(":,%s\n", val.setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
            }
        }
        sb.append(String.format("Total Sick Leave:,%s\n", timeSheetData.getTotalSickLeaveHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));

        sb.append("\n");

        sb.append("* Annual Leave *");
        sb.append("\n");
        for(int i = 0; i < timeSheetData.getAnnualLeaveHours().size(); i++) {
            String item = timeSheetData.getAnnualLeaveHours().get(i);
            String[] itemArr = item.split("---");
            BigDecimal val = new BigDecimal(itemArr[1]);
            if(val.compareTo(BigDecimal.ZERO) > 0) {
                sb.append(itemArr[0]);
                sb.append(String.format(":,%s\n", val.setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
            }
        }
        sb.append(String.format("Total Annual Leave:,%s\n", timeSheetData.getTotalAnnualLeaveHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));

        sb.append("\n");

        sb.append("* Bereavement Leave *");
        sb.append("\n");
        for(int i = 0; i < timeSheetData.getBereavementLeaveHours().size(); i++) {
            String item = timeSheetData.getBereavementLeaveHours().get(i);
            String[] itemArr = item.split("---");
            BigDecimal val = new BigDecimal(itemArr[1]);
            if(val.compareTo(BigDecimal.ZERO) > 0) {
                sb.append(itemArr[0]);
                sb.append(String.format(":,%s\n", val.setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
            }
        }
        sb.append(String.format("Total Bereavement Leave:,%s\n", timeSheetData.getTotalBereavementLeaveHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));

        sb.append("\n");

        sb.append("* VACC Time *");
        sb.append("\n");
        for(int i = 0; i < timeSheetData.getVACCTimeHours().size(); i++) {
            String item = timeSheetData.getVACCTimeHours().get(i);
            String[] itemArr = item.split("---");
            BigDecimal val = new BigDecimal(itemArr[1]);
            if(val.compareTo(BigDecimal.ZERO) > 0) {
                sb.append(itemArr[0]);
                sb.append(String.format(":,%s\n", val.setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
            }
        }
        sb.append(String.format("Total VACC Time:,%s\n", timeSheetData.getTotalVACCTimeHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));


        sb.append("\n");

        sb.append(String.format("RTO / TAFE:,%s\n",timeSheetData.getTAFEHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
        sb.append(String.format("Notes:\n%s\n", (TextUtils.isEmpty(timeSheetData.getNotes())) ? " no notes " : timeSheetData.getNotes()));
        sb.append("\n");
        sb.append(String.format("Employer Approval Code:,%s\n", EncodeEmployerApprovalString()));

        return sb.toString();
    }

    public String GetTimesheetDataAsPostBody()
    {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("ApprenticeId=%s&", profileData.getApprenticeMembershipNumber()));
        sb.append(String.format("Email=%s&", profileData.getEmailAddress()));
        sb.append(String.format("Mobile=%s&", profileData.getMobileNumber()));
        sb.append(String.format("Manager=%s&", profileData.getFieldManager()));
        sb.append(String.format("EmployerEmail1=%s&", profileData.getEmployerEmailAddress()));
        sb.append(String.format("EmployerEmail2=%s&", profileData.getExtraHostEmailAddress()));
        sb.append(String.format("School=%s&", profileData.isSchoolBasedApprentice()));
        sb.append(String.format("Meal=%s&", profileData.isMealAllowance()));
        sb.append(String.format("Uniform=%s&", profileData.isUniformAllowance()));
        sb.append(String.format("Parking=%s&", profileData.isParkingAllowance()));
        sb.append(String.format("Week=%s&", formatDate.format(timeSheetData.getStartDate().getTime())));
        sb.append(String.format("ApprovalCode=%s&", timeSheetData.getEmployerCode()));
        sb.append(String.format("NormalHours=%s&", timeSheetData.getWorkHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
        sb.append(String.format("HalfHours=%s&",timeSheetData.getTimeAndAHalfHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
        sb.append(String.format("DoubleHours=%s&", timeSheetData.getDoubleTimeHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
        boolean startedDayBuilder = false;

        //OVERTIME
        sb.append("OvertimeDays=");
        for(int i = 0; i < timeSheetData.getOvertimeHours().size(); i++) {
            String item = timeSheetData.getOvertimeHours().get(i);
            String[] itemArr = item.split("---");
            addDayValue(sb, new BigDecimal(itemArr[1]), !startedDayBuilder);
            startedDayBuilder = true;
        }
        startedDayBuilder = false;


        sb.append("&VaccDays=");
        for(int i = 0; i < timeSheetData.getVACCTimeHours().size(); i++) {
            String item = timeSheetData.getVACCTimeHours().get(i);
            String[] itemArr = item.split("---");
            addDayValue(sb, new BigDecimal(itemArr[1]), !startedDayBuilder);
            startedDayBuilder = true;
        }
        startedDayBuilder = false;

        sb.append(String.format("&Vacc=%s&", timeSheetData.getTotalVACCTimeHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));
        sb.append(String.format("Tafe=%s&", timeSheetData.getTAFEHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));

        sb.append("SickDays=");
        for(int i = 0; i < timeSheetData.getSickLeaveHours().size(); i++) {
            String item = timeSheetData.getSickLeaveHours().get(i);
            String[] itemArr = item.split("---");
            addDayValue(sb, new BigDecimal(itemArr[1]), !startedDayBuilder);
            startedDayBuilder = true;
        }
        startedDayBuilder = false;

        sb.append(String.format("&Sick=%s&", timeSheetData.getTotalSickLeaveHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));

        //ANNUAL LEAVE
        sb.append("LeaveDays=");

        for(int i = 0; i < timeSheetData.getAnnualLeaveHours().size(); i++) {
            String item = timeSheetData.getAnnualLeaveHours().get(i);
            String[] itemArr = item.split("---");
            addDayValue(sb, new BigDecimal(itemArr[1]), !startedDayBuilder);
            startedDayBuilder = true;
        }
        startedDayBuilder = false;
        sb.append(String.format("&Leave=%s&", timeSheetData.getTotalAnnualLeaveHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));

        //BEREAVEMENT LEAVE
        sb.append("BereavementDays=");
        for(int i = 0; i < timeSheetData.getBereavementLeaveHours().size(); i++) {
            String item = timeSheetData.getBereavementLeaveHours().get(i);
            String[] itemArr = item.split("---");
            addDayValue(sb, new BigDecimal(itemArr[1]), !startedDayBuilder);
            startedDayBuilder = true;
        }
        startedDayBuilder = false;
        sb.append(String.format("&Bereavement=%s&", timeSheetData.getTotalBereavementLeaveHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString()));

        sb.append(String.format("Notes=%s", (TextUtils.isEmpty(timeSheetData.getNotes())) ? "" : timeSheetData.getNotes()));

        return sb.toString();
    }

    private StringBuilder addDayValue(StringBuilder sb, BigDecimal value, boolean first)
    {
        if (!first)
        {
            sb.append(",");
        }
        if (value.compareTo(BigDecimal.ZERO) > 0)
        {
            sb.append(value.setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString());
        }
        return sb;
    }

    /// <summary>
    /// Gets the CSV filename.
    /// </summary>
    /// <returns>The CSV filename based on current profile and timesheet data.</returns>
    public String GetCSVFilename()
    {
        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
        return profileData.getLastName().toUpperCase() + " " + profileData.getFirstName().toUpperCase() + " - " + formatDate.format(timeSheetData.getStartDate().getTime()) + " to " + formatDate.format(timeSheetData.getEndDate().getTime()) + ".csv";
    }

    /// <summary>
    /// Gets the CSV file path.
    /// </summary>
    /// <returns>The CSV file path based on current profile and timesheet data.</returns>
    public String GetCSVFilePath()
    {
        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
        return formatDate.format(timeSheetData.getStartDate()) + " to " + formatDate.format(timeSheetData.getEndDate());
    }

}
