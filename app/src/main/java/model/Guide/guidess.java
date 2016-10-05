package model.Guide;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zss on 2016/10/5.
 */
public class guidess implements Serializable{
    private ArrayList<guides> ggt;

    public ArrayList<guides> getGgt() {
        return ggt;
    }

    public void setGgt(ArrayList<guides> ggt) {
        this.ggt = ggt;
    }
}
