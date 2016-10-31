package model.AllRichList;

import java.io.Serializable;
import java.util.ArrayList;

import model.RichList;

/**
 * Created by Administrator on 2016/10/15.
 */

public class all implements Serializable{
    private ArrayList<RichList> all;

    public ArrayList<RichList> getAll() {
        return all;
    }

    public void setAll(ArrayList<RichList> all) {
        this.all = all;
    }
}
