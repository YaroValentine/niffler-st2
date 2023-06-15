package niffler.api.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    public static Date convertFrontDateToJavaDate(String date) {
        DateFormat frontDate = new SimpleDateFormat("dd MMM yy", new Locale("en", "EN"));
        try {
            return frontDate.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public static Date convertJavaDateToFrontDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}
