package dharmista.batman.com.voicetodo.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.NoArgsConstructor;

/**
 * Created by Dharmista on 7/21/2017.
 */

@NoArgsConstructor
public class DateUtil {

    Date date = null;

    private static String[] months = {
            "JAN",
            "FEB",
            "MAR",
            "APR",
            "MAY",
            "JUN",
            "JUL",
            "AUG",
            "SEP",
            "OCT",
            "NOV",
            "DEC"
    };

    public static String[] months_ = months;

    public DateUtil(Date date) {
        this.date = date;
    }

    public String getFormattedDate() {
        if(date == null) {
            date = new Date();
        } String ampm = "AM";
        int hour = date.getHours();
        if(hour > 12) {
            hour -= 12;
            ampm = "PM";
        }
        return String.format("%d-%s-%s %d:%d %s", date.getDate(), months[date.getMonth()], date.getYear(), hour , date.getMinutes(), ampm);
    }

    public DateUtil(String year, String month, String day, String hour, String minute) {
        date = new Date(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day),Integer.parseInt(hour),Integer.parseInt(minute));
    }

    public static String getFormattedPathway(long milliseconds){
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds - minutes * 60000);
        long hours = minutes / 60;
        minutes = minutes - 60 * hours;
        long days = hours / 24;
        hours = hours - 24 * days;
        String timeo = "";
        if(days > 0)
            timeo += (days + " days, ");
        if(hours > 0)
            timeo += (hours + " hours, ");
        if(minutes > 0)
            timeo += (minutes + " minutes, ");
        if(seconds > 0)
            timeo += (seconds + " seconds");
        return timeo;
    }
}
