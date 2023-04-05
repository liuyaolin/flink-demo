package com.it.dbswap.util;

/**
 * @description:
 * @author: yanghuazhao-01389603
 * @date: 2020-05-25 16:49
 */

import com.it.dbswap.constant.CommonConstant;

/**
 *  pis取数据接口  10SF1022361912037 ：预分区的运单号
 */

//  curl http://10.150.66.44:9999/pis-compare/getDataService/geCompare/pis:pis_waybill_compare/f/10SF1022361912037 > 10SF1022361912037.txt


public class HashUtil {

    /**
     * md5取模，注意这个函数的divisor不能超过1000，
     *@author Lin Jian (01376083)
     *@date 2018年11月5日
     * @param key
     * @param divisor 除数：生产环境配100，测试环境配10，
     * @return
     *
     */
    public static String getMD5Str(String key, int divisor) {
        int hash = key.hashCode();
        if(hash < 0) {
            hash = 0 - hash;
        }

        if(divisor > 100 && divisor <= 1000) {
            if(hash >= 10 && hash < 100){
                return "0" + hash + key;
            }else if(hash < 10) {
                return "00" + hash + key;
            }else {
                return hash + key;
            }
        }else if(divisor > 10 && divisor <= 100) {
            if(hash < 10) {
                return "0" + hash + key;
            }else {
                return hash + key;
            }
        }else if(divisor <= 10) {
            return hash + key;
        }else {
            //大于1000的divisor不处理
            return "";
        }
    }

    //public static String


    public static void main(String[] args) {

//        String waybillno ="SF1026074922702";
//
//        String saltWaybillNo = getMD5Str(waybillno, 100);
//        StringBuilder sb = new StringBuilder();
//        sb.append("curl http://10.150.66.44:9999/pis-compare/getDataService/geCompare/pis:pis_waybill_compare/f/");
//        sb.append(saltWaybillNo);
//        sb.append(" > ");
//        sb.append(waybillno + ".txt");
//
//        System.out.println(saltWaybillNo);
//        System.out.println(sb.toString());

      String s =  "11"+ CommonConstant.FULL_SPLIT_KEY+"yyyy"
                +CommonConstant.FULL_SPLIT_KEY+"hh";
      System.out.println(s.split(CommonConstant.FULL_SPLIT_KEY).length);

    }
}
