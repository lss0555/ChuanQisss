package model.Banner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lss on 2016/8/15.
 */
public class Banner implements Serializable{
    private List<banners> ggt;
    public List<banners> getGgt() {
        return ggt;
    }
    public void setGgt(List<banners> ggt) {
        this.ggt = ggt;
    }
}
