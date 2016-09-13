package model.sign;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zss on 2016/8/31.
 */
public class Signs implements Serializable{
    private ArrayList<signDate> qdjl;
    public ArrayList<signDate> getQdjl() {
        return qdjl;
    }
    public void setQdjl(ArrayList<signDate> qdjl) {
        this.qdjl = qdjl;
    }
}
