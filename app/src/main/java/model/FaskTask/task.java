package model.FaskTask;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zss on 2016/8/12.
 */
public class task implements Serializable {
    private List<faskTask> applyarr;

    public List<faskTask> getApplyarr() {
        return applyarr;
    }

    public void setApplyarr(List<faskTask> applyarr) {
        this.applyarr = applyarr;
    }
}
