package model;

import java.io.Serializable;

/**
 * Created by zss on 2016/10/4.
 */
public class TaskState implements Serializable {
    private String run;
    private String applyid;

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getApplyid() {
        return applyid;
    }

    public void setApplyid(String applyid) {
        this.applyid = applyid;
    }
}
