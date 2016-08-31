package model;

import java.io.Serializable;

/**
 * Created by lss on 2016/8/4.
 */
public class UserMoney implements Serializable{
    private   String fNotPayIncome;
    private   String fTodayIncome;

    public String getfNotPayIncome() {
        return fNotPayIncome;
    }

    public void setfNotPayIncome(String fNotPayIncome) {
        this.fNotPayIncome = fNotPayIncome;
    }

    public String getfTodayIncome() {
        return fTodayIncome;
    }

    public void setfTodayIncome(String fTodayIncome) {
        this.fTodayIncome = fTodayIncome;
    }
}
