package com.example.sachi.whatsappmodme.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.sachi.whatsappmodme.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class UniversalImageLoader {
    public static int DEFAULT_IMG = R.drawable.ic_default_img;
    private Context context;
    public UniversalImageLoader(Context context){
        this.context=context;
    }
    public ImageLoaderConfiguration getConfig(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(DEFAULT_IMG).showImageForEmptyUri(DEFAULT_IMG)
                .showImageOnFail(DEFAULT_IMG).considerExifParams(true)
                .resetViewBeforeLoading(true).imageScaleType(ImageScaleType.EXACTLY).build();
        ImageLoaderConfiguration config =new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(options)
                .memoryCache(new WeakMemoryCache()).diskCacheSize(100*1024*1024).build();
        return config;
    }
    public static void setImage(String ImageURL, ImageView img, final ProgressBar mprogress, String append){
        ImageLoader imageloader = ImageLoader.getInstance();
        imageloader.displayImage(append + ImageURL,img, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(mprogress!=null){
                    mprogress.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(mprogress!=null){
                    mprogress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(mprogress!=null){
                    mprogress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(mprogress!=null){
                    mprogress.setVisibility(View.GONE);
                }
            }
        });
    }
}
