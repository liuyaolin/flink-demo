package com.it.dbswap.util;

public class TableCountHashUtil {


    public static String getKeyHash(String key,int partition) {
        int hash =  Math.abs(key.hashCode())%partition;
        return ""+hash;
    }

}
