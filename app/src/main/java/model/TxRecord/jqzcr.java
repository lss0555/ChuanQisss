package model.TxRecord;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zss on 2016/8/10.
 */
public class jqzcr implements Serializable{
    private ArrayList<JqzCrRecord>  jqzcr;

    public ArrayList<JqzCrRecord> getJqzcr() {
        return jqzcr;
    }

    public void setJqzcr(ArrayList<JqzCrRecord> jqzcr) {
        this.jqzcr = jqzcr;
    }
}
