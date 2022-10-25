package com.luffy.testautomation.prjhelpers;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

import com.luffy.testautomation.appautomation.config.WDAServer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateHelpers {
    private static final Logger log = LoggerFactory.getLogger(WDAServer.class);

    /**
     * Checks if two dates are on the same day ignoring time.
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is <code>null</code>
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * Checks if two calendars represent the same day ignoring time.
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * Checks if a date is today.
     *
     * @param date the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    /**
     * Checks if a calendar date is today.
     *
     * @param cal the calendar, not altered, not null
     * @return true if cal date is today
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }

    /**
     * Checks if the first date is before the second date ignoring time.
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is before the second date day.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isBeforeDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isBeforeDay(cal1, cal2);
    }

    /**
     * Checks if the first calendar date is before the second calendar date ignoring time.
     *
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is before cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isBeforeDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return false;
        return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Checks if the first date is after the second date ignoring time.
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is after the second date day.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isAfterDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isAfterDay(cal1, cal2);
    }

    /**
     * Checks if the first calendar date is after the second calendar date ignoring time.
     *
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is after cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return true;
        return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Checks if a date is after today and within a number of days in the future.
     *
     * @param date the date to check, not altered, not null.
     * @param days the number of days.
     * @return true if the date day is after today and within days in the future .
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Date date, int days) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return isWithinDaysFuture(cal, days);
    }

    /**
     * Checks if a calendar date is after today and within a number of days in the future.
     *
     * @param cal the calendar, not altered, not null
     * @param days the number of days.
     * @return true if the calendar date day is after today and within days in the future .
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Calendar cal, int days) {
        if (cal == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar today = Calendar.getInstance();
        Calendar future = Calendar.getInstance();
        future.add(Calendar.DAY_OF_YEAR, days);
        return (isAfterDay(cal, today) && !isAfterDay(cal, future));
    }

    /** Returns the given date with the time set to the start of the day. */
    public static Date getStart(Date date) {
        return clearTime(date);
    }

    /** Returns the given date with the time values cleared. */
    public static Date clearTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * Determines whether or not a date has any time values (hour, minute, seconds or
     * millisecondsReturns the given date with the time values cleared.
     */

    /**
     * Determines whether or not a date has any time values.
     *
     * @param date The date.
     * @return true iff the date is not null and any of the date's hour, minute, seconds or
     *     millisecond values are greater than zero.
     */
    public static boolean hasTime(Date date) {
        if (date == null) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (c.get(Calendar.HOUR_OF_DAY) > 0) {
            return true;
        }
        if (c.get(Calendar.MINUTE) > 0) {
            return true;
        }
        if (c.get(Calendar.SECOND) > 0) {
            return true;
        }
        if (c.get(Calendar.MILLISECOND) > 0) {
            return true;
        }
        return false;
    }

    /** Returns the given date with time set to the end of the day */
    public static Date getEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * Returns the maximum of two dates. A null date is treated as being less than any non-null
     * date.
     */
    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return d1.after(d2) ? d1 : d2;
    }

    /**
     * Returns the minimum of two dates. A null date is treated as being greater than any non-null
     * date.
     */
    public static Date min(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return d1.before(d2) ? d1 : d2;
    }

    /** The maximum date possible. */
    public static Date MAX_DATE = new Date(Long.MAX_VALUE);

    /** dateDisplayed example "Jul 21, 2019" ; dateFormat example "MMM dd, yyyy" */
    public static Date dateFormatter(String dateDisplayed, String dateFormat) {
        Date parsedDate = null;
        SimpleDateFormat formatter =
                new SimpleDateFormat(dateFormat, new Locale(System.getProperty("locale")));
        try {
            parsedDate = formatter.parse(dateDisplayed);
        } catch (ParseException e) {
            log.info(getStackTrace(e));
        }
        return parsedDate;
    }

    /** Input date and dateFormat (example "yyyy, MM-dd"), return formatted string of date */
    public static String dateParser(Date parsedDate, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(parsedDate);
    }

    public String dateFormatterMoveMoneyJourney(String date, String countryName) {
        String[] dateInArray;
        String dateSelected;
        String formatted = null;
        switch (countryName) {
            case "us":
                formatted = date.substring(6, date.length()).replaceAll("[^\\S*]", "");
                break;
            case "mns":
                dateInArray = date.split(",");
                dateSelected = dateInArray[dateInArray.length - 1];
                formatted = dateSelected.replaceAll("[^\\S*]", "");
                break;
            case "expat":
                formatted = date.substring(13, date.length()).replaceAll("[^\\S*]", "");
                break;
            case "Malaysia":
                dateInArray = date.split(",");
                dateSelected = dateInArray[dateInArray.length - 1];
                formatted = dateSelected.replaceAll(" ", "");
        }
        return formatted;
    }

    /*  This method will check if the order of
     *  date array list is in order of Chronologically
     *  Then a boolean value will be returned */
    public static boolean isDateArrayListChronologicallyOrdered(List<String> actualDateList) {
        ArrayList<String> dateList = new ArrayList<String>();
        Date date;

        for (String dates : actualDateList) {
            date = DateHelpers.dateFormatter(dates, "dd MMMM yyyy");
            dateList.add(dateParser(date, "yyyy/MM/dd"));
        }

        ArrayList<String> expectedDateList = (ArrayList<String>) dateList.clone();
        expectedDateList.sort(Comparator.reverseOrder());
        actualDateList.clear();
        return expectedDateList.equals(dateList);
    }

    /*  This method will return the formatted date
     *  Number of days will added to current date
     * Then the date will be formatted on the basis of pattern which is passed through parameter */
    public String getDateInFuture(int numberOfDays, String pattern, String deliveryDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        LocalDate formattedDeliveryDate = LocalDate.parse(deliveryDate, formatter);
        LocalDate localDate = formattedDeliveryDate.plusDays(numberOfDays);
        DateTimeFormatter calendarFormattedDeliveryDate = DateTimeFormatter.ofPattern(pattern);
        return calendarFormattedDeliveryDate.format(localDate);
    }

    public String getFutureDate(int numberOfDays, String pattern, String currentDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate formattedDeliveryDate = LocalDate.parse(currentDate, formatter);
        LocalDate localDate = formattedDeliveryDate.plusDays(numberOfDays);
        DateTimeFormatter calendarFormattedDeliveryDate = DateTimeFormatter.ofPattern(pattern);
        return calendarFormattedDeliveryDate.format(localDate);
    }

    public String getFutureDateAsExpectPattern(
            int numberOfDays, String pattern, String currentDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate formattedDeliveryDate = LocalDate.parse(currentDate, formatter);
        LocalDate localDate = formattedDeliveryDate.plusDays(numberOfDays);
        DateTimeFormatter calendarFormattedDeliveryDate = DateTimeFormatter.ofPattern(pattern);
        return calendarFormattedDeliveryDate.format(localDate);
    }

    public int getMonthNumberFromDate(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate formattedDeliveryDate = LocalDate.parse(dateString, formatter);
        return formattedDeliveryDate.getMonthValue();
    }

    /**
     * Calculates the total days difference between today and date. This is specific to RPQ product
     * as difference is calculated to check if the pop up should be displayed or not.
     *
     * @param date till which difference should be calculated.
     * @return calculated days.
     * @throws IllegalArgumentException if the futureDate is null
     */
    public long getDateDifference(String date) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        DateFormat formatter = new SimpleDateFormat("dd/MM");
        String currentDate = formatter.format(new Date());
        try {
            long difference =
                    formatter.parse(date).getTime() - formatter.parse(currentDate).getTime();
            return TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
        } catch (Exception exception) {
            log.info("EXCEPTION " + exception);
            return 0;
        }
    }

    /**
     * Return current date as per time zone
     *
     * @param dateFormat,timeZone For eg dateFormat = dd/MMM/yyyy, timeZone = GMT+8
     * @return current date of the specified time zone in format passed as parameter.
     * @throws IllegalArgumentException if the dateFormat and timeZone is null
     */
    public static String getDateAsPerTimeZone(String dateFormat, String timeZone) {
        if (dateFormat == null || timeZone == null) {
            throw new IllegalArgumentException("date format and timezone must not be null");
        }
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        return formatter.format(new Date());
    }

    /**
     * Returns true if the date is in correct format else return false
     *
     * @param dateFormat,date eg dateFormat = "dd/MMM/yyyy" date = "01/Jan/2020"
     * @return true if format is correct else false
     * @throws IllegalArgumentException if the dateFormat and timeZone is null
     */
    public static boolean isDateFormatCorrect(String dateFormat, String date) {
        if (dateFormat == null || date == null) {
            throw new IllegalArgumentException("date format and date must not be null");
        }
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            formatter.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy");
        String nowDate = DateFor.format(date);
        return nowDate;
    }

    public static String get18MonthsBeforeDateFromCurrent() {
        SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy");
        Calendar currentDateBefore18Months = Calendar.getInstance();
        currentDateBefore18Months.add(Calendar.MONTH, -18);
        String date_before_18months = DateFor.format(currentDateBefore18Months.getTime());
        return date_before_18months;
    }

    public static String getUTCTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = df.format(new Date());
        return formattedDate;
    }
}
