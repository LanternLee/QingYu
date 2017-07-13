package com.example.root.mdtest.Model;

import java.util.List;

/**
 * Created by root on 2017/7/13.
 */

public class UblMess {
    public static final String AVA = "avail";
    public static final String UNA = "unavail";

    public int status;

    public List<Item> retVal;

    public static class Item{
        List<Integer> lockid;
    }
}
