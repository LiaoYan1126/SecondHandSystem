package com.example.secondhandsystem.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/8/7.
 */

//okHttp 封装
public class OkHttpHelper {

    private Gson gson;

    private Handler mHandler;

    private static OkHttpClient okHttpClient;


    //私有构造函数，构造函数里面进行一些初始化
    private OkHttpHelper(){

         okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(10,TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(10,TimeUnit.SECONDS);

        gson=new Gson();

        mHandler=new Handler(Looper.getMainLooper());

    };

    public static OkHttpHelper getInstance(){

        return new OkHttpHelper();
    }

    // 对外公开的get方法
    public void get(String url,BaseCallback callback){
        Request request=buildRequest(url,null,HttpMethodType.GET);

        doRequest(request,callback);
    }
    //对外公开的post方法
    public void post(String url, Map<String,String> params,BaseCallback callback){
        Request request=buildRequest(url,params,HttpMethodType.POST);

        doRequest(request,callback);
    }


    //封装一个request方法，不管post或者get方法中都会用到
    public void doRequest(Request request, final BaseCallback callback){


        //在请求之前所做的事，比如弹出对话框等
        callback.onRequestBefore(request);



        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                callback.onFailure(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {

                callback.onResponse(response);

                //返回成功回调
                if (response.isSuccessful()){
                    String resultStr =response.body().string();

                    if (callback.mType==String.class){
                        //如果我们需要返回String类型
                        callback.onSuccess(response,resultStr);

                        callbackSuccess(callback,response,resultStr);
                    }else {
                        //如果返回的是其他类型，则利用Gson去解析
                        try {
                            //根据泛型返回解析制定的类型
                            Object object = gson.fromJson(resultStr, callback.mType);
                            callbackSuccess(callback,response,object);

                        }catch (JsonParseException e){
                            callback.onError(response,response.code(),e);

                        }
                    }


                }else {
                    //返回错误
                    callbackError(callback,response,null);
                }


            }
        });
    }

    //构建请求对象
    private Request buildRequest(String url,Map<String,String> params,HttpMethodType methodType){
        //构建request对象
        Request.Builder builder=new Request.Builder();

        builder.url(url);

        //判断方法类型
        if (methodType==HttpMethodType.GET){
            builder.get();
        }else if (methodType==HttpMethodType.POST){
            RequestBody body=buildFormData(params);

              builder.post(body);
        }
                return builder.build();
    }
    //构建FormData,返回RequestBody 通过Map的键值对构建请求对象的body
    private RequestBody buildFormData(Map<String,String> params){

        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (params!=null){
            //把params中的，每一个元素放入map中。
            for (Map.Entry<String,String> entry:params.entrySet()){

                builder.add(entry.getKey(),entry.getValue());

            }


        }
        return builder.build();

    }

    //在主线程中执行的回调
    private void callbackSuccess(final BaseCallback callback, final Response response, final Object object){

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                callback.onSuccess(response,object);

            }
        });

    }

    //在主线程中执行的回调
    private void callbackError(final BaseCallback callback, final Response response, final Exception e){

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                callback.onError(response,response.code(),e);

            }
        });

    }
    enum HttpMethodType{//构建一个枚举

        GET,
        POST

    }


}
