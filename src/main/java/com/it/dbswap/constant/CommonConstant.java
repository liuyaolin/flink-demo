package com.it.dbswap.constant;

import com.it.dbswap.util.SaltUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonConstant {


    public static final int VIN_HBASE_PARTITION = 200;
    public static final int VIN_HBASE_SALT_SEED = 1000;
    public static final String VIN_HBAE_FAMILY_NAME = "f";
    public static final String VIN_TIME_HBAE_COLUMN_NAME = "time";
    public static final String VIN_VALUE_HBAE_COLUMN_NAME = "val";
    public static final String VIN_NO_HBAE_COLUMN_NAME = "vin";
    public static final String VIN_DAY_HBAE_COLUMN_NAME = "day";
    private static final String VIN_BASE_HBASE_NAME_SPACE = "dbswap";
    private static final String VIN_BASE_HBASE_TABLE = ":dbswap_vin_time_data";
    public static final String FULL_SPLIT_KEY = "!!";

    private static final String PARSE_RESULT_BASE_HBASE_TABLE = ":dbswap_parse_vin_time_data";

    public static String getVinBaseHbaseTable(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String nameSpace =  CommonConstant.VIN_BASE_HBASE_NAME_SPACE + sdf.format(new Date());
        return nameSpace+VIN_BASE_HBASE_TABLE;
    }
    public static String getVinParseHbaseTable(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String nameSpace =  CommonConstant.VIN_BASE_HBASE_NAME_SPACE + sdf.format(new Date());
        return nameSpace+PARSE_RESULT_BASE_HBASE_TABLE;
    }
}
