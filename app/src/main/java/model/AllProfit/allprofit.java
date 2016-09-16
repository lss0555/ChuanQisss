package model.AllProfit;

import java.io.Serializable;

/**
 * Created by zss on 2016/9/16.
 */
public class allprofit implements Serializable{
    private String ApplyIcon;
    private String ApplyName;
    private String price;
    private String dTime;

    public String getApplyIcon() {
        return ApplyIcon;
    }

    public void setApplyIcon(String applyIcon) {
        ApplyIcon = applyIcon;
    }

    public String getApplyName() {
        return ApplyName;
    }

    public void setApplyName(String applyName) {
        ApplyName = applyName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getdTime() {
        return dTime;
    }

    public void setdTime(String dTime) {
        this.dTime = dTime;
    }
}
