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
public class EsPropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(EsPropertiesUtil.class);

    private static Properties props;

    public static Properties inint(){
        if(null == props || props.isEmpty()) {
            synchronized (EsPropertiesUtil.class) {
                if (null == props || props.isEmpty()) {
                    String fileName = "es.properties";
                    props = new Properties();
                    try {
                        props.load(new InputStreamReader(EsPropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
                        logger.info("加载 es.properties 配置");

                        if (props.size() == 0) {
                            throw new RuntimeException("es.properties的配置项为空");
                        }

                    } catch (IOException e) {
                        logger.error("配置文件读取异常", e);
                    }
                }
            }
        }
        return props;
    }

  /*  static {
        String fileName = "es.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(EsPropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
            logger.info("加载 es.properties 配置");

            if (props.size() == 0) {
                throw new RuntimeException("es.properties的配置项为空");
            }

        } catch (IOException e) {
            logger.error("配置文件读取异常", e);
        }
    }*/

    //自定义俩个get方法，方便调用工具类读取properties文件的属性
    public static String getProperty(String key) {
        inint();
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            String message = "EsPropertiesUtil配置项 " + key + "不存在";
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

    public static String getIndex() {
        return getProperty("dept.stat.index");
    }

    public static String getType() {
        return getProperty("dept.stat.type");
    }

    public static int getPort() {
        return Integer.parseInt(getProperty("dept.stat.port"));
    }

    public static String getDepartmentTag() {  return getProperty("department.tag"); }

    public static String getDepartmentIndexName() {
        return getProperty("department.index.name");
    }

    public static String getDepartmentIndexType() {
        return getProperty("department.index.type");
    }

    public static String getEmployeeIndexName() {
        return getProperty("employee.index.name");
    }

    public static String getEmployeeIndexType() {
        return getProperty("employee.index.type");
    }

    public static String getEmployeeTag() {  return getProperty("employee.tag"); }

    public static String getBatchTag() { return getProperty("batch.tag"); }

    public static String getBatchIndexName() {  return getProperty("batch.index.name"); }

    public static String getBatchTypeName() {  return getProperty("batch.index.type"); }

    public static String getTeamTag() { return getProperty("teamCode.tag"); }

    public static String getTeamIndexName() {  return getProperty("teamCode.index.name"); }

    public static String getTeamTypeName() {  return getProperty("teamCode.index.type"); }

    public static String getCustomerTag() { return getProperty("customer.tag"); }

    public static String getCustomerIndexName() {  return getProperty("customer.index.name"); }

    public static String getCustomerTypeName() {  return getProperty("customer.index.type"); }

    public static String getProductTag() { return getProperty("product.tag"); }

    public static String getProductIndexName() {  return getProperty("product.index.name"); }

    public static String getProductTypeName() {  return getProperty("product.index.type"); }

    public static String getFeeTag() { return getProperty("fee.tag"); }

    public static String getFeeIndexName() {  return getProperty("fee.index.name"); }

    public static String getFeeTypeName() {  return getProperty("fee.index.type"); }

    public static String getStayCodeTag() { return getProperty("stayCode.tag"); }

    public static String getStayCodeIndexName() {  return getProperty("stayCode.index.name"); }

    public static String getStayCodeTypeName() {  return getProperty("stayCode.index.type"); }

    public static String getOrderBatchTag() {
        return getProperty("order.batch.tag");
    }

    public static String getOrderBatchIndexName() {
        return getProperty("order.batch.index.name");
    }

    public static String getOrderBatchTypeName() {
        return getProperty("order.batch.index.type");
    }

    public static String getSeasonFoodTag() { return getProperty("seasonFood.tag"); }

    public static String getSeasonFoodIndexName() {  return getProperty("seasonFood.index.name"); }

    public static String getSeasonFoodTypeName() {  return getProperty("seasonFood.index.type"); }

    public static String getAoiTag() { return getProperty("aoi.tag"); }

    public static String getAoiIndexName() {  return getProperty("aoi.index.name"); }

    public static String getAoiTypeName() {  return getProperty("aoi.index.type"); }

    public static String getNightBatchTag() { return getProperty("nightBatch.tag"); }

    public static String getNightBatchIndexName() {  return getProperty("nightBatch.index.name"); }

    public static String getNightBatchTypeName() {  return getProperty("nightBatch.index.type"); }

    public static void main(String[] args) {

        System.out.println(getProperty("teamCode.esHosts"));

    }


}
