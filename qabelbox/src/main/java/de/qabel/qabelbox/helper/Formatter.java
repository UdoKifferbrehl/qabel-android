package de.qabel.qabelbox.helper;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.regex.Pattern;

import de.qabel.qabelbox.R;

/**
 * Created by danny on 14.01.2016.
 */
public class Formatter {

    private static final long DAY_IN_MILLIS = 86400000;
    private static final long HOUR_IN_MILLIS = 3600000;
    private static final long MINUTE_IN_MILLIS = 60000;

    private static final DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    private static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    private static final DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

    public static String formatDateShort(Date date) {

        return dateFormat.format(date);
    }

    public static String formatDateShort(long date) {

        return formatDateShort(new Date(date));
    }

    public static String formatDateTimeShort(Date date) {

        return dateTimeFormat.format(date);
    }

    public static String formatDateTimeShort(long date) {

        return formatDateTimeShort(new Date(date));
    }

    /**
     * Formats a Date to a String like "Fr. 20:35" or "15.02.15 20:35" if the date is more than a week in the past.
     *
     * @param time
     * @return
     */
    public static String formatDateTimeString(long time) {
        return DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
    }

    public static boolean isEMailValid(String email) {

        final String EMAIL_PATTERN = "^.+@.+$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }
}
