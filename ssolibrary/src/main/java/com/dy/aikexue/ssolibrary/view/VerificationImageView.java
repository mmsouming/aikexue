package com.dy.aikexue.ssolibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import org.cny.awf.view.ImageView;

/**
 * Created by yuhy on 2017/1/20.
 */

public class VerificationImageView extends ImageView implements View.OnClickListener{

    public VerificationImageView(Context context) {
        this(context, null);
    }

    public VerificationImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init(){
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //刷新
        refresh();
    }

    public void refresh(){
        setUrl(refreshUrl(getUrl()));
    }

    public String refreshUrl(String url){
        if(url != null){
            int index = url.indexOf("&time=");

            if(index != -1){
                url = url.substring(0, index);
            }

            url += "&time=" + System.currentTimeMillis();
        }
        return url;
    }

    public String getOriginalUrl(){
        String url = getUrl();
        if(url != null){
            int index = url.indexOf("&time=");

            if(index != -1){
                url = url.substring(0, index);
            }
        }
        return url;
    }
}














