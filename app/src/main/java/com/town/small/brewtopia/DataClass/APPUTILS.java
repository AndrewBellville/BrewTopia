package com.town.small.brewtopia.DataClass;

import java.text.SimpleDateFormat;
import java.util.HashMap;
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
        }
    };



}
