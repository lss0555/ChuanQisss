package model.TxRecord;

import java.io.Serializable;

/**
 * Created by lss on 2016/8/10.
 */
public class JqzCrRecord implements Serializable {
    private  String id;
    private  String userid;
    private  String money;
    private  String tdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTdate() {
        return tdate;
    }

    public void setTdate(String tdate) {
        this.tdate = tdate;
    }
}
