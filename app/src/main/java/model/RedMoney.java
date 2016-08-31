package model;

import java.io.Serializable;

/**
 * Created by zss on 2016/8/26.
 */
public class RedMoney implements Serializable {
    private  double money;
    private String run;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }
}
