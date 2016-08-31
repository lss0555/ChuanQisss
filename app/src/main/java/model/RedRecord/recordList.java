package model.RedRecord;

import java.io.Serializable;

/**
 * Created by zss on 2016/8/22.
 */
public class recordList implements Serializable {
    private  String jine;
    private  String dtime;

    public String getJine() {
        return jine;
    }

    public void setJine(String jine) {
        this.jine = jine;
    }

    public String getDtime() {
        return dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }
}
