package com.baemin.woowahan_presentation_android.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by leetaejun on 2016. 2. 15..
 */
public class DateConvertor {
    private static final String SERVER_FORMAT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String CLIENT_FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static String convertToDate(String inputDate) throws Exception {
        String dateTime = inputDate;
        SimpleDateFormat format = new SimpleDateFormat(SERVER_FORMAT_DATE_TIME);
        Date parseDate = null;
        String convertedDate = null;

        try {
            parseDate = format.parse(dateTime);
            DateFormat dateFormatNeeded = new SimpleDateFormat(CLIENT_FORMAT_DATE_TIME);
            convertedDate = dateFormatNeeded.format(parseDate);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        return convertedDate;
    }

    public static String utcToLocaltime(String datetime) throws Exception {
        String locTime = null;
        //  해당 국가 일시 확인 할 때, 한쿸은 +9
        TimeZone tz = TimeZone.getTimeZone("GMT+09:00");
//        TimeZone tz = TimeZone.getDefault();
        SimpleDateFormat sdf = new SimpleDateFormat(CLIENT_FORMAT_DATE_TIME);
        try {
            Date parseDate = sdf.parse(datetime);
            long milliseconds = parseDate.getTime();
            int offset = tz.getOffset(milliseconds);
            locTime = sdf.format(milliseconds + offset);
            locTime = locTime.replace("+0000", "");
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return locTime;
    }

}
