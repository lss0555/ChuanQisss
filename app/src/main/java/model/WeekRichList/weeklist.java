package model.WeekRichList;

import java.io.Serializable;
import java.util.ArrayList;

import model.RichList;

/**
 * Created by Administrator on 2016/10/15.
 */

public class weeklist implements Serializable {
    private ArrayList<RichList> thisweektdcount;

    public ArrayList<RichList> getThisweektdcount() {
        return thisweektdcount;
    }

    public void setThisweektdcount(ArrayList<RichList> thisweektdcount) {
        this.thisweektdcount = thisweektdcount;
    }
}
