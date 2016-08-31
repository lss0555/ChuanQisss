package model.TxRecord;

import java.io.Serializable;

/**
 * Created by zss on 2016/8/10.
 */
public class TxRecord implements Serializable {
    private  String id;
    private  String userid;
    private  String accountstyle;
    private  String txstate;
    private  String price;
    private  String txtime;

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

    public String getAccountstyle() {
        return accountstyle;
    }

    public void setAccountstyle(String accountstyle) {
        this.accountstyle = accountstyle;
    }

    public String getTxstate() {
        return txstate;
    }

    public void setTxstate(String txstate) {
        this.txstate = txstate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTxtime() {
        return txtime;
    }

    public void setTxtime(String txtime) {
        this.txtime = txtime;
    }
}
