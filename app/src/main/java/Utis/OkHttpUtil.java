package Utis;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import okhttp.OkHttpUtils;
import okhttp.callback.StringCallback;
import okhttp3.Call;
/**
 * Created by lss on 2016/7/5.
 */
public class OkHttpUtil {
    private final  int PHOTO_ONE=1;
    private final  int PHOTO_TWO=2;
    private final  int PHOTO_THREE=3;
    private static OkHttpUtil instance;


    public  OkHttpUtil(){}
    public static OkHttpUtil getInstance(){
        if(instance==null){instance=new OkHttpUtil();}
        return instance;
    }
    public FinishListener dataFinishListener;
    public void setFinishListener(FinishListener dataFinishListener) {
        this.dataFinishListener = dataFinishListener;
    }
    public static interface FinishListener {
        void Successfully(boolean IsSuccess, String data, String Msg);
    }
    /**
     * post请求
     * @param paragram
     * @param Url
     */
    public  void  Post(Map<String, String> paragram,String Url, final FinishListener dataFinishListener){
        OkHttpUtils
                .post()//
                .url(Url)//
                .params(paragram)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        if (e instanceof SocketTimeoutException) {
                            if(dataFinishListener!=null){
                                dataFinishListener.Successfully(false,"连接超时，加载失败",null);
                            }
                        } else if (e instanceof IOException) {
                            dataFinishListener.Successfully(false,"服务器异常，请请稍后...",null);
                        } else {
                            dataFinishListener.Successfully(false,"加载失败...",null);
                        }
                    }
                    @Override
                    public void onResponse(String response) {
                            if(dataFinishListener!=null){
                                dataFinishListener.Successfully(true,response,null);
                            }
                    }
                });
    }/**
     * get请求
     * @param Url
     */
    public  void  Get(String Url, final FinishListener dataFinishListener){
        OkHttpUtils
                .post()//
                .url(Url)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i("Post—————onError", e.toString());
                    }
                    @Override
                    public void onResponse(String response) {
                            if(dataFinishListener!=null){
                                dataFinishListener.Successfully(true,response,null);
                            }
                    }
                });
    }

    /**
     * 上传图片文件
     * @param Url
     * @param map
     * @param mFileName
     * @param mFilePath
     */
    public  void   MultiFile(String Url,HashMap<String,String> map,ArrayList<String> mFileName,ArrayList<String> mFilePath){
        if(mFilePath.size()>0){
            switch (mFilePath.size()){
                case PHOTO_ONE:
                    OkHttpUtils
                            .post()//
                            .addFile(mFileName.get(0), mFileName.get(0), new File(mFilePath.get(0)))
                            .url(Url)//
                            .params(map)
                            .build()//
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e) {
                                    Log.i("OnError====Okhttputis",""+e.toString());
                                }
                                @Override
                                public void onResponse(String response) {
//                                    Result result = GsonUtils.parseJSON(response, Result.class);
//                                    String Msg=result.getMsg();
//                                    if (result.getSuccess()) {
//                                        if (dataFinishListener != null) {
//                                            dataFinishListener.Successfully(true, response,Msg);
//                                        }
//                                    } else {
//                                        if (dataFinishListener != null) {
//                                            dataFinishListener.Successfully(false, response,Msg);
//                                        }
//                                    }
                                }
                            });
                    break;
                case PHOTO_TWO:
                    OkHttpUtils
                            .post()//
                            .addFile(mFileName.get(0), mFileName.get(0), new File(mFilePath.get(0)))
                            .addFile(mFileName.get(1), mFileName.get(1), new File(mFilePath.get(1)))
                            .url(Url)//
                            .params(map)
                            .build()//
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e) {
                                    Log.i("OnError====Okhttputis",""+e.toString());
                                }
                                @Override
                                public void onResponse(String response) {
//                                    Result result = GsonUtils.parseJSON(response, Result.class);
//                                    String Msg=result.getMsg();
//                                    if(result.getSuccess()){
//                                        if(dataFinishListener!=null){
//                                            dataFinishListener.Successfully(true,response,Msg);
//                                        }
//                                    }else {
//                                        if(dataFinishListener!=null){
//                                            dataFinishListener.Successfully(false,response,Msg);
//                                        }
//                                    }
                                }
                            });
                    break;
                case PHOTO_THREE:
                    OkHttpUtils
                            .post()//
                            .addFile(mFileName.get(0), mFileName.get(0), new File(mFilePath.get(0)))
                            .addFile(mFileName.get(1), mFileName.get(1), new File(mFilePath.get(1)))
                            .addFile(mFileName.get(2), mFileName.get(2), new File(mFilePath.get(2)))
                            .url(Url)//
                            .params(map)
                            .build()//
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e) {
                                    Log.i("OnError====Okhttputis",""+e.toString());
                                }
                                @Override
                                public void onResponse(String response) {
//                                    Result result = GsonUtils.parseJSON(response, Result.class);
//                                    String Msg=result.getMsg();
////                        Toast.makeText(context, response.toString(),Toast.LENGTH_SHORT).show();
//                                    if(result.getSuccess()){
//                                        if(dataFinishListener!=null){
//                                            dataFinishListener.Successfully(true,response,Msg);
//                                        }
//                                    }else {
//                                        if(dataFinishListener!=null){
//                                            dataFinishListener.Successfully(false,response,Msg);
//                                        }
//                                    }
                                }
                            });
                    break;
            }
        }else {
            OkHttpUtils
                    .post()//
                    .url(Url)//
                    .params(map)
                    .build()//
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            Log.i("OnError====Okhttputis",""+e.toString());
                        }
                        @Override
                        public void onResponse(String response) {
//                            Result result = GsonUtils.parseJSON(response, Result.class);
//                            String Msg=result.getMsg();
//                            if (result.getSuccess()) {
//                                if (dataFinishListener != null) {
//                                    dataFinishListener.Successfully(true, response,Msg);
//                                }
//                            } else {
//                                if (dataFinishListener != null) {
//                                    dataFinishListener.Successfully(false, response,Msg);
//                                }
//                            }
                        }
                    });
        }
    }

}
