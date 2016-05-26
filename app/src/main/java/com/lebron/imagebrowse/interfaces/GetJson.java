package com.lebron.imagebrowse.interfaces;

/**定义下载json数据并解析json的接口
 * Created by wuxiangkun on 2016/5/21 20:40.
 * Contacts wuxiangkun@live.com
 */
public interface GetJson {
    void downLoadJson(String url, GetJsonListener listener);
}
