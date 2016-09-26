package model;

import java.io.Serializable;

/**
 * Created by zss on 2016/9/23.
 */
public class WxWithDraw implements Serializable {
    private String run;
    private  String des;

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
