package model;

import java.io.Serializable;

/**
 * Created by zss on 2016/9/27.
 */
public class ShaiDan implements Serializable{
    private String     userappcount;
    private String     tudicount;
    private String     tudisy;
    private String     allsy;

    public String getUserappcount() {
        return userappcount;
    }

    public void setUserappcount(String userappcount) {
        this.userappcount = userappcount;
    }

    public String getTudicount() {
        return tudicount;
    }

    public void setTudicount(String tudicount) {
        this.tudicount = tudicount;
    }

    public String getTudisy() {
        return tudisy;
    }

    public void setTudisy(String tudisy) {
        this.tudisy = tudisy;
    }

    public String getAllsy() {
        return allsy;
    }

    public void setAllsy(String allsy) {
        this.allsy = allsy;
    }
}
