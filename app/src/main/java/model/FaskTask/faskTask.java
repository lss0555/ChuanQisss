package model.FaskTask;

import java.io.Serializable;

/**
 * Created by lss on 2016/8/12.
 */
public class faskTask implements Serializable{
     private  String startTime;
     private  String Amount;
     private  String NowAmount;
     private  String appname;
     private  String keyword;
     private  String price;
     private  String Prompt;
     private  String Explain;
     private  String type;
     private  String step;
     private  String appUrl;
     private  String applyIcon;

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
}
