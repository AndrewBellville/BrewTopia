package com.town.small.brewtopia.DataClass;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Andrew on 4/3/2016.
 */
public class APPUTILS {

    //***********************Date Formatting**********************************
    public static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static SimpleDateFormat dateFormatCompare = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.getDefault());

    public static String StringDateCompare(String date)
    {
        //strip off time on date string
        return  date.substring(0,10);
    }

    //********************Brew Style Hash Map Name/Color**********************
    public static final HashMap StyleMap = new HashMap(){
        {
            put("None","");
            put("Amber","#FF0000");
            put("Blonde","#ffff00");
            put("Brown","#A52A2A");
            put("Cream","#f4a460");
            put("Dark","#654321");
            put("Fruit","#FFC0CB");
            put("Golden","#ffff00");
            put("Honey","#ffff00");
            put("IPA","#00ff00");
            put("Wheat","#ffff00");
            put("Red","#FF0000");
            put("Pilsner","#ffff00");
            put("Pale","#ffff00");
            put("Light","#ffff00");
            put("Hefeweizen","#DAA520");
        }
    };

    //TODO: Add this to the DB so users can add more
    //********************Unit Of Measure List**********************
    public static final List<String> UofM = new ArrayList<String>(){
        {
            add("unit");
            add("oz");
            add("g");
            add("cups");
            add("lbs");
        }
    };

    //********************Calculations**********************
    public static Double GetABV(Double aOG, Double aFG)
    {
        return ((aOG - aFG)/.75);
    }
    public static Double GetTruncatedABV(Double aABV)
    {
        Double truncatedDouble = new BigDecimal(Double.toString(aABV))
                .setScale(4, BigDecimal.ROUND_HALF_UP)
                .doubleValue();

        return truncatedDouble;
    }
    public static Double GetTruncatedABVPercent(Double aABV)
    {
        Double truncatedDouble = new BigDecimal(Double.toString(aABV*100))
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();

        return truncatedDouble;
    }

    //********************WebAPI**********************
    public static String WEBAPIRURL = "http://smalltowndev.com/index.php/BrewTopiaMobileAPI";

    ///********************Image Conversion**********************
    public static Bitmap GetBitmapFromByteArr(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
    public static byte[] GetBitmapByteArray(Bitmap aBitmap)
    {
        byte[] bArray =new byte[1];
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            aBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bArray = bos.toByteArray();

        }
        catch (Exception e){}
        return bArray;
    }
}
