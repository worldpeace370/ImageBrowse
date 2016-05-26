package com.lebron.imagebrowse;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**用于单例模式获得Volley请求队列，唯一的
 * Created by wuxiangkun on 2016/5/21 15:43.
 * Contacts wuxiangkun@live.com
 */
public class MyApplication extends Application{
//    声明一个新的RequestQueue对象
    private static  RequestQueue mRequestQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
}
