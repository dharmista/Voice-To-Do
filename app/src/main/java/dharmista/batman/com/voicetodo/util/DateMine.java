package dharmista.batman.com.voicetodo.util;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Dharmista on 7/19/2017.
 */

@AllArgsConstructor
@Getter
public class DateMine {

    int date, month, year, hours, minutes, seconds;

    public DateMine(Date date) {
        this.date = date.getDate();
        this.month = date.getMonth();
        this.year = 1900 + date.getYear();
        this.hours = date.getHours();
        this.minutes = date.getMinutes();
        this.seconds = date.getSeconds();
    }
}
