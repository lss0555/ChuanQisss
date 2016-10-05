package model.Guide;

import java.io.Serializable;

/**
 * Created by zss on 2016/10/5.
 */
public class guides implements Serializable {
    private String imgurl;
    public String getImgurl() {
        return imgurl;
    }
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
