package model.RedTimeList;

import java.io.Serializable;

/**
 * Created by zss on 2016/8/22.
 */
public class RedTimeList implements Serializable {
    private  String    jine;
    private  String    dtime;
    private  String    userid;

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
