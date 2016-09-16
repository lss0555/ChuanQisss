package model;

import java.io.Serializable;

/**
 * Created by zss on 2016/9/14.
 */
public class Version implements Serializable{
    private String    bbh;
    private String    gxxx;
    private String    url;

    public String getBbh() {
        return bbh;
    }

    public void setBbh(String bbh) {
        this.bbh = bbh;
    }

    public String getGxxx() {
        return gxxx;
    }

    public void setGxxx(String gxxx) {
        this.gxxx = gxxx;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
