package com.it.dbswap.hbase;

import java.io.IOException;

import com.it.dbswap.util.proputil.HBasePropertiesUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HBaseConnection {
	protected static final Logger LOGGER = LoggerFactory.getLogger(HBaseConnection.class);
	private static Connection conn;
	private static String zkHost = null;
	private static String zkPort = null;
	private static String znode = null;
	static {
		zkHost = HBasePropertiesUtil.getProperty("hbase.zkhost");
		zkPort = HBasePropertiesUtil.getProperty("hbase.zkport");
		znode = HBasePropertiesUtil.getProperty("zookeeper.znode.parent");
	}

	public static Connection getConn() {
		try {
			if (null == conn) {
				connect();
			}
		} catch (Exception e) {
			LOGGER.error("Hbase connect exception",e);
		}
		return conn;
	}

	private static void connect() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		conf.set("simple.user.name", "inc-bdp-pis");
	//	conf.set("hbase.client.userprovider.class", "org.apache.hadoop.hbase.security.SimpleUserProvider");
		conf.set("hbase.zookeeper.quorum", zkHost);
		conf.set("hbase.zookeeper.property.clientPort", zkPort);
		conf.set("zookeeper.znode.parent", znode);
		conf.set("hbase.client.retries.number", "3");
		conf.set("zookeeper.session.timeout", "120000");
//		conf.set("hbase.client.ipc.pool.type","RoundRobin");
//		conf.set("hbase.client.ipc.pool.size", "200");
		conn = ConnectionFactory.createConnection(conf);
	}

	public static Connection reConn() throws IOException {
		connect();
		return conn;
	}
}