package model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/15.
 */
public class RichList implements Serializable {
    private  String jine;
    private  String userid;
    private  String Headportrait;
    private  String tdcount;
    public String getJine() {
        return jine;
    }

    public void setJine(String jine) {
        this.jine = jine;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getHeadportrait() {
        return Headportrait;
    }

    public void setHeadportrait(String headportrait) {
        Headportrait = headportrait;
    }

    public String getTdcount() {
        return tdcount;
    }

    public void setTdcount(String tdcount) {
        this.tdcount = tdcount;
    }
}
