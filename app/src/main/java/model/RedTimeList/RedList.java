package model.RedTimeList;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zss on 2016/8/22.
 */
public class RedList implements Serializable {
    private ArrayList<RedTimeList> timerecord;

    public ArrayList<RedTimeList> getTimerecord() {
        return timerecord;
    }

    public void setTimerecord(ArrayList<RedTimeList> timerecord) {
        this.timerecord = timerecord;
    }
}
