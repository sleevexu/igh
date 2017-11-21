package service;

import common.Configuration;
import helper.CloudDataBaseHelper;
import helper.DataBaseHelper;
import model.SqlLog;
import model.SyncResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by holten on 2016/11/9.
 */
public class SyncService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncService.class);

    /**
     * 生成数据库操作日志对象
     *
     * @param sql
     * @param params
     * @return
     */
    public static SqlLog generateSqlLog(String sql, Object... params) {
        SqlLog sqlLog = new SqlLog();
        String paramString = "";
        for (Object param : params) {
            paramString = paramString + param + ",";
        }
        sqlLog.setSqlstatement(sql);
        sqlLog.setParameters(paramString);
        sqlLog.setSampletime(new Timestamp(System.currentTimeMillis()));
        sqlLog.setStatus(0);
        return sqlLog;
    }

    /**
     * 保存数据库操作日志
     *
     * @param data
     * @return
     */
    public static boolean saveSqlLog(SqlLog data) {
        Map<String, Object> logMap = new HashMap();
        logMap.put("sqlstatement", data.getSqlstatement());
        logMap.put("parameters", data.getParameters());
        logMap.put("sampletime", data.getSampletime());
        logMap.put("status", data.getStatus());
        return DataBaseHelper.insertEntityForLog("sqllog", logMap);
    }

    public static void truncateSqlLog() {
        DataBaseHelper.truncateTable("sqllog");
    }

    /**
     * 获取没有同步的数据库操作日志
     *
     * @return
     */
    public static List<SqlLog> getNotSyncLog() {
        String sql = "SELECT * FROM sqllog WHERE status=0";
        return DataBaseHelper.queryEntityList(SqlLog.class, sql);
    }

    /**
     * 数据库操作同步完成后更新日志状态
     *
     * @param id
     * @return
     */
    public static boolean setLogStatus(int id) {
        Map<String, Object> logMap = new HashMap();
        logMap.put("status", 1);
        return DataBaseHelper.updateEntityForLog("sqllog", id, logMap);
    }

    /**
     * 同步数据库操作至云端
     *
     * @param log
     * @return
     */
    private static boolean replayLogToCloud(SqlLog log) {
        String sql = log.getSqlstatement();
        Object[] params = log.getParameters().split(",");
        return CloudDataBaseHelper.executeUpdate(sql, params) == 1;
    }

    private static void clearSqlLog(SyncResult syncResult) {
        //同步全部成功，且日志总数大于日志有效数目，则清空日志表
        if (0 == syncResult.getFailureNum() & syncResult.getLatestId() > Configuration.LogValidity) {
            truncateSqlLog();
            LOGGER.info("Clear Log!");
        }
    }

    public static SyncResult syncToCloud() {
        List<SqlLog> notSyncLog = getNotSyncLog();
        SyncResult syncResult = new SyncResult();
        syncResult.setTotalNum(notSyncLog.size());
        int successNum = 0;
        //如果未同步SQL列表不为空，则进行同步
        if (0 != syncResult.getTotalNum()) {
            for (SqlLog log : notSyncLog) {
                if (replayLogToCloud(log)) {
                    setLogStatus(log.getId());
                    successNum++;
                } else {
                    if (replayLogToCloud(log)) {
                        setLogStatus(log.getId());
                        successNum++;
                    } else {
                        LOGGER.error("Upload record to Cloud Failure.");
                        break;//上传失败后停止上传，等待下一次上传
                    }
                }
            }
            syncResult.setSuccessNum(successNum);
            syncResult.setFailureNum(syncResult.getTotalNum() - successNum);
            syncResult.setLatestId(notSyncLog.get(notSyncLog.size() - 1).getId());
            clearSqlLog(syncResult);
        }
        return syncResult;
    }
}
