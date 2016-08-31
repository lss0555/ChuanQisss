package model;

import java.io.Serializable;

/**
 * Created by zss on 2016/8/17.
 */
public class RedTime implements Serializable {
    private  String Time;
    private  String State;

    public RedTime(String time, String state) {
        Time = time;
        State = state;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
