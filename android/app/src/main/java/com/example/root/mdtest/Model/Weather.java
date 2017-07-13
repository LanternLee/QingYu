package com.example.root.mdtest.Model;

import com.example.root.mdtest.R;

import java.util.List;

/**
 * Created by root on 2017/7/13.
 */

public class Weather {
    public static final String APPID = "rmdaxp9eqol0pku9";
    public static final String LOCATION = "WTW37HNSSJ68";
    public static final String LANGUAGE = "zh-Hans";
    public static final String UNIT = "c";
    public static final int[] WEATHERICON={
            R.mipmap.w0,R.mipmap.w1,R.mipmap.w2,R.mipmap.w3,
            R.mipmap.w4,R.mipmap.w5,R.mipmap.w6,R.mipmap.w7,
            R.mipmap.w8,R.mipmap.w9,R.mipmap.w10,R.mipmap.w11,
            R.mipmap.w12,R.mipmap.w13,R.mipmap.w14,R.mipmap.w15,
            R.mipmap.w16,R.mipmap.w17,R.mipmap.w18,R.mipmap.w19,
            R.mipmap.w20,R.mipmap.w21,R.mipmap.w22,R.mipmap.w23,
            R.mipmap.w24,R.mipmap.w25,R.mipmap.w26,R.mipmap.w27,
            R.mipmap.w28,R.mipmap.w29,R.mipmap.w30,R.mipmap.w31,
            R.mipmap.w32,R.mipmap.w33,R.mipmap.w34,R.mipmap.w35,
            R.mipmap.w36,R.mipmap.w37,R.mipmap.w38
    };

    public List<Item> results;
    public String status;

    public static class now{
        public String text;
        public String code;
    }

    public static class Item{
        public now now;
    }

}
