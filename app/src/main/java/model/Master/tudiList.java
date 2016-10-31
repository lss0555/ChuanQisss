package model.Master;

import java.io.Serializable;

/**
 * Created by lss on 2016/8/15.
 */
public class tudiList implements Serializable {
    private  String ApprenticeId;
    private  String Headportrait;
    private  String Pay;

    public String getApprenticeId() {
        return ApprenticeId;
    }

    public void setApprenticeId(String apprenticeId) {
        ApprenticeId = apprenticeId;
    }

    public String getPay() {
        return Pay;
    }

    public void setPay(String pay) {
        Pay = pay;
    }

    public String getHeadportrait() {
        return Headportrait;
    }

    public void setHeadportrait(String headportrait) {
        Headportrait = headportrait;
    }
}
