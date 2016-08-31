package model;

import java.io.Serializable;

/**
 * Created by zss on 2016/8/13.
 */
public class UserInfo implements Serializable{
    private  String id;
    private  String uname;
    private  String Headportrait;
    private  String tel;
    private  String birthday;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getHeadportrait() {
        return Headportrait;
    }

    public void setHeadportrait(String headportrait) {
        Headportrait = headportrait;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
