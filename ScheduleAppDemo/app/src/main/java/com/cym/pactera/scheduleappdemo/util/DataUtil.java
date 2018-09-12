package com.cym.pactera.scheduleappdemo.util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
/**
 * Created by pactera on 2018/8/31.
 */

public class DataUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 E");
    public static ArrayList<String> getDate(){
        ArrayList<String> arrayList=new ArrayList<String>();
        Date d = new Date();

        Date date = getMonthStart(d);
        Date monthEnd = getMonthEnd(d);
        while (!date.after(monthEnd)) {
            arrayList.add(sdf.format(date));
            date = getNext(date);
        }
        return arrayList;
    }

    private static Date getMonthStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
//        calendar.add(Calendar.MONTH,-1);
        int index = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, 0);
        return calendar.getTime();
    }

    private static Date getMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, +7);
        int index = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, (-index));
        return calendar.getTime();
    }

    private static Date getNext(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}
