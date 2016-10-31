package model.FaskTask;

import java.io.Serializable;

/**
 * Created by lss on 2016/8/12.
 */
public class faskTask implements Serializable{
     private String applyid;
     private String startTime;
     private String Amount;
     private String NowAmount;
     private String appname;
     private String keyword;
     private String price;
     private String Prompt;
     private String sBandleID;
     private String Explain;
     private String type;
     private String step;
     private String appUrl;
     private String applyIcon;
     private String urlschemes;

     public faskTask(String applyid, String startTime, String amount, String nowAmount, String appname, String keyword, String price, String prompt, String sBandleID, String explain, String type, String step, String appUrl, String applyIcon, String urlschemes) {
          this.applyid = applyid;
          this.startTime = startTime;
          Amount = amount;
          NowAmount = nowAmount;
          this.appname = appname;
          this.keyword = keyword;
          this.price = price;
          Prompt = prompt;
          this.sBandleID = sBandleID;
          Explain = explain;
          this.type = type;
          this.step = step;
          this.appUrl = appUrl;
          this.applyIcon = applyIcon;
          this.urlschemes = urlschemes;
     }

     public String getApplyid() {
          return applyid;
     }

     public void setApplyid(String applyid) {
          this.applyid = applyid;
     }

     public String getStartTime() {
          return startTime;
     }

     public void setStartTime(String startTime) {
          this.startTime = startTime;
     }

     public String getAmount() {
          return Amount;
     }

     public void setAmount(String amount) {
          Amount = amount;
     }

     public String getNowAmount() {
          return NowAmount;
     }

     public void setNowAmount(String nowAmount) {
          NowAmount = nowAmount;
     }

     public String getAppname() {
          return appname;
     }

     public void setAppname(String appname) {
          this.appname = appname;
     }

     public String getKeyword() {
          return keyword;
     }

     public void setKeyword(String keyword) {
          this.keyword = keyword;
     }

     public String getPrice() {
          return price;
     }

     public void setPrice(String price) {
          this.price = price;
     }

     public String getPrompt() {
          return Prompt;
     }

     public void setPrompt(String prompt) {
          Prompt = prompt;
     }

     public String getsBandleID() {
          return sBandleID;
     }

     public void setsBandleID(String sBandleID) {
          this.sBandleID = sBandleID;
     }

     public String getExplain() {
          return Explain;
     }

     public void setExplain(String explain) {
          Explain = explain;
     }

     public String getType() {
          return type;
     }

     public void setType(String type) {
          this.type = type;
     }

     public String getStep() {
          return step;
     }

     public void setStep(String step) {
          this.step = step;
     }

     public String getAppUrl() {
          return appUrl;
     }

     public void setAppUrl(String appUrl) {
          this.appUrl = appUrl;
     }

     public String getApplyIcon() {
          return applyIcon;
     }

     public void setApplyIcon(String applyIcon) {
          this.applyIcon = applyIcon;
     }

     public String getUrlschemes() {
          return urlschemes;
     }

     public void setUrlschemes(String urlschemes) {
          this.urlschemes = urlschemes;
     }

     @Override
     public String toString() {
          return "faskTask{" +
                  "applyid='" + applyid + '\'' +
                  ", startTime='" + startTime + '\'' +
                  ", Amount='" + Amount + '\'' +
                  ", NowAmount='" + NowAmount + '\'' +
                  ", appname='" + appname + '\'' +
                  ", keyword='" + keyword + '\'' +
                  ", price='" + price + '\'' +
                  ", Prompt='" + Prompt + '\'' +
                  ", sBandleID='" + sBandleID + '\'' +
                  ", Explain='" + Explain + '\'' +
                  ", type='" + type + '\'' +
                  ", step='" + step + '\'' +
                  ", appUrl='" + appUrl + '\'' +
                  ", applyIcon='" + applyIcon + '\'' +
                  ", urlschemes='" + urlschemes + '\'' +
                  '}';
     }
}
