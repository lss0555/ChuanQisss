package model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/15.
 */

public class Allprifit implements Serializable{
    private String yue;
    private String rwsr
            ;
    private String sttc;
    private String tixian;

    public String getYue() {
        return yue;
    }

    public void setYue(String yue) {
        this.yue = yue;
    }


    public String getSttc() {
        return sttc;
    }

    public void setSttc(String sttc) {
        this.sttc = sttc;
    }

    public String getTixian() {
        return tixian;
    }

    public void setTixian(String tixian) {
        this.tixian = tixian;
    }

    public String getRwsr() {
        return rwsr;
    }

    public void setRwsr(String rwsr) {
        this.rwsr = rwsr;
    }
}
