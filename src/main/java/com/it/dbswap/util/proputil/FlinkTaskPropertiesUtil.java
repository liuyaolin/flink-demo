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
public class FlinkTaskPropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(FlinkTaskPropertiesUtil.class);

    private static Properties props;

    static {
        String fileName = "flinktask.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(FlinkTaskPropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
            logger.info("加载 flinktask.properties 配置");

            if (props.size() == 0) {
                throw new RuntimeException("flinktask.properties的配置项为空");
            }


            Iterator<String> it = props.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                logger.info("{}={}", key, props.get(key));
            }


        } catch (IOException e) {
            logger.error("配置文件读取异常", e);
        }
    }

    /**
     * 自定义俩个get方法，方便调用工具类读取properties文件的属性
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            String message = "FlinkTaskPropertiesUtil配置项 " + key + " 不存在";
            logger.error(message);
            throw new RuntimeException(message);
        }
        return value.trim();
    }

    public static String getProperty(String key, String defaultValue) {
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            value = defaultValue;
        }
        return value.trim();
    }

    /**
     * @return
     */
    public static String getCheckpointDir() {
        return getProperty("checkpoint_dir");
    }

    public static String getEnv() {
        return getProperty("env");
    }

    public static int getProcessParrel() {
        String flinkParallelism = getProperty("processParrel");
        return Integer.valueOf(flinkParallelism);
    }

    public static int getSourceParrel() {
        String flinkParallelism = getProperty("sourceParrel");
        return Integer.valueOf(flinkParallelism);
    }

    public static int getSinkParrel() {
        String flinkParallelism = getProperty("sinkParrel");
        return Integer.valueOf(flinkParallelism);
    }


    public static int getHbaseProcessParrel() {
        String flinkParallelism = getProperty("hbaseProcessParrel");
        return Integer.valueOf(flinkParallelism);
    }

    public static int getHbaseSourceParrel() {
        String flinkParallelism = getProperty("hbaseSourceParrel");
        return Integer.valueOf(flinkParallelism);
    }

    public static int getHbaseSinkParrel() {
        String flinkParallelism = getProperty("hbaseSinkParrel");
        return Integer.valueOf(flinkParallelism);
    }
    public static void main(String[] args) {
    }


}
