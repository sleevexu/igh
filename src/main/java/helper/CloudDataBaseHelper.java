package helper;

import common.Configuration;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by holten.gao on 2016/7/14.
 */

public class CloudDataBaseHelper {
    private static final Logger logger = LoggerFactory.getLogger(CloudDataBaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static final QueryRunner QUERY_RUNNER;
    private static final BasicDataSource DATA_SOURCE;

    // 进行初始化工作
    static {
        CONNECTION_HOLDER = new ThreadLocal<>();
        QUERY_RUNNER = new QueryRunner();

        // 配置数据库连接池
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(Configuration.db_driver);
        DATA_SOURCE.setUrl(Configuration.clouddb_url);
        DATA_SOURCE.setUsername(Configuration.clouddb_username);
        DATA_SOURCE.setPassword(Configuration.clouddb_password);
        DATA_SOURCE.setInitialSize(5);
        DATA_SOURCE.setMinIdle(5);
        DATA_SOURCE.setValidationQuery("SELECT 1");
        DATA_SOURCE.setTestOnBorrow(true);
        DATA_SOURCE.setTestWhileIdle(true);
        DATA_SOURCE.setTestOnReturn(true);
    }

    /**
     * 获取数据库连接
     *
     * @return 数据库连接
     */
    private static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        try {
            if (conn == null || conn.isClosed()) {
                try {
                    conn = DATA_SOURCE.getConnection();
                    System.out.println("Connect to " + DATA_SOURCE.getUrl() + " successfully");
                } catch (SQLException e) {
                    logger.error("Get connection to cloud database failure", e);
                } finally {
                    CONNECTION_HOLDER.set(conn);
                }
            }
        } catch (SQLException e) {
            logger.error("Get connection to cloud database failure", e);
        }
        return conn;
    }

    /**
     * 执行增/删/改
     *
     * @param sql    SQL语句
     * @param params
     * @return 受影响行数
     */
    public static int executeUpdate(String sql, Object... params) {
        int rows = 0;
        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            logger.error("execute update/insert/delete failure", e);
        }
        return rows;
    }

    /**
     * 查询实体类
     *
     * @param entityClass 实体类
     * @param sql         SQL语句
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity = null;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            logger.error("Query entity failure!", e);
        }
        return entity;
    }
}