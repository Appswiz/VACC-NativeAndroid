package au.com.vacc.timesheets.utils;

import android.util.Log;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeHelper {
    public static BigDecimal CalculateHours(BigDecimal days)
    {
        final BigDecimal hoursPerDay = new BigDecimal("7.6");
        BigDecimal totalHours = new BigDecimal("0");

        BigDecimal daysTimesHoursPerDay = days.multiply(hoursPerDay);
        totalHours = totalHours.add(daysTimesHoursPerDay);
        //note: we do NOT round anything here, as it is used elsewhere for calculations.
        // only round on UI if need be
        Log.v("sak", totalHours + " -- totalHours");
        return totalHours;
    }


    public static BigDecimal GetDaysFromHoursMinutes(BigDecimal hours, BigDecimal minutes)
    {
        final BigDecimal hoursPerDay = new BigDecimal("7.6");
        BigDecimal totalDays = new BigDecimal("0");

        BigDecimal minutesBy60 = minutes.divide(new BigDecimal("60"), 4, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal hoursPlusMinutesBy60 = hours.add(minutesBy60);
        BigDecimal tempTotal = hoursPlusMinutesBy60.divide(hoursPerDay, 4, BigDecimal.ROUND_HALF_EVEN);
        totalDays = totalDays.add(tempTotal);

        //note: we do NOT round anything here, as it is used elsewhere for calculations.
        // only round on UI if need be

        return totalDays;
    }

    public static String GetHoursMinutesFromDays(BigDecimal days)
    {
        final BigDecimal hoursPerDay = new BigDecimal("7.6");
        BigDecimal hours= days.multiply(hoursPerDay);
        BigDecimal truncatehours = hours.setScale(0, BigDecimal.ROUND_DOWN);
        BigDecimal minutes = truncatehours.subtract(hours);
        BigDecimal minutesBy60 = minutes.multiply(new BigDecimal("60")).abs();
        return truncatehours.toString() + "---" + minutesBy60.toString();
    }

    public static BigDecimal GetHoursFromHoursMinutes(BigDecimal hours, BigDecimal minutes)
    {
        BigDecimal minutesBy60 = minutes.divide(new BigDecimal("60"), 4, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal hoursPlusMinutesBy60 = hours.add(minutesBy60);
        return hoursPlusMinutesBy60;
    }



    public static Calendar FindNearestMondayFromNow()
    {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date todayWithZeroTime = null;
        try {
            todayWithZeroTime = formatter.parse(formatter.format(now.getTime()));
            now.setTime(todayWithZeroTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return now;
    }
}
