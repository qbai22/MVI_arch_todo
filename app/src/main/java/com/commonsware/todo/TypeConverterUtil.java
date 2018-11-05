package com.commonsware.todo;

import android.arch.persistence.room.TypeConverter;

import java.util.Calendar;

/**
 * Created by Vladimir Kraev
 */


public class TypeConverterUtil {

    @TypeConverter
    public static Long fromCalendar(Calendar calendar){
        if(calendar == null) return null;

        return calendar.getTimeInMillis();
    }

    @TypeConverter
    public static Calendar toCalendar(Long millisSinceEpoch){
        if(millisSinceEpoch == null) return null;

        Calendar resultCalendar = Calendar.getInstance();
        resultCalendar.setTimeInMillis(millisSinceEpoch);

        return resultCalendar;
    }

}
