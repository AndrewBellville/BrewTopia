package com.dev.town.small.brewtopia.DataClass;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Andrew on 4/3/2016.
 */
public class APPUTILS {

    ///********************If we re going to write to log**********************
    public static boolean isLogging = true;

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
            put("Amber","#330000");
            put("Blonde","#ffff99");
            put("Red","#660000");
            put("Hefeweizen","#DAA520");
            put("Belgian","#DAA520");
            put("Bock","#2F4F4F");
            put("Brown Ale","#A52A2A");
            put("Cream","#FFA500");
            put("Fruit","#9370DB");
            put("IPA","#008000");
            put("Pale Ale","#EEE8AA");
            put("Pilsner","#FFFF00");
            put("Porter","#800000");
            put("Scottish","#8B0000");
            put("Stout","#000000");
            put("Wheat","#FFD700");
        }
    };

    //********************Brew SRM Hash Map SRM#/Color**********************
    public static final HashMap SRMMap = new LinkedHashMap(){
        {
            put(0,"#F9F75400");
            put(1,"#F9F754");
            put(3,"#F7F514");
            put(4,"#EBE619");
            put(6,"#D5BC24");
            put(8,"#C0923C");
            put(10,"#BF8039");
            put(13,"#BB6834");
            put(17,"#8C4227");
            put(20,"#5D331A");
            put(24,"#271717");
            put(29,"#0F0B0A");
            put(35,"#080607");
            put(40,"#040404");
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

    //********************Brew Method List**********************
    public static final List<String> BrewMethod = new ArrayList<String>(){
        {
            add("Extract");
            add("BIAB");
            add("All Grain");
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

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, Toolbar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
