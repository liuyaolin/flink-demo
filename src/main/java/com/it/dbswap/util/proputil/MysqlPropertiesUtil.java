package com.it.dbswap.util.proputil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @description:
 * @author: huangchm-01328365
 * @date: 2020-04-16 10:50
 */
public class MysqlPropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(MysqlPropertiesUtil.class);

    private static Properties props;

    static {
        String fileName = "jdbc.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(MysqlPropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
            logger.info("加载 jdbc.properties 配置");

            if(props.size()==0){
                throw new RuntimeException("jdbc.properties的配置项为空");
            }
        } catch (IOException e) {
            logger.error("配置文件读取异常",e);
        }
    }

    //自定义俩个get方法，方便调用工具类读取properties文件的属性
    public static String getProperty(String key){
        String value= props.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
            String message= "MysqlPropertiesUtil配置项 "+ key + "不存在";
            logger.error(message);
             throw new RuntimeException("message");
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


    public static String getOewmDbUrl(){
        return getProperty("oewm.database.url");
    }

    public static String getOewmDbUser(){
        return getProperty("oewm.database.user");
    }

    public static String getOewmDbPassword(){
        return  getProperty("oewm.database.password");
    }

    public static String getDimLevelTable(){
        return getProperty("oewm.dim.table");
    }


}
