package com.it.dbswap.util;

import com.it.dbswap.util.proputil.MysqlPropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @description:
 * @author: huangchm-01328365
 * @date: 2020-04-16 10:50
 */
public class DbHelper {

    private static Logger logger = LoggerFactory.getLogger(DbHelper.class);
    private volatile static Connection connection = null;
    private static String USER = MysqlPropertiesUtil.getOewmDbUser();
    private static String PASSWORD = MysqlPropertiesUtil.getOewmDbPassword();
    private static String URL = MysqlPropertiesUtil.getOewmDbUrl();

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                synchronized (DbHelper.class) {
                    if (connection == null || connection.isClosed()) {
                        Class.forName("com.mysql.jdbc.Driver");
                        connection = DriverManager.getConnection(URL, USER, PASSWORD);
                        return connection;
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(),e);
        }
        return connection;
    }

}
