package com.it.dbswap.util;

public class RowKeyHashUtil {

    public static String getMD5Str(String key,int partition) {
        int hash =  Math.abs(key.hashCode())%partition;
        if(partition==150){
            if(hash<10)
                return "00"+hash+key;
            if(hash<100)
                return "0"+hash+key;
        }
        return hash+key;
    }

    public static String getKeyHash(String key,int partition) {
        int hash =  Math.abs(key.hashCode())%partition;
        if(partition==100 && hash<10)
            return "0"+hash;
        return ""+hash;
    }

    public static void main(String[] args) {
        String key="LZZ1CLWB0MW790330";
        System.out.println(getMD5Str(key,150));
        System.out.println(SaltUtil.generateSaltOrigin(key,150));
        System.out.println(SaltUtil.generateSalt(key,150,10000));
    }
}
