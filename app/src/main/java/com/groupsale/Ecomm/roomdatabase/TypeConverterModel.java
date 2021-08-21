package com.groupsale.Ecomm.roomdatabase;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Sanjeev on 08,June,2021
 * therealsanjeev0@gmail.com
 */
public class TypeConverterModel {


    @TypeConverter
    public List<String> storedStringToList(String value) {
        List<String> list = Arrays.asList(value.split("\\s*,\\s*"));
        return list;
    }

    @TypeConverter
    public String ListToStoredString(List<String> cl) {
        String value = "";
        for (String lang :cl)
            value += lang + ",";

        return value;
    }
}
