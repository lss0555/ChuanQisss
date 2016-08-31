package model.Banner;

import java.io.Serializable;

/**
 * Created by zss on 2016/8/15.
 */
public class banners implements Serializable {
    private  String imgurl;
    private  String link;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
