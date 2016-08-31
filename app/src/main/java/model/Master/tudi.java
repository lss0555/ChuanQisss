package model.Master;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zss on 2016/8/15.
 */
public class tudi implements Serializable {
    private List<tudiList> st;

    public List<tudiList> getSt() {
        return st;
    }

    public void setSt(List<tudiList> st) {
        this.st = st;
    }
}
