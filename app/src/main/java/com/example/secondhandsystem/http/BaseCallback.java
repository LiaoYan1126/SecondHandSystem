package com.example.secondhandsystem.http;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/8/7.
 */

//在子类中执行获取其父类superclass，
// 判断superclass是Dao<T>还是Dao<Student>就可以知道是不是Dao<T>是不是被参数化了

public abstract class BaseCallback<T> {//一个泛型类

    //type用于方便JSON的解析
    public Type mType;

    //把T转换成Type 把type转换成对应的类
    static Type getSuperclassTypeParameter(Class<?> subclass){

        //得到带有泛型的类，获取当前对象的直接超类的 Type
        //
        Type superclass=subclass.getGenericSuperclass();

        // 如果 superclass指向的对象是Class类的实例，为true
        //如果superclass是该接口的一个实例化对象就表示superclass被参数化了
        if (superclass instanceof Class){
            throw new RuntimeException("Missing type parameter");

        }

        //取出当前类的泛型
        ParameterizedType parameterized=(ParameterizedType)superclass;

        //GetActualTypeArguments()这个方法的返回值是一个Type的数组里面存的就是我们的要的T
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    //构造的时候获得type的class
    public BaseCallback(){
        mType=getSuperclassTypeParameter(getClass());
    }

    public abstract void onRequestBefore(Request request);//请求之前调用
    public abstract void onFailure(Request request, IOException e) ;//请求失败调用（网络问题）

    public abstract void onResponse(Response response);

    public abstract void onSuccess(Response response,T t) ;//请求成功而且没有错误的时候调用
    public abstract void onError(Response response,int code,Exception e) ;//请求成功但是有错误的时候调用，例如Gson解析错误等

}
