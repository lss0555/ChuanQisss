package model.TxRecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lss on 2016/8/10.
 */
public class tx implements Serializable{
    private List<TxRecord> tx;

    public List<TxRecord> getTx() {
        return tx;
    }

    public void setTx(List<TxRecord> tx) {
        this.tx = tx;
    }
}
