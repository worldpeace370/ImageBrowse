package com.lebron.imagebrowse.imples;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**自定义Volley图片缓存类，实现Volley ImageCache接口
 * 通过静态内部类的形式创建单例
 * 无线程同步问题，实现了懒加载（Lazy Loading）。因为只有调用 getInstance 时才会装载内部类，才会创建实例
 * Created by wuxiangkun on 2016/5/23 12:33.
 * Contacts wuxiangkun@live.com
 */
public class MyBitmapCache implements ImageLoader.ImageCache{
    //LruCache对象
    private LruCache<String, Bitmap> lruCache;
    private MyBitmapCache(){
        int memoryCount = (int) Runtime.getRuntime().maxMemory();
        // 获取剩余内存的8分之一作为缓存
        int cacheSize = memoryCount / 8;

        lruCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public static MyBitmapCache getInstance(){
        return InnerClass.mMyBitmapCache;
    }

    private static class InnerClass{
        //单列模式
        private static final MyBitmapCache mMyBitmapCache = new MyBitmapCache();
    }

    @Override
    public Bitmap getBitmap(String url) {
        return lruCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        lruCache.put(url, bitmap);
    }
}
