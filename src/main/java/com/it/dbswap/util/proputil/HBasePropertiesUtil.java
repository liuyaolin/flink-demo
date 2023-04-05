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
 * @date: 2020-04-16 11:08
 */
public class HBasePropertiesUtil {

	private static Logger logger = LoggerFactory.getLogger(HBasePropertiesUtil.class);

	private static Properties props;

	static {
		String env = FlinkTaskPropertiesUtil.getEnv();
		String fileName = "hbase.properties";
		if("sit".equals(env)) {
			fileName = "hbase-sit.properties";
		}

		props = new Properties();
		try {
			props.load(new InputStreamReader(HBasePropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
			logger.info("加载 hbase.properties 配置");

			if (props.size() == 0) {
				throw new RuntimeException("hbase.properties的配置项为空");
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
			String message = "HBasePropertiesUtil配置项 " + key + " 不存在";
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
	 * 自定义俩个get方法，方便调用工具类读取properties文件的属性
	 * @param key
	 * @return
	 */
	public static int getIntProperty(String key) {
		int res = 0;

		String value = props.getProperty(key.trim());
		if (StringUtils.isBlank(value)) {
			String message = "HBasePropertiesUtil配置项 " + key + " 不存在";
			logger.error(message);
			throw new RuntimeException(message);
		} else {
			res = Integer.parseInt(value.trim());
		}
		return res;
	}

	public static int getIntProperty(String key, int defaultValue) {
		int res = defaultValue;

		String value = props.getProperty(key.trim());
		if (StringUtils.isNotBlank(value)) {
			res = Integer.parseInt(value.trim());
		}
		return res;
	}

	public static String getZkQuorum() {
		return getProperty("zkQuorum");
	}

	public static String getZkPort() {
		return getProperty("zkPort");
	}

	public static String getZkParent() {
		return getProperty("zkParent");
	}

	public static String getHbaseUserName() {
		return getProperty("hbaseUserName");
	}

	public static String getHbaseToken() {
		return getProperty("hbaseToken");
	}

	public static String getHbaseDbPath() {
		return getProperty("hbaseDbPath");
	}

	public static String getHbaseHmsUrl() {
		return getProperty("hbaseHmsUrl");
	}

	public static String getWaybillBaseDataTableName() {
		return getProperty("waybillBaseDataTableName");
	}

	public static String getWaybillBaseDataColumnFamily() {
		return getProperty("waybillBaseDataColumnFamily");
	}

	public static String getWaybillBaseDataColumn() {
		return getProperty("waybillBaseDataColumn");
	}

	public static String getWaybillBaseDataVersionTmColumn() {
		return getProperty("waybillBaseDataVersionTmColumn");
	}

	public static int getWaybillBaseDataPartitionNum() {
		String waybillBaseDataPartitionNum = getProperty("waybillBaseDataPartitionNum");
		return Integer.parseInt(waybillBaseDataPartitionNum);
	}

	public static int getWaybillBaseDataBatchSize() {
		String waybillBaseDataBatchSize = getProperty("waybillBaseDataBatchSize");
		return Integer.parseInt(waybillBaseDataBatchSize);
	}

	public static int getSaltTopSeed() {
		String saltTopSeed = getProperty("saltTopSeed");
		return Integer.parseInt(saltTopSeed);
	}

	public static String getOrderBaseDataTableName() {
		return getProperty("orderBaseDataTableName");
	}

	public static String getOrderBaseDataColumnFamily() {
		return "f";
	}
	public static String getOrderBaseDataQualifier() {
		return "c";
	}
	public static void main(String[] args) {
		System.out.println(HBasePropertiesUtil.getZkQuorum());
		System.out.println(HBasePropertiesUtil.getZkPort());
		System.out.println(HBasePropertiesUtil.getZkParent());
		System.out.println(HBasePropertiesUtil.getWaybillBaseDataTableName());
		System.out.println(HBasePropertiesUtil.getWaybillBaseDataColumnFamily());
		System.out.println(HBasePropertiesUtil.getWaybillBaseDataColumn());
		System.out.println(HBasePropertiesUtil.getWaybillBaseDataPartitionNum());
		System.out.println(HBasePropertiesUtil.getWaybillBaseDataBatchSize());
	}

}
