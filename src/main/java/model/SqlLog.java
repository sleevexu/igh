package model;

import java.sql.Timestamp;

/**
 * Created by holten on 2016/11/9.
 */
public class SqlLog {
    private int id;
    private String sqlstatement;
    private String parameters;
    private Timestamp sampletime;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSqlstatement() {
        return sqlstatement;
    }

    public void setSqlstatement(String sqlstatement) {
        this.sqlstatement = sqlstatement;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Timestamp getSampletime() {
        return sampletime;
    }

    public void setSampletime(Timestamp sampletime) {
        this.sampletime = sampletime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
