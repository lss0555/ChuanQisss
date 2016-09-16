package model.AllProfit;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zss on 2016/9/16.
 */
public class profit implements Serializable{
    private ArrayList<allprofit>   incomerecord;
    public ArrayList<allprofit> getIncomerecord() {
        return incomerecord;
    }
    public void setIncomerecord(ArrayList<allprofit> incomerecord) {
        this.incomerecord = incomerecord;
    }
}
