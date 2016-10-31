package model.DayRichList;

import java.io.Serializable;
import java.util.ArrayList;

import model.RichList;

/**
 * Created by Administrator on 2016/10/16.
 */

public class Dayrichlist implements Serializable {
    private ArrayList<RichList>     daterich;

    public ArrayList<RichList> getDaterich() {
        return daterich;
    }

    public void setDaterich(ArrayList<RichList> daterich) {
        this.daterich = daterich;
    }
}
