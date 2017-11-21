package model;

/**
 * Created by holten on 2016/11/9.
 */
public class SyncResult {
    private int totalNum;
    private int successNum;
    private int failureNum;
    private int latestId;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(int successNum) {
        this.successNum = successNum;
    }

    public int getFailureNum() {
        return failureNum;
    }

    public void setFailureNum(int failureNum) {

        this.failureNum = failureNum;
    }

    public int getLatestId() {
        return latestId;
    }

    public void setLatestId(int latestId) {
        this.latestId = latestId;
    }
}
