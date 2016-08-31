package model;

import java.io.Serializable;

/**
 * Created by lss on 2016/8/6.
 */
public class JxzAccount implements Serializable{
    private  String   yue;
    private  String   accrual;
    public String getYue() {
        return yue;
    }
    public void setYue(String yue) {
        this.yue = yue;
    }
    public String getAccrual() {
        return accrual;
    }
    public void setAccrual(String accrual) {
        this.accrual = accrual;
    }
}
