package com.snail.wanandroid.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * @author Snail
 * @date 2021/1/3
 * @description
 */
@GlideModule
public class GlideMoudle extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        //获取内存计算器
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
        //获取Glide默认内存缓存大小
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        //获取Glide默认图片池大小
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        //将数值修改为之前的1.1倍
        int myMemoryCacheSize = (int) (1.1 * defaultMemoryCacheSize);
        int myBitmapPoolSize = (int) (1.1 * defaultBitmapPoolSize);
        //修改默认值
        builder.setMemoryCache(new LruResourceCache(myMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(myBitmapPoolSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
//        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
//        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }
}
