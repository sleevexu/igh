package thread;

import model.SyncResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.SyncService;

import java.sql.Timestamp;

/**
 * Created by holten on 2016/11/10.
 */
public class SyncThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncThread.class);

    @Override
    public void run() {
        SyncResult syncResult = new SyncResult();
        try {
            syncResult = SyncService.syncToCloud();
        } catch (Throwable e) {
            LOGGER.error("-----Sync failure!-----{}", e);
        } finally {
            if (0 == syncResult.getFailureNum()) {
                System.out.println("Upload " + syncResult.getTotalNum() + " records successfully at " + new Timestamp(System.currentTimeMillis()));
            } else {
                LOGGER.error("-----WARNING----- Upload " + syncResult.getFailureNum() + " records failure ");
            }
        }
    }
}
