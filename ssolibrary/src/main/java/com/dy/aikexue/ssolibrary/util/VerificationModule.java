package com.dy.aikexue.ssolibrary.util;

import android.app.Dialog;
import android.content.Context;

import com.dy.aikexue.ssolibrary.bean.Mark;
import com.dy.aikexue.ssolibrary.config.Config;
import com.dy.aikexue.ssolibrary.view.dialog.VerificationDialog;
import com.dy.sdk.utils.handler.GsonCallBack;
import com.dy.sdk.utils.handler.HResult;
import com.dy.sdk.view.dialog.LoadingDialog;

import org.cny.awf.net.http.H;

/**
 * Created by yuhy on 2017/1/20.
 */

public class VerificationModule {

    private VerifyCallback verifyCallback;
    private VerificationDialog verificationDialog;
    private LoadingDialog loadingDialog;
    private boolean verifying;
    private boolean canceled;
    private boolean alertDialog;
    private Context context;

    public VerificationModule(Context context, VerifyCallback verifyCallback){
        this(context, true, verifyCallback);
    }

    public VerificationModule(Context context, boolean alertDialog, VerifyCallback verifyCallback){
        this.context = context;
        this.verifyCallback = verifyCallback;
        this.alertDialog = alertDialog;
    }

    /**
     * 请求获取验证码
     */
    public void requestVerification(){

        if(verifying){
            return;
        }

        verifying = true;
        canceled = false;
        requestMark();
    }

    /**
     * 取消请求，释放资源
     */
    public void cancelVerify(){
        canceled = true;
        context = null;
        verifyCallback = null;
        verificationDialog = null;
        loadingDialog = null;
    }

    private void requestMark(){
        getLoadingDialog().show();
        H.doGet(Config.getValidationCodeUrl(), new GsonCallBack<HResult<Mark>>() {

            @Override
            public void onNext(HResult<Mark> markHResult, boolean isCache) throws Exception {

                if(isCache || verifyCallback == null){
                    return;
                }

                if(!canceled &&
                        markHResult != null &&
                        markHResult.isSuccess() &&
                        markHResult.getData() != null &&
                        markHResult.getData().getMark() != null){
                    if(markHResult.getData().getImg() == null){
                        verifyCallback.verifySuccess(markHResult.getData().getMark(), "");
                    }else{

                        if(alertDialog) {
                            if (context != null) {
                                //显示验证码dialog;
                                getVerificationDialog(context,
                                        markHResult.getData().getImg(),
                                        markHResult.getData().getMark()).show();
                            }
                        }else{
                            verifyCallback.verifyImage(markHResult.getData().getMark(), markHResult.getData().getImg());
                        }
                    }
                }else if(canceled){
                    verifyCallback.verifyCancel();
                }else{
                    verifyCallback.verifyFail(new Exception("get mark fail"));
                }
            }

            @Override
            public void onComplete() throws Exception {
                verifying = false;
                getLoadingDialog().dismiss();
            }

            @Override
            public void onError(HResult<Mark> markHResult, Throwable throwable) throws Exception {
                if(verifyCallback == null){
                    return;
                }
                if(canceled){
                    verifyCallback.verifyCancel();
                }else{
                    verifyCallback.verifyFail(new NetworkErrorException());
                }
            }
        });
    }

    private Dialog getVerificationDialog(Context context, String imageUrl, String mark){
        if(verificationDialog == null){
            verificationDialog = new VerificationDialog(context,
                    imageUrl,
                    mark);
            verificationDialog.setOnConfirmClickListener(new VerificationDialog.onConfirmClickListener() {
                @Override
                public void onConfirmClick(String mark, String verificationCode) {
                    if(!canceled){
                        verifyCallback.verifySuccess(mark, verificationCode);
                    }
                }
            });
        }else{
            verificationDialog.refreshVerification(mark, imageUrl);
        }
        return verificationDialog;
    }

    private Dialog getLoadingDialog(){
        if(loadingDialog == null){
            loadingDialog = new LoadingDialog(context, "加载中...");
        }
        return loadingDialog;
    }

    public static class NetworkErrorException extends Exception{}

    public interface VerifyCallback{
        /**
         * 如果需要弹框就调用verifySuccess()，不需要则调用verifyImage()返回验证码图片地址
         * @param mark
         * @param verificationCode
         */
        void verifySuccess(String mark, String verificationCode);
        void verifyImage(String mark, String imageUrl);
        void verifyFail(Throwable throwable);
        void verifyCancel();
    }

    public static abstract class DefaultVerifyCallback implements VerifyCallback{
        @Override
        public void verifyFail(Throwable throwable) {
            if(throwable instanceof NetworkErrorException){
                SToastUtil.toastShort("请检查网络后重试");
            }else{
                SToastUtil.toastShort("获取验证码失败");
            }
        }

        @Override
        public void verifyCancel() {

        }

        @Override
        public void verifyImage(String mark, String imageUrl) {

        }
    }
}
