package model;

import java.io.Serializable;

/**
 * Created by zss on 2016/8/15.
 */
public class IsBindAccount implements Serializable {
    private  String account;
    private  String name;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
