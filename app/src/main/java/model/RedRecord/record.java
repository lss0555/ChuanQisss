package model.RedRecord;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zss on 2016/8/22.
 */
public class record implements Serializable {
    private ArrayList<recordList> record;

    public ArrayList<recordList> getRecord() {
        return record;
    }

    public void setRecord(ArrayList<recordList> record) {
        this.record = record;
    }
}
