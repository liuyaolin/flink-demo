package com.it.dbswap.util.proputil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;

/**
 * @description:
 * @author: huangchm-01328365
 * @date: 2020-04-16 10:50
 */
public class KafkaPropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(KafkaPropertiesUtil.class);

    private static Properties props;


    static {
        String fileName = "kafka.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(KafkaPropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
            logger.info("加载 kafka.properties 配置");

            if(props.size()==0){
                throw new RuntimeException("kafka.properties的配置项为空");
            }


            Iterator<String> it =  props.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key = it.next();
                logger.info("{}={}",key, props.get(key));
            }



        } catch (IOException e) {
            logger.error("配置文件读取异常",e);
        }
    }

    //自定义俩个get方法，方便调用工具类读取properties文件的属性
    public static String getProperty(String key){
        String value= props.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
            String message= "KafkaPropertiesUtil配置项 "+ key + "不存在";
            logger.error(message);
             throw new RuntimeException(message);
        }
        return value.trim();
    }

    public static String getProperty(String key,String defaultValue){
        String value= props.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }











    // *********************  swap source   begin *****************************************

    public static String getSwapSourceBroker(){
        return getProperty("swapSourceBroker");
    }

    public static String getSwapSourceZookeeper(){
        return getProperty("swapSourceZookeeper");
    }

    public static String getSwapSourceTopic(){
        return getProperty("swapSourceTopic");
    }

    public static String getSwapSourceGroupId(){
        return getProperty("swapSourceGroupId");
    }



    public static String getHbaseSwapSourceBroker(){
        return getProperty("hbaseSwapSourceBroker");
    }

    public static String getHbaseSwapSourceZookeeper(){
        return getProperty("hbaseSwapSourceZookeeper");
    }

    public static String getHbaseSwapSourceTopic(){
        return getProperty("hbaseSwapSourceTopic");
    }

    public static String getHbaseSwapSourceGroupId(){
        return getProperty("hbaseSwapSourceGroupId");
    }


    public static String getParseResultSourceBroker(){
        return getProperty("parseResultSourceBroker");
    }

    public static String getParseResultSourceZookeeper(){
        return getProperty("parseResultSourceZookeeper");
    }

    public static String getParseResultSourceTopic(){
        return getProperty("parseResultSourceTopic");
    }

    public static String getParseResultSourceGroupId(){
        return getProperty("parseResultSourceGroupId");
    }


    public static String getPisMonitorResultStartOffset(){
        return getProperty("pisMonitorResultStartOffset");
    }







}
