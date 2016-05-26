package com.lebron.imagebrowse.imples;

import com.alibaba.fastjson.JSONArray;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lebron.imagebrowse.MyApplication;
import com.lebron.imagebrowse.bean.ImageInfo;
import com.lebron.imagebrowse.interfaces.GetJson;
import com.lebron.imagebrowse.interfaces.GetJsonListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wuxiangkun on 2016/5/21 20:44.
 * Contacts wuxiangkun@live.com
 */
public class VolleyGetJson implements GetJson{
    @Override
    public void downLoadJson(String url, final GetJsonListener listener) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.success(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error();
            }
        });
        //设置取消取消http请求标签 Activity的生命周期中的onStop()中调用
        request.setTag("volleyGet");
        //将当前请求加入请求队列,不要忘了INTERNET权限
        MyApplication.getRequestQueue().add(request);
    }

    /**
     * 根据下载来的json数据解析为List<ImageInfo>,
     * 并且根据先前加载的数据来得到刷新的数据，加载到LinkedList头部
     * @param response 下载来的json数据
     * @param oldList 刷新操作之前的List<ImageInfo>,用来跟新得到的数据进行判断，将新数据添加到头部，避免重复数据
     * @return
     */
    public List<ImageInfo> getImageInfoFromJson(String response, LinkedList<ImageInfo> oldList){
        List<ImageInfo> parseList = JSONArray.parseArray(response, ImageInfo.class);
        //如果oldList.size()==0，说明是第一次解析
        if (parseList!=null && oldList.size()==0){
            oldList.addAll(parseList);
            return oldList;
        } else if (parseList!=null){//如果oldList!=null，说明是刷新操作，需要进行比较
            //比较解析来的parseList所有元素和oldList第一个元素的id值，判断是否是新数据。避免重复
            for (int i = parseList.size()-1; i >= 0 ; i--) { //第一个元素是最新的，所以从最后遍历
                if (Integer.valueOf(parseList.get(i).getId()) > Integer.valueOf(oldList.get(0).getId())){
                    //添加到first处
                    oldList.addFirst(parseList.get(i));
                }
            }
            return oldList;
        }
        return null;
    }

    public List<ImageInfo> getImageInfoFromJson(String response){
        List<ImageInfo> parseList = JSONArray.parseArray(response, ImageInfo.class);
        LinkedList<ImageInfo> imageInfoList = new LinkedList<>();
        imageInfoList.addAll(parseList);
        return imageInfoList;
    }
}
