package au.com.vacc.timesheets.model;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonNode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import au.com.vacc.timesheets.utils.TimeSheetCore;
import au.com.vacc.timesheets.utils.Utils;

public class TimeSheetData implements Serializable {


    private Calendar StartDate;
    private Calendar EndDate;
    private BigDecimal WorkHours;
    private BigDecimal DoubleTimeHours;
    private BigDecimal TimeAndAHalfHours;
    private BigDecimal TAFEHours;
    private String Notes;
    private String EmployerCode;
    private BigDecimal TotalOvertimeHours;
    private BigDecimal TotalSickLeaveHours;
    private BigDecimal TotalAnnualLeaveHours;
    private BigDecimal TotalBereavementLeaveHours;
    private BigDecimal TotalVACCTimeHours;
    private BigDecimal TotalLeaveHours;
    private BigDecimal TotalLoggedHours;
    private List<String> OvertimeHours;
    private List<String> SickLeaveHours;
    private List<String> AnnualLeaveHours;
    private List<String> BereavementLeaveHours;
    private List<String> VACCTimeHours;

    public TimeSheetData() {
        resetData();
    }

    public void calculateTimeHalfAndDoubleTime() {
        BigDecimal totalTimeAndAHalfHours = new BigDecimal("0");
        BigDecimal totalDoubleTimeHours =  new BigDecimal("0");
        BigDecimal overtimeHrs;
        for (int i = 0; i < getOvertimeHours().size(); i++) {
            String obj = getOvertimeHours().get(i);
            String[] objArr = obj.split("---");
            overtimeHrs = new BigDecimal(objArr[1]);
            if (objArr[0].equals("Sunday")) {
                totalDoubleTimeHours = totalDoubleTimeHours.add(overtimeHrs);
                continue;
            }

            if (overtimeHrs.compareTo(TimeSheetCore.HoursUntilOvertime) > 0) {
                totalTimeAndAHalfHours = totalTimeAndAHalfHours.add(TimeSheetCore.HoursUntilOvertime);
                totalDoubleTimeHours = totalDoubleTimeHours.add(overtimeHrs.subtract(TimeSheetCore.HoursUntilOvertime));
            } else {
                totalTimeAndAHalfHours = totalTimeAndAHalfHours.add(overtimeHrs);
            }
        }

        //set instance properties
        TimeAndAHalfHours = totalTimeAndAHalfHours;
        DoubleTimeHours = totalDoubleTimeHours;
    }

    /// <summary>
    /// Reset this instance.
    /// </summary>
    public void resetData() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date todayWithZeroTime = null;
        StartDate = Calendar.getInstance();
        StartDate.set(Calendar.DAY_OF_YEAR, Calendar.MONDAY);

        try {
            todayWithZeroTime = formatter.parse(formatter.format(StartDate.getTime()));
            StartDate.setTime(todayWithZeroTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.v("sak", "startDate " + StartDate.getTimeInMillis());

        EndDate = Calendar.getInstance();
        EndDate.set(Calendar.DAY_OF_YEAR, Calendar.SUNDAY);
        try {
            todayWithZeroTime = formatter.parse(formatter.format(EndDate.getTime()));
            EndDate.setTime(todayWithZeroTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.v("sak", "EndDate " + EndDate.getTimeInMillis());

        WorkHours = new BigDecimal("0");
        DoubleTimeHours = new BigDecimal("0");
        TimeAndAHalfHours = new BigDecimal("0");
        TAFEHours = new BigDecimal("0");
        Notes = "";
        EmployerCode = "";

        resetSickLeaveHours();
        resetOvertimeHours();
        resetAnnualLeaveHours();
        resetBereavementLeaveHours();
        resetVACCTimeHours();
        calculateTimeHalfAndDoubleTime();
    }

    public void resetOvertimeHours() {
        OvertimeHours = new ArrayList<String>();
        OvertimeHours.add("Monday" + "---" + 0);
        OvertimeHours.add("Tuesday" + "---" + 0);
        OvertimeHours.add("Wednesday" + "---" + 0);
        OvertimeHours.add("Thursday" + "---" + 0);
        OvertimeHours.add("Friday" + "---" + 0);
        OvertimeHours.add("Saturday" + "---" + 0);
        OvertimeHours.add("Sunday" + "---" + 0);

    }

    public void resetSickLeaveHours() {
        SickLeaveHours = new ArrayList<String>();
        SickLeaveHours.add("Monday" + "---" + 0);
        SickLeaveHours.add("Tuesday" + "---" + 0);
        SickLeaveHours.add("Wednesday" + "---" + 0);
        SickLeaveHours.add("Thursday" + "---" + 0);
        SickLeaveHours.add("Friday" + "---" + 0);
        SickLeaveHours.add("Saturday" + "---" + 0);
        SickLeaveHours.add("Sunday" + "---" + 0);
    }

    public void resetAnnualLeaveHours() {
        AnnualLeaveHours = new ArrayList<String>();
        AnnualLeaveHours.add("Monday" + "---" + 0);
        AnnualLeaveHours.add("Tuesday" + "---" + 0);
        AnnualLeaveHours.add("Wednesday" + "---" + 0);
        AnnualLeaveHours.add("Thursday" + "---" + 0);
        AnnualLeaveHours.add("Friday" + "---" + 0);
        AnnualLeaveHours.add("Saturday" + "---" + 0);
        AnnualLeaveHours.add("Sunday" + "---" + 0);
    }

    public void resetBereavementLeaveHours() {
        BereavementLeaveHours = new ArrayList<String>();
        BereavementLeaveHours.add("Monday" + "---" + 0);
        BereavementLeaveHours.add("Tuesday" + "---" + 0);
        BereavementLeaveHours.add("Wednesday" + "---" + 0);
        BereavementLeaveHours.add("Thursday" + "---" + 0);
        BereavementLeaveHours.add("Friday" + "---" + 0);
        BereavementLeaveHours.add("Saturday" + "---" + 0);
        BereavementLeaveHours.add("Sunday" + "---" + 0);
    }

    /// <summary>
    /// Resets the VACC time hours.
    /// </summary>
    public void resetVACCTimeHours() {
        VACCTimeHours = new ArrayList<String>();
        VACCTimeHours.add("Monday" + "---" + 0);
        VACCTimeHours.add("Tuesday" + "---" + 0);
        VACCTimeHours.add("Wednesday" + "---" + 0);
        VACCTimeHours.add("Thursday" + "---" + 0);
        VACCTimeHours.add("Friday" + "---" + 0);
        VACCTimeHours.add("Saturday" + "---" + 0);
        VACCTimeHours.add("Sunday" + "---" + 0);
    }

    public Calendar getStartDate() {
        return StartDate;
    }

    public Calendar getEndDate() {
        return EndDate;
    }

    public BigDecimal getWorkHours() {
        if(WorkHours !=  null) {
            return WorkHours;
        } else {
            return new BigDecimal("0");
        }
    }

    public BigDecimal getDoubleTimeHours() {
        if(DoubleTimeHours !=  null) {
            return DoubleTimeHours;
        } else {
            return new BigDecimal("0");
        }
    }

    public BigDecimal getTimeAndAHalfHours() {
        if(TimeAndAHalfHours !=  null) {
            return TimeAndAHalfHours;
        } else {
            return new BigDecimal("0");
        }
    }

    public BigDecimal getTAFEHours() {
        if(TAFEHours !=  null) {
            return TAFEHours;
        } else {
            return new BigDecimal("0");
        }
    }

    public String getNotes() {
        return Notes;
    }

    public String getEmployerCode() {
        return EmployerCode;
    }

    public BigDecimal getTotalOvertimeHours() {

        TotalOvertimeHours = new BigDecimal("0");
        for (int i = 0; i < OvertimeHours.size(); i++) {
            String[] itemArr = OvertimeHours.get(i).split("---");
            TotalOvertimeHours = TotalOvertimeHours.add(new BigDecimal(itemArr[1]));
        }
        return TotalOvertimeHours;

    }

    public BigDecimal getTotalSickLeaveHours() {

        TotalSickLeaveHours = new BigDecimal("0");
        for (int i = 0; i < SickLeaveHours.size(); i++) {
            String[] itemArr = SickLeaveHours.get(i).split("---");
            TotalSickLeaveHours = TotalSickLeaveHours.add(new BigDecimal(itemArr[1]));
        }
        return TotalSickLeaveHours;

    }

    public BigDecimal getTotalAnnualLeaveHours() {

        TotalAnnualLeaveHours = new BigDecimal("0");
        for (int i = 0; i < AnnualLeaveHours.size(); i++) {
            String[] itemArr = AnnualLeaveHours.get(i).split("---");
            TotalAnnualLeaveHours = TotalAnnualLeaveHours.add(new BigDecimal(itemArr[1]));
        }
        return TotalAnnualLeaveHours;

    }

    public BigDecimal getTotalBereavementLeaveHours() {
        TotalBereavementLeaveHours = new BigDecimal("0");
        for (int i = 0; i < BereavementLeaveHours.size(); i++) {
            String[] itemArr = BereavementLeaveHours.get(i).split("---");
            TotalBereavementLeaveHours = TotalBereavementLeaveHours.add(new BigDecimal(itemArr[1]));
        }
        return TotalBereavementLeaveHours;

    }

    public BigDecimal getTotalVACCTimeHours() {
        TotalVACCTimeHours = new BigDecimal("0");
        for (int i = 0; i < VACCTimeHours.size(); i++) {
            String[] itemArr = VACCTimeHours.get(i).split("---");
            TotalVACCTimeHours = TotalVACCTimeHours.add(new BigDecimal(itemArr[1]));
        }
        return TotalVACCTimeHours;

    }

    public BigDecimal getTotalLeaveHours() {
        TotalLeaveHours = new BigDecimal("0");
        TotalLeaveHours = getTotalSickLeaveHours().add(getTotalAnnualLeaveHours()).add(getTotalBereavementLeaveHours()).add(getTotalVACCTimeHours());
        return TotalLeaveHours;
    }

    public BigDecimal getTotalLoggedHours() {
        TotalLoggedHours = new BigDecimal("0");
        TotalLoggedHours = getWorkHours().add(getTotalLeaveHours()).add(getTAFEHours());
        return TotalLoggedHours;

    }

    public List<String> getOvertimeHours() {
        return OvertimeHours;
    }

    public List<String> getSickLeaveHours() {
        return SickLeaveHours;
    }

    public List<String> getAnnualLeaveHours() {
        return AnnualLeaveHours;
    }

    public List<String> getBereavementLeaveHours() {
        return BereavementLeaveHours;
    }

    public List<String> getVACCTimeHours() {
        return VACCTimeHours;
    }

    public void setStartDate(Calendar startDate) {
        StartDate = startDate;
        EndDate.setTime(StartDate.getTime());
        EndDate.add(Calendar.DAY_OF_YEAR, 6);

    }



    public void setWorkHours(BigDecimal workHours) {

        WorkHours = workHours;

        //update additional items automatically - rule
        if(WorkHours.compareTo(TimeSheetCore.HoursTarget) >= 0)
        {
            TAFEHours = new BigDecimal("0");

            if(WorkHours.compareTo(TimeSheetCore.HoursTarget) > 0)
            {
                //TODO: confirm?
                DoubleTimeHours = workHours.subtract(TimeSheetCore.HoursTarget);
            }
        }
    }

    public void setTAFEHours(BigDecimal TAFEHours) {
        this.TAFEHours = TAFEHours;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public void setEmployerCode(String employerCode) {
        EmployerCode = employerCode;
    }

    public void setTotalOvertimeHours(BigDecimal totalOvertimeHours) {
        TotalOvertimeHours = totalOvertimeHours;
    }

    public void setTotalSickLeaveHours(BigDecimal totalSickLeaveHours) {
        TotalSickLeaveHours = totalSickLeaveHours;
    }

    public void setTotalAnnualLeaveHours(BigDecimal totalAnnualLeaveHours) {
        TotalAnnualLeaveHours = totalAnnualLeaveHours;
    }

    public void setTotalBereavementLeaveHours(BigDecimal totalBereavementLeaveHours) {
        TotalBereavementLeaveHours = totalBereavementLeaveHours;
    }

    public void setTotalVACCTimeHours(BigDecimal totalVACCTimeHours) {
        TotalVACCTimeHours = totalVACCTimeHours;
    }

    public void setTotalLeaveHours(BigDecimal totalLeaveHours) {
        TotalLeaveHours = totalLeaveHours;
    }

    public void setTotalLoggedHours(BigDecimal totalLoggedHours) {
        TotalLoggedHours = totalLoggedHours;
    }

    public void setOvertimeHours(List<String> overtimeHours) {
        OvertimeHours = overtimeHours;
    }

    public void setSickLeaveHours(List<String> sickLeaveHours) {
        SickLeaveHours = sickLeaveHours;
    }

    public void setAnnualLeaveHours(List<String> annualLeaveHours) {
        AnnualLeaveHours = annualLeaveHours;
    }

    public void setBereavementLeaveHours(List<String> bereavementLeaveHours) {
        BereavementLeaveHours = bereavementLeaveHours;
    }

    public void setVACCTimeHours(List<String> VACCTimeHours) {
        this.VACCTimeHours = VACCTimeHours;
    }
}

