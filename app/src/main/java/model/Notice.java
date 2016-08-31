package model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zss on 2016/8/9.
 */
public class Notice implements Serializable{
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
