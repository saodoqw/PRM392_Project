package com.example.prm392.Data;

import androidx.room.TypeConverter;

import java.sql.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Double fromDoubleString(String value) {
        return value == null ? null : Double.parseDouble(value);
    }

    @TypeConverter
    public static String doubleToString(Double price) {
        return price == null ? null : price.toString();
    }

    @TypeConverter
    public static Long fromLongString(String value) {
        return value == null ? null : Long.parseLong(value);
    }

    @TypeConverter
    public static String longToString(Long id) {
        return id == null ? null : id.toString();
    }
}
