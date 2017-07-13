package com.example.root.mdtest.Model;

import java.util.List;

/**
 * Created by root on 2017/7/13.
 */

public class Weather {
    public static final String APPID = "rmdaxp9eqol0pku9";
    public static final String LOCATION = "WTW37HNSSJ68";
    public static final String LANGUAGE = "zh-Hans";
    public static final String UNIT = "c";

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
