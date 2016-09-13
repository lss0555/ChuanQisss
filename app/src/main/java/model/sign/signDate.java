package model.sign;

import java.io.Serializable;

/**
 * Created by zss on 2016/8/31.
 */
public class signDate implements Serializable{
    private  String dtime;

    public String getDtime() {
        return dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }
}
